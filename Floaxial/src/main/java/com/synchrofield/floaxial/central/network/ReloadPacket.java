package com.synchrofield.floaxial.central.network;

import net.minecraft.network.FriendlyByteBuf;

public class ReloadPacket {

	protected ReloadPacket() {

	}

	public static ReloadPacket of() {

		return new ReloadPacket();
	}

	public static ReloadPacket fromBuffer(FriendlyByteBuf buffer) {

		ReloadPacket packet = ReloadPacket.of();

		return packet;
	}

	public static void toBuffer(ReloadPacket packet, FriendlyByteBuf buffer) {

	}
}
