package com.synchrofield.floaxial.client;

import javax.annotation.Nullable;

import com.synchrofield.floaxial.central.CentralData;
import com.synchrofield.floaxial.central.configure.ClientConfigure;
import com.synchrofield.floaxial.central.configure.MaterialConfigureList;
import com.synchrofield.floaxial.central.configure.ProductConfigure;
import com.synchrofield.floaxial.central.droplet.MaterialTable;
import com.synchrofield.floaxial.central.network.DropPacket;
import com.synchrofield.floaxial.central.network.GameResetPacket;
import com.synchrofield.floaxial.central.network.ReloadPacket;
import com.synchrofield.floaxial.central.network.StatisticsPacket;
import com.synchrofield.floaxial.central.registry.CentralRegistry;
import com.synchrofield.floaxial.central.statistics.ClientStatistics;
import com.synchrofield.floaxial.central.statistics.ServerStatistics;
import com.synchrofield.library.client.ClientChat;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;

// integrated server
public class ClientIntegrated extends Client {

	public ClientConfigure configure;
	public MaterialConfigureList materialConfigure;

	@Nullable
	public Level levelControl;

	public final StatisticsDisplay statisticsDisplay;

	public final CentralData centralData;

	public final CentralRegistry centralRegistry;

	public final ClientChat clientChat;

	protected ClientIntegrated(ClientConfigure configure,
			MaterialConfigureList commonMaterialConfigure, CentralRegistry centralRegistry,
			CentralData centralData, ClientChat clientChat) {

		this.configure = configure;
		this.materialConfigure = commonMaterialConfigure;
		this.centralRegistry = centralRegistry;
		this.centralData = centralData;
		this.clientChat = clientChat;

		this.statisticsDisplay = StatisticsDisplay.of();

		this.renderLayerSet();
	}

	public static ClientIntegrated of(ClientConfigure configure,
			MaterialConfigureList commonMaterialConfigure, CentralRegistry centralRegistry,
			CentralData centralData) {

		ClientChat clientChat = ClientChat.of();

		return new ClientIntegrated(configure, commonMaterialConfigure, centralRegistry,
				centralData, clientChat);
	}

	@Override
	public void reset(ClientLevel level) {

		levelControl = Level.of(level, configure.level, materialConfigure, centralRegistry);
	}

	@Override
	public void onConfigure(ClientLevel level, ClientConfigure configure,
			MaterialConfigureList materialConfigure) {

		this.configure = configure;
		this.materialConfigure = materialConfigure;

		levelControl = Level.of(level, configure.level, materialConfigure, centralRegistry);
	}

	public void renderLayerSet() {

		// palm
		ItemBlockRenderTypes.setRenderLayer(centralRegistry.block.PalmLeafXObject.get(),
				RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(centralRegistry.block.PalmLeafZObject.get(),
				RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(centralRegistry.block.PalmLeafXEndObject.get(),
				RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(centralRegistry.block.JungleSaplingObject.get(),
				RenderType.cutout());

		// mark
		ItemBlockRenderTypes.setRenderLayer(centralRegistry.block.MarkObject.get(),
				RenderType.translucent());

		// drop
		ItemBlockRenderTypes.setRenderLayer(centralRegistry.block.DropGhostObject.get(),
				RenderType.translucent());

		// fluid
		ItemBlockRenderTypes.setRenderLayer(centralRegistry.fluid.SaltWaterStillFluidObject.get(),
				RenderType.translucent());
	}

	public boolean levelCheck() {

		return levelControl != null;
	}

	public void statisticsDisplay(ClientLevel level, ServerStatistics serverStatistics,
			MaterialTable materialTable) {

		assert levelCheck();

		ClientStatistics clientStatistics = clientStatisticsDerive(level);

		statisticsDisplay.fullDisplay(clientChat, levelControl.dropletControl.materialTable,
				serverStatistics, clientStatistics);
	}

	public ClientStatistics clientStatisticsDerive(ClientLevel level) {

		ClientStatistics result = ClientStatistics.of();

		result.tick = level.getGameTime();
		result.drop = levelControl.statisticsDerive();

		return result;
	}

	@Override
	public void onRender(ClientLevel level, RenderLevelStageEvent event) {

		level.updateSkyBrightness();
		
		levelControl.onRender(level, event);
	}

	@Override
	public void receive(ReloadPacket packet) {

		// assume configure has been updated somehow, have to work this out proerly for remote
		// client
		onConfigure(clientLevel(), configure, materialConfigure);

		clientChat.chatAdd(ProductConfigure.NameDisplay + " client configuration reload");
	}

	@Override
	public void receive(GameResetPacket packet) {

		reset(clientLevel());
	}

	@Override
	public void receive(DropPacket packet) {

		levelControl.animateAdd(clientLevel(), packet);
	}

	@Override
	public void receive(StatisticsPacket packet) {

		statisticsDisplay(clientLevel(), packet.server, centralData.materialTable);
	}

	public void onFogRender(EntityViewRenderEvent.RenderFogEvent event) {

		if (event.getMode() == FogMode.FOG_TERRAIN) {

			event.setNearPlaneDistance(32.0f);
			event.setFarPlaneDistance(128.0f);
			event.setCanceled(true);
		}
	}

	public void onFogColor(EntityViewRenderEvent.FogColors event) {

		FogType fogType = event.getCamera()
				.getFluidInCamera();

		if (fogType == FogType.WATER) {

			event.setRed(configure.waterFogColor.x());
			event.setGreen(configure.waterFogColor.y());
			event.setBlue(configure.waterFogColor.z());
		}
		else {

			//			event.setRed(0.9f);
			//			event.setGreen(0.5f);
			//			event.setBlue(0.5f);
		}
	}

	public void onOverlayRender(RenderBlockOverlayEvent event) {

		if (event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.WATER) {

			// don't bother with overlay for any fluid
			event.setCanceled(true);
		}
	}
}
