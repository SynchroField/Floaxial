package com.synchrofield.floaxial.central.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ReloadCommand extends ServerCommand {

	@Override
	public void register(CommandDispatcher<CommandSourceStack> dispatcher) {

		dispatcher.register(Commands.literal("FloReload")
				.executes((command) -> {

					commandControl().onCommandReload(levelGet(command), playerGet(command));

					return Command.SINGLE_SUCCESS;
				}));
	}

	public static ReloadCommand of() {

		return new ReloadCommand();
	}
}
