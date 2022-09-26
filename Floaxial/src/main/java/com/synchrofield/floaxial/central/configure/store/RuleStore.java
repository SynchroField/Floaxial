package com.synchrofield.floaxial.central.configure.store;

import java.util.Optional;

import com.synchrofield.floaxial.central.configure.RuleConfigure;
import com.synchrofield.floaxial.server.rule.BlockCriteriaType;
import com.synchrofield.floaxial.server.rule.RuleCriteriaType;
import com.synchrofield.floaxial.server.rule.RuleTable;
import com.synchrofield.library.configure.ConfigureStore;
import com.synchrofield.library.math.MathUtility;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

// rule configuration storage
public class RuleStore extends ConfigureStore {

	// append index
	public static final String FilenameBase = "rule";

	public final int ruleIndex;

	public final ConfigValue<Boolean> enableIs;
	public final ConfigValue<Integer> sectionPerTick;
	public final ConfigValue<Integer> tickPerLevel;
	public final ConfigValue<Integer> locationPerSection;
	public final ConfigValue<String> criteriaType;
	public final ConfigValue<String> centerType;
	public final ConfigValue<String> centerName;
	public final ConfigValue<String> touchType;
	public final ConfigValue<String> touchName;
	public final ConfigValue<Boolean> touchIsAny;
	public final ConfigValue<Integer> touchDirectionList;
	public final ConfigValue<Integer> rowDistance;
	public final ConfigValue<Integer> sampleRadius;
	public final ConfigValue<String> destinationState0;
	public final ConfigValue<Integer> destinationWeight0;
	public final ConfigValue<String> destinationState1;
	public final ConfigValue<Integer> destinationWeight1;
	public final ConfigValue<Integer> destinationOffset;

