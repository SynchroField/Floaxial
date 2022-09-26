package com.synchrofield.floaxial.server.rule;

import com.synchrofield.library.configure.ConfigureException;
import com.synchrofield.library.terrain.Geometry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

public class RuleCriteriaRow extends RuleCriteria {

	// touch
	public BlockCriteria touchCriteria;

	public final boolean touchIsAny;
	public final int directionList;
	public final int rowDistance;

	protected RuleCriteriaRow(BlockCriteria centerCriteria, BlockCriteria touchCriteria,
			boolean touchIsAny, int directionList, int rowDistance) {

		super(centerCriteria);

		this.touchCriteria = touchCriteria;
		this.touchIsAny = touchIsAny;
		this.directionList = directionList;
		this.rowDistance = rowDistance;
	}

	public static RuleCriteriaRow of(BlockCriteria centerCriteria, BlockCriteria touchCriteria,
			boolean touchIsAny, int directionList, int rowDistance) {

		return new RuleCriteriaRow(centerCriteria, touchCriteria, touchIsAny, directionList,
				rowDistance);
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

		// center match try row
		if (touchIsAny) {

			for (int directionPack = 0; directionPack < 6; directionPack++) {

				if (Geometry.directionPatternGet(directionList, directionPack)) {

					Direction direction = Geometry.DirectionFromPack[directionPack];

					if (rowIsMatch(level, location, direction, rowDistance)) {

						return true;
					}
				}
			}
			return false;
		}
		else {

			for (int directionPack = 0; directionPack < 6; directionPack++) {

				if (Geometry.directionPatternGet(directionList, directionPack)) {

					Direction direction = Geometry.DirectionFromPack[directionPack];

					if (!rowIsMatch(level, location, direction, rowDistance)) {

						return false;
					}
				}
			}

			return true;
		}

	}

	public boolean rowIsMatch(ServerLevel level, BlockPos location, Direction direction,
			int distance) {

		assert distance > 0;

		for (int i = 0; i < distance; i++) {

			BlockPos candidateLocation = location.relative(direction, i + 1);

			if (!touchCriteria.matchIs(level, candidateLocation)) {

				return false;
			}
		}

		return true;
	}
}
