package com.synchrofield.floaxial.central.network;

import com.synchrofield.floaxial.server.droplet.Droplet;
import com.synchrofield.library.math.MathUtility;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;

// network drop move packed into 32 bit
public class DropNetwork {

	// location 0, 0, 0
	// energy 0
	// direction 0
	// time start 0
	public final static int Default = 0;

	// relative to section
	public final static int LocationIndex = 0;
	public final static int LocationAxisSize = Droplet.LocationAxisSize;
	public final static int LocationSize = LocationAxisSize * 3;
	public final static int LocationMaximum = ((1 << LocationSize) - 1);
	public final static int LocationMask = LocationMaximum << LocationIndex;
	public final static int LocationClear = ~LocationMask;

	// start tick of move, fixed decimal
	// later 6 bit integer 4 bit decimal
	public final static int TimeIndex = LocationIndex + LocationSize;
	public final static int TimeSize = Droplet.TimeSize;
	public final static int TimeMaximum = (1 << TimeSize) - 1;
	public final static int TimeMask = TimeMaximum << TimeIndex;
	public final static int TimeClear = ~TimeMask;

	// energy
	public final static int EnergyIndex = TimeIndex + TimeSize;
	public final static int EnergySize = 3;
	public final static int EnergyMaximum = (1 << EnergySize) - 1;
	public final static int EnergyMask = EnergyMaximum << EnergyIndex;
	public final static int EnergyClear = ~EnergyMask;

	// energy direction
	public final static int DirectionIndex = EnergyIndex + EnergySize;
	public final static int DirectionSize = 3;
	public final static int DirectionMaximum = (1 << DirectionSize) - 1;
	public final static int DirectionMask = DirectionMaximum << DirectionIndex;
	public final static int DirectionClear = ~DirectionMask;

	public final static int SpareSize = 4;

	public static int location(int data) {

		return (data & LocationMask) >>> LocationIndex;
	}

	public static int locationSet(int data, int location) {

		assert MathUtility.msb(location) < location;

		return (data & LocationClear) | (location << LocationIndex);
	}

	// output absolute
	public static BlockPos locationUnpack(int data, SectionPos sectionLocation) {

		return locationFromPack(sectionLocation, location(data));
	}

	// output relative to section
	public static int locationSetUnpack(int data, BlockPos locationUnpack) {

		// masked properly by SectionPos
		return (data & LocationClear) | (locationToPack(locationUnpack) << LocationIndex);
	}

	// output relative to section
	public static int locationToPack(BlockPos locationUnpack) {

		return (int) SectionPos.sectionRelativePos(locationUnpack);
	}

	// output absolute
	public static BlockPos locationFromPack(SectionPos sectionLocation, int location) {

		return sectionLocation.relativeToBlockPos((short) (location));
	}

	public static int time(int data) {

		return (data & TimeMask) >>> TimeIndex;
	}

	public static int timeSet(int data, int time) {

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

	public static int timeGetDeltaStart(int data, long packetCreateTime, long startTime) {

		// silently cap
		int deltaStartTime = (int) (startTime - packetCreateTime) & TimeMaximum;

		return deltaStartTime;
		//		return timeSet(data, deltaStartTime);
	}

	public static int energy(int data) {

		return (data & EnergyMask) >>> EnergyIndex;
	}

	public static int energySet(int data, int energy) {

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

	public static int direction(int data) {

		return (data & DirectionMask) >>> DirectionIndex;
	}

	public static int directionSet(int data, int direction) {

		return (data & DirectionClear) | (direction << DirectionIndex);
	}
}
