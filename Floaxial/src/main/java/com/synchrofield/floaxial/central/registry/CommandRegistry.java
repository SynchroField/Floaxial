package com.synchrofield.floaxial.central.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.synchrofield.floaxial.central.command.RainCommand;
import com.synchrofield.floaxial.central.command.ReloadCommand;
import com.synchrofield.floaxial.central.command.ResetCommand;
import com.synchrofield.floaxial.central.command.StatisticsCommand;

import net.minecraft.commands.CommandSourceStack;

public class CommandRegistry {

	public final ReloadCommand reload;
	public final ResetCommand reset;
	public final StatisticsCommand statistics;
	public final RainCommand rain;

	protected CommandRegistry() {

		this.reload = ReloadCommand.of();
		this.reset = ResetCommand.of();
		this.statistics = StatisticsCommand.of();
		this.rain = RainCommand.of();
	}

	public static CommandRegistry of() {

		return new CommandRegistry();
	}

	public void register(CommandDispatcher<CommandSourceStack> dispatcher) {

		reload.register(dispatcher);
		reset.register(dispatcher);
		statistics.register(dispatcher);
		rain.register(dispatcher);
	}
}
