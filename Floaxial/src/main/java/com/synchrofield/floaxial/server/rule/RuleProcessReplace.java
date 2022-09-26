package com.synchrofield.floaxial.server.rule;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;
import com.synchrofield.library.configure.ConfigureException;
import com.synchrofield.library.terrain.Geometry;
import com.synchrofield.library.terrain.Terrain;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RuleProcessReplace extends RuleProcess {

	public final String destinationStateName;
	@Nullable
	public BlockState destinationState;

	public final int destinationOffset;

	protected RuleProcessReplace(String destinationStateName, int destinationOffset) {

		this.destinationStateName = destinationStateName;
		this.destinationOffset = destinationOffset;
	}

	public static RuleProcessReplace of(String destinationStateName, int destinationOffset) {

		return new RuleProcessReplace(destinationStateName, destinationOffset);
	}

	@Override
	public void compile() throws ConfigureException {

		destinationState = Terrain.blockStateFromString(destinationStateName);
	}

	@Override
	public Pair<BlockPos, Integer> run(ServerLevel level, BlockPos location) {

		BlockPos destinationLocation;

		if (destinationOffset == Geometry.DirectionPackNone) {

			destinationLocation = location;
		}
		else {

			// offset
			destinationLocation = location.relative(Geometry.DirectionFromPack[destinationOffset]);
		}

		if (!level.setBlock(destinationLocation, destinationState, Block.UPDATE_CLIENTS)) {

			// fail ignore
		}

		return Pair.of(destinationLocation, 0);
	}

	@Override
	public BlockState output0State() {

		return destinationState;
	}

	@Override
	public BlockState output1State() {

		return null;
	}
}
