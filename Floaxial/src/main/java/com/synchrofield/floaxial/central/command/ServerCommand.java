package com.synchrofield.floaxial.central.command;

import javax.annotation.Nullable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.synchrofield.floaxial.central.CentralControl;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public abstract class ServerCommand {

	public abstract void register(CommandDispatcher<CommandSourceStack> dispatcher);

	@Nullable
	public ServerPlayer playerGet(CommandContext<CommandSourceStack> command) {

		if (command.getSource()
				.getEntity() instanceof ServerPlayer) {

			return (ServerPlayer) command.getSource()
					.getEntity();
		}
		else {

			return null;
		}
	}

	@Nullable
	public ServerLevel levelGet(CommandContext<CommandSourceStack> command) {

		ServerPlayer player = playerGet(command);
		if (player == null) {

			return null;
		}

		if (player.level instanceof ServerLevel) {

			return (ServerLevel) player.level;
		}
		else {

			return null;
		}
	}

	protected CommandReceive commandControl() {

		return CentralControl.commandReceive();
	}
}
