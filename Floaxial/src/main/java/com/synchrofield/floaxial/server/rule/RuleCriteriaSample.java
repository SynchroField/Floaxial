package com.synchrofield.floaxial.server.rule;

import com.synchrofield.library.configure.ConfigureException;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class RuleCriteriaSample extends RuleCriteria {

	// touch
	public BlockCriteria touchCriteria;

	public final int radius;

	protected RuleCriteriaSample(BlockCriteria centerCriteria, BlockCriteria touchCriteria,
			int radius) {

		super(centerCriteria);

		this.touchCriteria = touchCriteria;
		this.radius = radius;
	}

	public static RuleCriteriaSample of(BlockCriteria centerCriteria, BlockCriteria touchCriteria,
			int radius) {

		return new RuleCriteriaSample(centerCriteria, touchCriteria, radius);
	}

	@Override
	public void compile() throws ConfigureException {

		super.compile();

		touchCriteria = touchCriteria.withCompile();
	}

	@Override
	public boolean matchIs(ServerLevel level, BlockPos location) {

		if (!super.matchIs(level, location)) {

			return false;
		}

		int xOffset = level.random.nextInt(radius * 2) - radius;
		int yOffset = level.random.nextInt(radius * 2) - radius;
		int zOffset = level.random.nextInt(radius * 2) - radius;

		BlockPos sampleLocation = location.offset(xOffset, yOffset, zOffset);

		if (touchCriteria.matchIs(level, sampleLocation)) {

			return true;
		}

		return false;
	}
}
