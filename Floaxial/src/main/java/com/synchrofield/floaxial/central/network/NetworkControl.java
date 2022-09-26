package com.synchrofield.floaxial.central.network;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.synchrofield.floaxial.central.configure.ProductConfigure;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkControl {

	public static final String ChannelName = "main";
	public static final String ProtocolVersion = "1";
	public static final String ServerProtocolVersion = ProtocolVersion;
	public static final String ClientProtocolVersion = ProtocolVersion;

	public static final int ClientRockMovePacketIndex = 0;
	public static final int ReloadPacketIndex = 1;
	public static final int ClientResetPacketIndex = 2;
	public static final int ClientStatisticsPacketIndex = 3;

	public boolean initializeIs;

	@Nullable
	public SimpleChannel channel;

	@Nullable
	public ClientNetworkReceive clientReceive;

	protected NetworkControl(boolean initializeIs, SimpleChannel channel,
			ClientNetworkReceive clientReceive) {

		this.initializeIs = initializeIs;
		this.channel = channel;
		this.clientReceive = clientReceive;
	}

	public static NetworkControl ofEmpty() {

		return new NetworkControl(false, null, null);
	}

	public void initialize(ClientNetworkReceive clientControl) {

		this.channel = channelCreate();
		this.clientReceive = clientControl;
		this.initializeIs = true;
	}

	public static SimpleChannel channelCreate() {

		return NetworkRegistry.newSimpleChannel(
				new ResourceLocation(ProductConfigure.Name, ChannelName), () -> ProtocolVersion,
				ClientProtocolVersion::equals, ServerProtocolVersion::equals);
	}

	public void packetRegister() {

		channel.registerMessage(NetworkControl.ClientRockMovePacketIndex, DropPacket.class,
				DropPacket::toBuffer, DropPacket::fromBuffer, this::clientReceive);

		channel.registerMessage(NetworkControl.ReloadPacketIndex, ReloadPacket.class,
				ReloadPacket::toBuffer, ReloadPacket::fromBuffer, this::clientReceive);

		channel.registerMessage(NetworkControl.ClientResetPacketIndex, GameResetPacket.class,
				GameResetPacket::toBuffer, GameResetPacket::fromBuffer, this::clientReceive);

		channel.registerMessage(NetworkControl.ClientStatisticsPacketIndex, StatisticsPacket.class,
				StatisticsPacket::toBuffer, StatisticsPacket::fromBuffer, this::clientReceive);
	}

	public void clientReceive(GameResetPacket packet, Supplier<NetworkEvent.Context> context) {

		assert initializeIs;

		context.get()
				.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
						() -> () -> clientReceive.receive(packet)));

		context.get()
				.setPacketHandled(true);
	}

	public void clientReceive(ReloadPacket packet, Supplier<NetworkEvent.Context> context) {

		assert initializeIs;

		context.get()
				.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
						() -> () -> clientReceive.receive(packet)));

		context.get()
				.setPacketHandled(true);
	}

	public void clientReceive(DropPacket packet, Supplier<NetworkEvent.Context> context) {

		assert initializeIs;

		context.get()
				.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
						() -> () -> clientReceive.receive(packet)));

		context.get()
				.setPacketHandled(true);
	}

	public void clientReceive(StatisticsPacket packet, Supplier<NetworkEvent.Context> context) {

		assert initializeIs;

		context.get()
				.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
						() -> () -> clientReceive.receive(packet)));

		context.get()
				.setPacketHandled(true);
	}
}
