package com.synchrofield.floaxial.server.rule;

import com.synchrofield.library.configure.ConfigureException;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class RuleCriteria {

	public BlockCriteria centerCriteria;

	protected RuleCriteria(BlockCriteria centerCriteria) {

		this.centerCriteria = centerCriteria;
	}

	public static RuleCriteria of(BlockCriteria centerCriteria) {

		return new RuleCriteria(centerCriteria);
	}

	public void compile() throws ConfigureException {

		centerCriteria = centerCriteria.withCompile();
	}

	public boolean centerMatchIs(ServerLevel level, BlockPos location) {

		return centerCriteria.matchIs(level, location);
	}

	public boolean matchIs(ServerLevel level, BlockPos location) {

		return centerMatchIs(level, location);
	}
}
