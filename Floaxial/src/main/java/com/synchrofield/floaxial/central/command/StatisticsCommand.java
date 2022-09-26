package com.synchrofield.floaxial.central.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class StatisticsCommand extends ServerCommand {

	@Override
	public void register(CommandDispatcher<CommandSourceStack> dispatcher) {

		dispatcher.register(Commands.literal("FloStatistics")

				// no argument
				.executes((command) -> {

					commandControl().onCommandStatistics(levelGet(command), playerGet(command));

					return Command.SINGLE_SUCCESS;
				}));
	}

	public static StatisticsCommand of() {

		return new StatisticsCommand();
	}
}