package com.synchrofield.floaxial.central;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.synchrofield.floaxial.central.block.TerrainEventReceive;
import com.synchrofield.floaxial.central.command.CommandReceive;
import com.synchrofield.floaxial.central.configure.CentralConfigure;
import com.synchrofield.floaxial.central.configure.ProductConfigure;
import com.synchrofield.floaxial.central.network.GameResetPacket;
import com.synchrofield.floaxial.central.network.NetworkControl;
import com.synchrofield.floaxial.central.network.ReloadPacket;
import com.synchrofield.floaxial.central.network.StatisticsPacket;
import com.synchrofield.floaxial.central.registry.CentralRegistry;
import com.synchrofield.floaxial.central.statistics.ServerStatistics;
import com.synchrofield.floaxial.client.Client;
import com.synchrofield.floaxial.client.ClientDedicated;
import com.synchrofield.floaxial.client.ClientIntegrated;
import com.synchrofield.floaxial.server.Server;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;

// Entry point and executive control for both client and server.  
@Mod(ProductConfigure.Name)
public class CentralControl implements CommandReceive, TerrainEventReceive {

	protected static CentralControl instance;

	// track any loading error so player can be notified upon connect
	public ErrorState state;

	public final Logger log;
	public final CentralConfigure configure;
	public final CentralRegistry registry;
	public NetworkControl network;
	public CentralData centralData;
	public Server server;
	public Client client;

	public CentralControl() {

		state = ErrorState.ofStart();

		assert instance == null;
		instance = this;

		IEventBus bus = FMLJavaModLoadingContext.get()
				.getModEventBus();

		eventAttach(bus);

		this.log = LogUtils.getLogger();

		this.configure = CentralConfigure.of();

		try {

			this.configure.cacheCopyFromStore();
		}
		catch (RuntimeException e) {

			onError(e);
		}
		finally {

			this.registry = CentralRegistry.of(bus, this.configure.registry);
		}
	}

	public void eventAttach(IEventBus bus) {

		MinecraftForge.EVENT_BUS.register(this);

		bus.addListener(this::onRegisterComplete);
		bus.addListener(this::onDedicatedInitialize);
		bus.addListener(this::onClientInitialize);
		bus.addListener(this::onRegisterBlockColor);
	}

	// initialize phase 2
	public void onRegisterComplete(final FMLCommonSetupEvent event) {

		try {

			configure.validateBlock();

			centralData = CentralData.of(configure.material, registry);

			server = Server.of();

			network = NetworkControl.ofEmpty();

			// after vanilla replace so tree can see new leaf
			registerManual();

			if (configure.generate.enableIs) {

				registry.generate.islandChunkGeneratorInstall();
			}

			registry.onConfigure(configure);

		}
		catch (RuntimeException e) {

			onError(e);
			return;
		}
	}

	// game objects that don't use deferred register
	protected void registerManual() {

		registry.tree.register();
		registry.generate.register();
	}

	// dedicated server
	// initialize phase 3
	protected void onDedicatedInitialize(final FMLDedicatedServerSetupEvent event) {

		try {

			// dummy client
			client = ClientDedicated.of();

			network.initialize(client);
			network.packetRegister();

			// initialize complete
			state = state.withInitializeEnd();
		}
		catch (RuntimeException e) {

			onError(e);
			return;
		}
	}

	// integrated server
	// initialize phase 3
	protected void onClientInitialize(final FMLClientSetupEvent event) {

		try {

			// regular client
			client = ClientIntegrated.of(configure.client, configure.material, registry,
					centralData);

			network.initialize(client);
			network.packetRegister();

			// initialize complete
			state = state.withInitializeEnd();
		}
		catch (RuntimeException e) {

			onError(e);
			return;
		}
	}

	// commands need executive access
	public static CommandReceive commandReceive() {

		return instance;
	}

	// called by Block classes to notify of any new materials in level
	public static TerrainEventReceive terrainEventReceive() {

		return instance;
	}

	public void onError(Exception e) {

		onError(e.getMessage());
	}

	public void onRegisterBlockColor(ColorHandlerEvent.Block event) {

		if (state.errorIs) {

			return;
		}

		try {

			registry.block.onRegisterColor(event.getBlockColors());
		}
		catch (RuntimeException e) {

			onError(e);
			return;
		}
	}

