package com.synchrofield.floaxial.central.command;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

// receive game command
public interface CommandReceive {

	public void onCommandReset(ServerLevel level, ServerPlayer player);

	public void onCommandReload(ServerLevel level, ServerPlayer player);

	public void onCommandStatistics(ServerLevel level, ServerPlayer player);

	public void onCommandRain(ServerLevel level, ServerPlayer player, int timeSize);
}