	protected RuleStore(Optional<String> folder, String filename, ForgeConfigSpec.Builder builder,
			int ruleIndex) {

		super(folder, filename, builder);

		assert MathUtility.rangeCheck(ruleIndex, RuleTable.ListSize);

		this.ruleIndex = ruleIndex;

		this.builder.push("Rule");

		this.enableIs = propertyCreateBoolean("EnableIs",
				RuleConfigure.DefaultValue[ruleIndex].enableIs, "Enable processing of this rule.");

		this.sectionPerTick = propertyCreateInteger("SectionPerTick",
				RuleConfigure.SectionPerTickMinimum, RuleConfigure.SectionPerTickMaximum,
				RuleConfigure.DefaultValue[ruleIndex].sectionPerTick,
				"Number of chunk section (16 size cube) to sample each tick."
						+ "Each random block sample is checked against this rule criteria and processed if there's a match.");

		this.tickPerLevel = propertyCreateInteger("TickPerLevel", RuleConfigure.TickPerLevelMinimum,
				RuleConfigure.TickPerLevelMaximum,
				RuleConfigure.DefaultValue[ruleIndex].tickPerLevel,
				"Minimum time to process all sections for this rule."
						+ " Provides a speed limit on processing when only a few chunks are loaded."
						+ " e.g. A value of 100 tick with 2 section loaded means the rule will be ignored for 98 tick.");

		this.locationPerSection = propertyCreateInteger("LocationPerSection",
				RuleConfigure.LocationPerSectionMinimum, RuleConfigure.LocationPerSectionMaximum,
				RuleConfigure.DefaultValue[ruleIndex].locationPerSection,
				"Block locations to sample within each section that is processed.");

		this.criteriaType = propertyCreateString("CriteriaType",
				RuleCriteriaType.toString(RuleConfigure.DefaultValue[ruleIndex].criteriaType),
				"Rule criteria type."
						+ " [Center] matches a single block. [Touch] checks all sides as well."
						+ " [Row] scans a straight line in the given direction. [Sample] checks a nearby block.");

		this.centerType = propertyCreateString("CenterCriteriaType",
				BlockCriteriaType
						.toString(RuleConfigure.DefaultValue[ruleIndex].centerCriteriaType),
				"Type of criteria for matching the rule."
						+ "[BlockState] exact match of block state, [BlockStateSubset] subset match"
						+ ", [Block] block name, [Material] block material"
						+ ", [FluidIs] block is fluid, [SolidIs] regular block"
						+ ", [HeavyIs] excludes leaf etc., [ReplaceableIs] blocks that fluid can destroy.");

		this.centerName = propertyCreateBlockState("CenterName",
				RuleConfigure.DefaultValue[ruleIndex].centerName,
				"Criteria block state for this rule in the form \"mod:block[property=value]\"."
						+ " Otherwise block or material name.");

		this.touchType = propertyCreateString("TouchType",
				BlockCriteriaType.toString(RuleConfigure.DefaultValue[ruleIndex].touchType),
				"Type of criteria for touch criteria."
						+ " [BlockState], [BlockStateSubset], [Block], [Material], [FluidIs], [SolidIs], [HeavyIs] or [ReplaceableIs].");

		this.touchName = propertyCreateBlockState("TouchName",
				RuleConfigure.DefaultValue[ruleIndex].touchName,
				"Touch block state for this rule in the form \"mod:block[property=value]\".");

		this.touchIsAny = propertyCreateBoolean("TouchIsAny",
				RuleConfigure.DefaultValue[ruleIndex].enableIs,
				"Only one touch direction needs to match rather than all given directions.");

		this.touchDirectionList = propertyCreateInteger("TouchDirectionList",
				RuleConfigure.TouchDirectionListMinimum, RuleConfigure.TouchDirectionListMaximum,
				RuleConfigure.DefaultValue[ruleIndex].touchDirectionList,
				"Bit field for the 6 directions.  Add each bit value together to get the final value."
						+ " Down [1], Up [2], North [4], South [8], West [16], East [32].");

		this.rowDistance = propertyCreateInteger("RowDistance", RuleConfigure.RowDistanceMinimum,
				RuleConfigure.RowDistanceMaximum, RuleConfigure.DefaultValue[ruleIndex].rowDistance,
				"Distance of row to scan.  Touch direction is used to specify the side.");

		this.sampleRadius = propertyCreateInteger("SampleRadius", RuleConfigure.SampleRadiusMinimum,
				RuleConfigure.SampleRadiusMaximum,
				RuleConfigure.DefaultValue[ruleIndex].sampleRadius,
				"Distance to sample for criteria touch block.");

		this.destinationState0 = propertyCreateBlockState("DestinationState0",
				RuleConfigure.DefaultValue[ruleIndex].destinationState0,
				"Replacement block state for this rule in the form \"mod:block[property=value]\".");

		this.destinationWeight0 = propertyCreateInteger("DestinationWeight0",
				RuleConfigure.DestinationWeightMinimum, RuleConfigure.DestinationWeightMaximum,
				RuleConfigure.DefaultValue[ruleIndex].destinationWeight0,
				"Chance of placing this block state relative to any others that are specified."
						+ " Chance = Weight / TotalWeight .  Treated as [1] for rules with 1 destination.");

		this.destinationState1 = propertyCreateBlockState("DestinationState1",
				RuleConfigure.DefaultValue[ruleIndex].destinationState1,
				"Add a second choice for destination.  [minecraft:air] can be used to remove the block.");

		this.destinationWeight1 = propertyCreateInteger("DestinationWeight1",
				RuleConfigure.DestinationWeightMinimum, RuleConfigure.DestinationWeightMaximum,
				RuleConfigure.DefaultValue[ruleIndex].destinationWeight1,
				" Chance = Weight / TotalWeight .");

		this.destinationOffset = propertyCreateInteger("DestinationOffset",
				RuleConfigure.DestinationOffsetMinimum, RuleConfigure.DestinationOffsetMaximum,
				RuleConfigure.DefaultValue[ruleIndex].destinationOffset,
				"Places the block in the given direction rather than replacing center."
						+ " Down [0], Up [1], North [2], South [3], West [4], East [5], None [6].");

		this.builder.pop();

		this.spec = this.builder.build();
	}

	public static RuleStore of(Optional<String> folder, String filename, int materialIndex) {

		return new RuleStore(folder, filename, builderCreate(), materialIndex);
	}
}
