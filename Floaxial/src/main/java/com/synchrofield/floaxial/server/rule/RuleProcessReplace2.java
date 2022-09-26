package com.synchrofield.floaxial.server.rule;

import com.mojang.datafixers.util.Pair;
import com.synchrofield.library.configure.ConfigureException;
import com.synchrofield.library.terrain.Geometry;
import com.synchrofield.library.terrain.Terrain;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RuleProcessReplace2 extends RuleProcess {

	public final String stateName0;
	public BlockState state0;
	public final int weight0;

	public final String stateName1;
	public BlockState state1;
	public final int weight1;

	public final int destinationOffset;

	protected RuleProcessReplace2(String stateName0, int weight0, String stateName1, int weight1,
			int destinationOffset) {

		this.stateName0 = stateName0;
		this.weight0 = weight0;
		this.stateName1 = stateName1;
		this.weight1 = weight1;
		this.destinationOffset = destinationOffset;
	}

	public static RuleProcessReplace2 of(String stateName0, int weight0, String stateName1,
			int weight1, int destinationOffset) {

		return new RuleProcessReplace2(stateName0, weight0, stateName1, weight1, destinationOffset);
	}

	@Override
	public void compile() throws ConfigureException {

		state0 = Terrain.blockStateFromString(stateName0);
		state1 = Terrain.blockStateFromString(stateName1);
	}

	@Override
	public Pair<BlockPos, Integer> run(ServerLevel level, BlockPos location) {

		int totalWeight = weight0 + weight1;

		BlockState winState;
		int outputIndex;
		if (level.random.nextInt(totalWeight) < weight0) {

			outputIndex = 0;
			winState = state0;
		}
		else {

			outputIndex = 1;
			winState = state1;
		}

		BlockPos destinationLocation;

		if (destinationOffset == Geometry.DirectionPackNone) {

			destinationLocation = location;
		}
		else {

			// offset
			destinationLocation = location.relative(Geometry.DirectionFromPack[destinationOffset]);
		}

		if (!level.setBlock(destinationLocation, winState, Block.UPDATE_CLIENTS)) {

			// fail ignore
		}

		return Pair.of(destinationLocation, outputIndex);
	}

	@Override
	public BlockState output0State() {

		return state0;
	}

	@Override
	public BlockState output1State() {

		return state1;
	}
}
