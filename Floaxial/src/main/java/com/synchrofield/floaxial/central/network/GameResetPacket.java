package com.synchrofield.floaxial.central.network;

import net.minecraft.network.FriendlyByteBuf;

// send to client to reset all buffer
public class GameResetPacket {

	protected GameResetPacket() {

	}

	public static GameResetPacket of() {

		return new GameResetPacket();
	}

	public static GameResetPacket fromBuffer(FriendlyByteBuf buffer) {

		GameResetPacket packet = GameResetPacket.of();

		return packet;
	}

	public static void toBuffer(GameResetPacket packet, FriendlyByteBuf buffer) {

	}
}
