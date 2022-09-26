package com.synchrofield.floaxial.central.configure;

import com.synchrofield.floaxial.server.rule.BlockCriteriaType;
import com.synchrofield.floaxial.server.rule.RuleCriteriaType;
import com.synchrofield.library.terrain.Geometry;

import net.minecraft.core.Direction;

public class RuleConfigure {

	public static final boolean EnableIsDefault = false;

	public static final int SectionPerTickMinimum = 1;
	public static final int SectionPerTickMaximum = 1024 * 64;
	public static final int SectionPerTickDefault = SectionPerTickMinimum;

	public static final int TickPerLevelMinimum = 1;
	public static final int TickPerLevelMaximum = 1024 * 64;
	public static final int TickPerLevelDefault = TickPerLevelMinimum;

	public static final int LocationPerSectionMinimum = 1;
	public static final int LocationPerSectionMaximum = 1024 * 4;
	public static final int LocationPerSectionDefault = LocationPerSectionMinimum;

	public static final int CriteriaTypeDefault = RuleCriteriaType.Center;

	public static final int CenterCriteriaTypeDefault = BlockCriteriaType.BlockState;
	public static final String CenterNameDefault = "";

	public static final int TouchTypeDefault = BlockCriteriaType.BlockState;
	public static final String TouchNameDefault = "";
	public static final boolean TouchIsAnyDefault = true;

	public static final int TouchDirectionListMinimum = 1;
	public static final int TouchDirectionListMaximum = (1 << 6) - 1;
	public static final int TouchDirectionListDefault = TouchDirectionListMinimum;

	public static final int RowDistanceMinimum = 1;
	public static final int RowDistanceMaximum = 16;
	public static final int RowDistanceDefault = RowDistanceMinimum;

	public static final int SampleRadiusMinimum = 1;
	public static final int SampleRadiusMaximum = 16;
	public static final int SampleRadiusDefault = SampleRadiusMinimum;

	public static final String DestinationNameDefault = "";

	public static final int DestinationWeightMinimum = 1;
	public static final int DestinationWeightMaximum = 1024 * 64;
	public static final int DestinationWeightDefault = DestinationWeightMinimum;

	public static final int DestinationOffsetMinimum = 0;
	public static final int DestinationOffsetMaximum = 6;
	public static final int DestinationOffsetDefault = Geometry.DirectionPackNone;

	public static final RuleConfigure Default0 = RuleConfigure.of()
			.withEnableIs(true)
			.withSectionPerTick(1)
			.withTickPerLevel(1)
			.withLocationPerSection(8)
			.withCriteriaType(RuleCriteriaType.Center)
			.withCenterCriteriaType(BlockCriteriaType.BlockStateSubset)
			.withCenterName("floaxial:vanilla_jungle_leaf[persistent=true]")
			.withDestinationState0("minecraft:dirt")
			.withDestinationWeight0(1)
			.withDestinationState1("minecraft:air")
			.withDestinationWeight1(3);

	public static final RuleConfigure Default1 = RuleConfigure.of()
			.withEnableIs(true)
			.withSectionPerTick(1)
			.withTickPerLevel(20)
			.withLocationPerSection(1)
			.withCriteriaType(RuleCriteriaType.Touch)
			.withCenterCriteriaType(BlockCriteriaType.BlockState)
			.withCenterName("minecraft:grass_block")
			.withTouchType(BlockCriteriaType.Block)
			.withTouchName("minecraft:air")
			.withTouchDirectionList(Geometry.directionPatternSet(0, Direction.UP))
			.withDestinationState0("floaxial:vanilla_jungle_sapling")
			.withDestinationOffset(Geometry.DirectionToPack(Direction.UP));

	public static final RuleConfigure Default2 = RuleConfigure.of()
			.withEnableIs(true)
			.withSectionPerTick(1)
			.withTickPerLevel(1)
			.withLocationPerSection(1)
			.withCriteriaType(RuleCriteriaType.Center)
			.withCenterCriteriaType(BlockCriteriaType.BlockStateSubset)
			.withCenterName("minecraft:jungle_log")
			.withDestinationState0("minecraft:air")
			.withDestinationWeight0(127)
			.withDestinationState1("minecraft:glowstone")
			.withDestinationWeight1(1);

	public static final RuleConfigure Default3 = RuleConfigure.of()
			.withEnableIs(true)
			.withSectionPerTick(1)
			.withTickPerLevel(10)
			.withLocationPerSection(1)
			.withCriteriaType(RuleCriteriaType.Sample)
			.withCenterCriteriaType(BlockCriteriaType.Block)
			.withCenterName("minecraft:dirt")
			.withTouchType(BlockCriteriaType.Block)
			.withTouchName("floaxial:salt_water")
			.withSampleRadius(2)
			.withDestinationState0("floaxial:vanilla_sand");

	public static final RuleConfigure Default4 = RuleConfigure.of()
			.withEnableIs(true)
			.withSectionPerTick(1)
			.withTickPerLevel(10)
			.withLocationPerSection(1)
			.withCriteriaType(RuleCriteriaType.Sample)
			.withCenterCriteriaType(BlockCriteriaType.Block)
			.withCenterName("minecraft:grass_block")
			.withTouchType(BlockCriteriaType.Block)
			.withTouchName("floaxial:salt_water")
			.withSampleRadius(4)
			.withDestinationState0("minecraft:dirt");

