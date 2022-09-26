package com.synchrofield.floaxial.server.droplet;

import com.synchrofield.library.math.MathUtility;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;

public class Droplet {

	public static final int LocationIndex = 0;
	public static final int LocationAxisSize = 4;
	public static final int LocationSize = LocationAxisSize * 3;
	public static final int LocationMaximum = ((1 << LocationSize) - 1);
	public static final int LocationMask = LocationMaximum << LocationIndex;
	public static final int LocationClear = (~LocationMask);

	// start of move, fixed decimal
	// only applicable to ghost mode
	public static final int TimeIndex = 16;
	public static final int TimeSize = 10;
	public static final int TimeMaximum = (1 << TimeSize) - 1;
	public static final int TimeMaximumSitu = TimeMaximum << TimeSize;
	public static final int TimeMask = TimeMaximum << TimeIndex;
	public static final int TimeClear = (~TimeMask);

	public static final int TimeDecimalScale = 16;
	public static final int TimeDecimalSize = 4;
	public static final int TimeIntegerMaximum = 64 - 1;

	public static final int EnergyIndex = TimeIndex + TimeSize;
	public static final int EnergySize = 3;
	public static final int EnergyMaximum = (1 << EnergySize) - 1;
	public static final int EnergyMaximumSitu = EnergyMaximum << EnergySize;
	public static final int EnergyMask = EnergyMaximum << EnergyIndex;
	public static final int EnergyClear = (~EnergyMask);

	public static final int DirectionIndex = EnergyIndex + EnergySize;
	public static final int DirectionSize = 3;
	public static final int DirectionMaximum = (1 << DirectionSize) - 1;
	public static final int DirectionMaximumSitu = DirectionMaximum << DirectionSize;
	public static final int DirectionMask = DirectionMaximum << DirectionIndex;
	public static final int DirectionClear = (~DirectionMask);

	// need material unless put in separate lists
	public static final int SpareSize = 0;

	//	location 0, 0, 0
	// energy 0
	// direction 0 down
	// time 0
	public static final int DefaultValue = 0;

	public static int fromShort(short location, short data) {

		return ((int) data << 16) | (int) location;
	}

	public static short toLocation(int data) {

		return (short) (data & 0xffff);
	}

	public static short toData(int data) {

		return (short) (data >>> 16);
	}

	public static int locationGet(int data) {

		return (data & LocationMask) >>> LocationIndex;
	}

	public static int locationSet(int data, int location) {

		assert MathUtility.msb(location) < LocationSize;

		return (data & LocationClear) | (location << LocationIndex);
	}

	// output absolute
	public static BlockPos locationGetUnpack(int data, SectionPos sectionLocation) {

		return locationFromPack(sectionLocation, locationGet(data));
	}

	// output relative to section
	public static int locationSetUnpack(int data, BlockPos locationAbsolute) {

		// masked properly by SectionPos
		return (data & LocationClear) | (locationToPack(locationAbsolute) << LocationIndex);
	}

	// output relative to section
	public static int locationToPack(BlockPos locationAbsolute) {

		return (int) SectionPos.sectionRelativePos(locationAbsolute);
	}

	// output absolute
	public static BlockPos locationFromPack(SectionPos sectionLocation, int locationPack) {

		return sectionLocation.relativeToBlockPos((short) (locationPack));
	}

	public static int timeGet(int data) {

		return (data & TimeMask) >>> TimeIndex;
	}

	public static int timeSet(int data, int time) {

		assert MathUtility.msb(time) < TimeSize;

		return (data & TimeClear) | (time << TimeIndex);
	}

	public static int timeClear(int data) {

		return data & TimeClear;
	}

	public static int timeSetMaximum(int data) {

		return data | TimeMask;
	}

	public static boolean timeIsZero(int data) {

		return (data & TimeMask) == 0;
	}

	public static int timeCap(int time) {

		return time & TimeMaximum;
	}

	public static int energyGet(int data) {

		return (data & EnergyMask) >>> EnergyIndex;
	}

	public static int energySet(int data, int energy) {

		assert MathUtility.msb(energy) < EnergySize;

		return (data & EnergyClear) | (energy << EnergyIndex);
	}

	public static int energyClear(int data) {

		return data & EnergyClear;
	}

	public static int energySetMaximum(int data) {

		return data | EnergyMask;
	}

	public static boolean energyIsZero(int data) {

		return (data & EnergyMask) == 0;
	}

	public static int directionGet(int data) {

		return (data & DirectionMask) >>> DirectionIndex;
	}

	public static int directionSet(int data, int direction) {

		assert MathUtility.msb(direction) < DirectionSize;

		return (data & DirectionClear) | (direction << DirectionIndex);
	}

	public static int directionCap(int direction) {

		return direction & DirectionMaximum;
	}
}
