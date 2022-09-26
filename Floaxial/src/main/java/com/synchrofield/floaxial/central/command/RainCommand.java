package com.synchrofield.floaxial.central.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.synchrofield.library.math.MathUtility;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RainCommand extends ServerCommand {

	public static final int TimeSizeDefault = 100;
	public static final int TimeSizeMaximum = 10000;

	@Override
	public void register(CommandDispatcher<CommandSourceStack> dispatcher) {

		dispatcher.register(Commands.literal("FloRain")
				.executes((command) -> {

					commandControl().onCommandRain(levelGet(command), playerGet(command),
							TimeSizeDefault);

					return Command.SINGLE_SUCCESS;
				})
				.then(Commands.argument("timeSize", IntegerArgumentType.integer())
						.executes((command) -> {

							Integer timeSize = IntegerArgumentType.getInteger(command, "timeSize");
							timeSize = MathUtility.cap(timeSize, TimeSizeMaximum);

							commandControl().onCommandRain(levelGet(command), playerGet(command),
									timeSize);

							return Command.SINGLE_SUCCESS;
						})));
	}

	public static RainCommand of() {

		return new RainCommand();
	}
}
