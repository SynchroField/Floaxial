package com.synchrofield.floaxial.server.rule;

import com.mojang.datafixers.util.Pair;
import com.synchrofield.library.configure.ConfigureException;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RuleProcess {

	public void compile() throws ConfigureException {

	}

	// return location and output index
	public abstract Pair<BlockPos, Integer> run(ServerLevel level, BlockPos location);

	public abstract BlockState output0State();

	public abstract BlockState output1State();
}
