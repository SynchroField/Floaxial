package com.synchrofield.floaxial.server.rule;

import com.synchrofield.library.configure.ConfigureException;
import com.synchrofield.library.terrain.Geometry;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class RuleCriteriaTouch extends RuleCriteria {

	public BlockCriteria touchCriteria;
	public final boolean touchIsAny;
	public final int touchPattern;

	protected RuleCriteriaTouch(BlockCriteria centerCriteria, BlockCriteria touchCriteria,
			boolean touchIsAny, int touchPattern) {

		super(centerCriteria);

		this.touchCriteria = touchCriteria;
		this.touchIsAny = touchIsAny;
		this.touchPattern = touchPattern;
	}

	public static RuleCriteriaTouch of(BlockCriteria centerCriteria, BlockCriteria touchCriteria,
			boolean touchIsAny, int touchPattern) {

		return new RuleCriteriaTouch(centerCriteria, touchCriteria, touchIsAny, touchPattern);
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

		// center match try touch
		if (touchIsAny) {

			for (int directionPack = 0; directionPack < 6; directionPack++) {

				if (Geometry.directionPatternGet(touchPattern, directionPack)) {

					BlockPos touchLocation = location
							.relative(Geometry.DirectionFromPack[directionPack]);

					if (touchCriteria.matchIs(level, touchLocation)) {

						return true;
					}
				}
			}

			return false;
		}
		else {

			for (int directionPack = 0; directionPack < 6; directionPack++) {

				if (Geometry.directionPatternGet(touchPattern, directionPack)) {

					BlockPos touchLocation = location
							.relative(Geometry.DirectionFromPack[directionPack]);

					if (!touchCriteria.matchIs(level, touchLocation)) {

						return false;
					}
				}
			}
			
			return true;
		}
	}
}