	public static final RuleConfigure Default5 = RuleConfigure.of()
			.withEnableIs(true)
			.withSectionPerTick(1)
			.withTickPerLevel(60)
			.withLocationPerSection(1)
			.withCriteriaType(RuleCriteriaType.Row)
			.withCenterCriteriaType(BlockCriteriaType.BlockState)
			.withCenterName("minecraft:dirt")
			.withTouchType(BlockCriteriaType.HeavyIs)
			.withTouchIsAny(false)
			.withTouchDirectionList(Geometry.DirectionPatternAllNotDown)
			.withRowDistance(4)
			.withDestinationState0("floaxial:vanilla_gravel");

	public static final RuleConfigure Default6 = RuleConfigure.of()
			.withEnableIs(true)
			.withSectionPerTick(1)
			.withTickPerLevel(60)
			.withLocationPerSection(1)
			.withCriteriaType(RuleCriteriaType.Row)
			.withCenterCriteriaType(BlockCriteriaType.BlockState)
			.withCenterName("floaxial:vanilla_gravel")
			.withTouchType(BlockCriteriaType.HeavyIs)
			.withTouchIsAny(false)
			.withTouchDirectionList(Geometry.DirectionPatternAllNotDown)
			.withRowDistance(8)
			.withDestinationState0("minecraft:stone");

	public static final RuleConfigure Default7 = RuleConfigure.of()
			.withEnableIs(false);

	public static final RuleConfigure[] DefaultValue = {

			Default0, Default1, Default2, Default3, Default4, Default5, Default6, Default7,
	};

	public final boolean enableIs;
	public final int sectionPerTick;
	public final int tickPerLevel;
	public final int locationPerSection;
	public final int criteriaType;
	public final int centerCriteriaType;
	public final String centerName;
	public final int touchType;
	public final String touchName;
	public final boolean touchIsAny;
	public final int touchDirectionList;
	public final int rowDistance;
	public final int sampleRadius;
	public final String destinationState0;
	public final int destinationWeight0;
	public final String destinationState1;
	public final int destinationWeight1;
	public final int destinationOffset;

	protected RuleConfigure(boolean enableIs, int sectionPerTick, int tickPerLevel,
			int locationPerSection, int criteriaType, int centerCriteriaType, String centerName,
			int touchType, String touchName, boolean touchIsAny, int touchDirectionList,
			int rowDistance, int sampleRadius, String destinationState0, int destinationWeight0,
			String destinationState1, int destinationWeight1, int destinationOffset) {

		this.enableIs = enableIs;
		this.sectionPerTick = sectionPerTick;
		this.tickPerLevel = tickPerLevel;
		this.locationPerSection = locationPerSection;

		this.criteriaType = criteriaType;
		this.centerCriteriaType = centerCriteriaType;
		this.centerName = centerName;

		this.touchType = touchType;
		this.touchName = touchName;
		this.touchIsAny = touchIsAny;
		this.touchDirectionList = touchDirectionList;

		this.rowDistance = rowDistance;
		this.sampleRadius = sampleRadius;

		this.destinationState0 = destinationState0;
		this.destinationWeight0 = destinationWeight0;
		this.destinationState1 = destinationState1;
		this.destinationWeight1 = destinationWeight1;
		this.destinationOffset = destinationOffset;
	}

	public static RuleConfigure of() {

		return new RuleConfigure(EnableIsDefault, SectionPerTickDefault, TickPerLevelDefault,
				LocationPerSectionDefault, CriteriaTypeDefault, CenterCriteriaTypeDefault,
				CenterNameDefault, TouchTypeDefault, TouchNameDefault, TouchIsAnyDefault,
				TouchDirectionListDefault, RowDistanceDefault, SampleRadiusDefault,
				DestinationNameDefault, DestinationWeightDefault, DestinationNameDefault,
				DestinationWeightDefault, DestinationOffsetDefault);
	}

	public RuleConfigure withEnableIs(boolean enableIs) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withSectionPerTick(int sectionPerTick) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withTickPerLevel(int tickPerLevel) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withLocationPerSection(int locationPerSection) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withCriteriaType(int criteriaType) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withCenterCriteriaType(int centerCriteriaType) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withCenterName(String centerName) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withTouchType(int touchType) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withTouchName(String touchName) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withTouchIsAny(boolean touchIsAny) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withTouchDirectionList(int touchDirectionList) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withRowDistance(int rowDistance) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withSampleRadius(int sampleRadius) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withDestinationState0(String destinationState0) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withDestinationWeight0(int destinationWeight0) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withDestinationState1(String destinationState1) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withDestinationWeight1(int destinationWeight1) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}

	public RuleConfigure withDestinationOffset(int destinationOffset) {

		return new RuleConfigure(enableIs, sectionPerTick, tickPerLevel, locationPerSection,
				criteriaType, centerCriteriaType, centerName, touchType, touchName, touchIsAny,
				touchDirectionList, rowDistance, sampleRadius, destinationState0,
				destinationWeight0, destinationState1, destinationWeight1, destinationOffset);
	}
}