	@SubscribeEvent
	public void onCommandRegister(RegisterCommandsEvent event) {

		if (state.errorIs) {

			return;
		}

		try {

			registry.command.register(event.getDispatcher());
		}
		catch (RuntimeException e) {

			onError(e);
			return;
		}
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {

		if (state.errorIs) {

			return;
		}

		try {

			if (event.getWorld()
					.isClientSide()) {

				// client
				if (event.getWorld() instanceof ClientLevel) {

					ClientLevel level = (ClientLevel) event.getWorld();

					client.reset(level);
				}
			}
			else {

				// server
				if (event.getWorld() instanceof ServerLevel) {

					if (event.getWorld() == event.getWorld()
							.getServer()
							.overworld()) {

						server.reset(configure.server, configure.material, network.channel,
								centralData);
					}
				}
			}
		}
		catch (RuntimeException e) {

			onError(e);
			return;
		}
	}

	@SubscribeEvent
	public void onPlayerConnect(PlayerEvent.PlayerLoggedInEvent event) {

		if (state.errorIs) {

			String errorText = "Error: " + state.errorText.get();

			event.getPlayer()
					.sendMessage(new TextComponent(errorText), Util.NIL_UUID);
		}
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {

		if (state.errorIs) {

			return;
		}

		if (event.phase == Phase.END) {

			if (event.type == TickEvent.Type.WORLD) {

				if (event.world.dimension()
						.location()
						.getPath() == "overworld") {

					if (event.side == LogicalSide.SERVER) {

						// server
						if (event.world instanceof ServerLevel) {

							ServerLevel level = (ServerLevel) event.world;

							server.onTick(level);
						}
					}
					else {

						// client
						if (event.world instanceof ClientLevel) {

							ClientLevel level = (ClientLevel) event.world;

							client.onTick(level);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityBlockPlace(BlockEvent.EntityPlaceEvent event) {

		onBlockPlace((ServerLevel) event.getEntity().level, event.getPos(), event.getState());
	}

	@SubscribeEvent
	public void onEntityBlockListPlace(BlockEvent.EntityMultiPlaceEvent event) {

		onBlockPlace((ServerLevel) event.getEntity().level, event.getPos(), event.getState());
	}

	@SubscribeEvent
	public void onBlockNeighborNotify(NeighborNotifyEvent event) {

		onBlockPlace((ServerLevel) event.getWorld(), event.getPos(), event.getState());
	}

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {

		if (state.errorIs) {

			return;
		}

		if (event.getPos() != null) {

			server.onGap(event.getPos());
		}
	}

	@SubscribeEvent
	public void onLevelRender(RenderLevelStageEvent event) {

		if (state.errorIs) {

			return;
		}

		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {

			Minecraft minecraft = Minecraft.getInstance();

			client.onRender(minecraft.level, event);
		}
	}

	@Override
	public void onCommandReset(ServerLevel level, ServerPlayer player) {

		if (state.errorIs) {

			return;
		}

		centralData = CentralData.of(configure.material, registry);

		// server
		server.reset(configure.server, configure.material, network.channel, centralData);

		// client resets itself upon receive
		GameResetPacket resetPacket = GameResetPacket.of();
		network.channel.send(PacketDistributor.ALL.noArg(), resetPacket);

		// notify player
		player.sendMessage(new TextComponent(ProductConfigure.NameDisplay + " reset"),
				Util.NIL_UUID);
	}

	@Override
	public void onCommandReload(ServerLevel level, ServerPlayer player) {

		if (!state.initializeIs) {

			// mod in half loaded state, no safe way to reload except to restart
			player.sendMessage(
					new TextComponent(ProductConfigure.NameDisplay + " was unable to load."),
					Util.NIL_UUID);

			return;
		}

		try {

			// updates client configure too, later need to work this out properly with forge client config type and
			// remote client
			configure.cacheCopyFromStore();
			configure.validateBlock();

			registry.onConfigure(configure);

			centralData = CentralData.of(configure.material, registry);

			server.reset(configure.server, configure.material, network.channel, centralData);
		}
		catch (RuntimeException e) {

			player.sendMessage(new TextComponent(
					ProductConfigure.NameDisplay + " Configuration error: " + e.getMessage()),
					Util.NIL_UUID);

			onError(e);
			return;
		}

		// force good program state, assuming it was a configure error before
		if (state.errorIs) {

			state = state.withErrorClear();
		}

		// client reloads itself upon receive
		ReloadPacket reloadPacket = ReloadPacket.of();
		network.channel.send(PacketDistributor.ALL.noArg(), reloadPacket);

		// notify player
		player.sendMessage(
				new TextComponent(ProductConfigure.NameDisplay + " server configuration reload"),
				Util.NIL_UUID);
	}

	@Override
	public void onCommandStatistics(ServerLevel level, ServerPlayer player) {

		if (state.errorIs) {

			return;
		}

		ServerStatistics serverStatistics = server.statisticsDerive(level);

		StatisticsPacket packet = StatisticsPacket.of(serverStatistics);

		network.channel.send(PacketDistributor.PLAYER.with(() -> player), packet);
	}

	@Override
	public void onCommandRain(ServerLevel level, ServerPlayer player, int timeSize) {

		if (state.errorIs) {

			return;
		}

		server.rainStart(level, timeSize);

		// notify player
		player.sendMessage(new TextComponent("Rain enable, period " + timeSize / 20 + " second"),
				Util.NIL_UUID);
	}

	// fatal error, returns control to caller
	public void onError(String text) {

		// log 
		log.error(text);

		// only record first error
		if (!state.errorIs) {

			// display to user on world load
			state = state.withErrorSet(text);
		}

		// continue in disable state
	}

	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load event) {

		if (state.errorIs) {

			return;
		}

		if (event.getWorld() instanceof ServerLevel) {

			if (event.getChunk() != null) {

				ChunkAccess chunk = event.getChunk();

				for (int sectionIndex = 0; sectionIndex < chunk
						.getSections().length; sectionIndex++) {

					LevelChunkSection section = chunk.getSections()[sectionIndex];

					int sectionY = section.bottomBlockY() / 16;

					SectionPos sectionLocation = SectionPos.of(chunk.getPos().x, sectionY,
							chunk.getPos().z);

					server.onSectionLoad(sectionLocation, section);
				}
			}
		}
	}

	@SubscribeEvent
	public void onFogRender(EntityViewRenderEvent.RenderFogEvent event) {

		if (state.errorIs) {

			return;
		}

		client.onFogRender(event);
	}

	@SubscribeEvent
	public void onFogColor(EntityViewRenderEvent.FogColors event) {

		if (state.errorIs) {

			return;
		}

		client.onFogColor(event);
	}

	@SubscribeEvent
	public void onOverlayRender(RenderBlockOverlayEvent event) {

		if (state.errorIs) {

			return;
		}

		client.onOverlayRender(event);
	}

	@Override
	public void onBlockPlace(ServerLevel level, BlockPos location, BlockState state) {

		if (this.state.errorIs) {

			return;
		}

		server.onBlockPlace(level, location, state);
	}
}