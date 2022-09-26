package com.synchrofield.library.terrain;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;

// level geometry
public class Geometry {

	// pack direction
	public static int DirectionPackBitSize = 3;

	public static int DirectionPackNone = 6;
	public static int DirectionPackAll = 7;

	public static int DirectionPatternAllNotDown = 0x3e;

	public static int DirectionToPack(Direction direction) {

		return direction.get3DDataValue();
	}

	public static Direction[] DirectionFromPack = {

			Direction.DOWN,
			Direction.UP,
			Direction.NORTH,
			Direction.SOUTH,
			Direction.WEST,
			Direction.EAST,

			// in case it somehow gets used with special indexes
			Direction.DOWN,
			Direction.DOWN
	};

	public static Direction directionGetRandomXZ(Random random) {

		return Direction.Plane.HORIZONTAL.getRandomDirection(random);
	}

	public static Direction directionGetRandomX(Random random) {

		return (random.nextInt(2) == 0) ? Direction.WEST : Direction.EAST;
	}

	public static Direction directionGetRandomZ(Random random) {

		return (random.nextInt(2) == 0) ? Direction.NORTH : Direction.SOUTH;
	}

	public static int deltaGetTaxi(BlockPos source, BlockPos destination) {

		return source.distManhattan(destination);
	}

	public static BlockPos sectionGetRandomBlockPosition(Random random,
			SectionPos sectionPosition) {

		int blockPositionX = sectionPosition.origin()
				.getX() + random.nextInt(16);
		int blockPositionY = sectionPosition.origin()
				.getY() + random.nextInt(16);
		int blockPositionZ = sectionPosition.origin()
				.getZ() + random.nextInt(16);

		return new BlockPos(blockPositionX, blockPositionY, blockPositionZ);
	}

	public static boolean directionPatternGet(int pattern, int directionPack) {

		return ((1 << directionPack) & pattern) != 0;
	}

	public static int directionPatternSet(int pattern, Direction direction) {

		return directionPatternSet(pattern, DirectionToPack(direction));
	}

	public static int directionPatternSet(int pattern, int directionPack) {

		return pattern | (1 << directionPack);
	}
}
