package com.synchrofield.floaxial.central.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ResetCommand extends ServerCommand {

	@Override
	public void register(CommandDispatcher<CommandSourceStack> dispatcher) {

		dispatcher.register(Commands.literal("FloReset")
				.executes((command) -> {

					commandControl().onCommandReset(levelGet(command), playerGet(command));

					return Command.SINGLE_SUCCESS;
				}));
	}

	public static ResetCommand of() {

		return new ResetCommand();
	}
}
