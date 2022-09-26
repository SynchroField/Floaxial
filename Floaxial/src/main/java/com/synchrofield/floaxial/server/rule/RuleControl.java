package com.synchrofield.floaxial.server.rule;

import com.mojang.datafixers.util.Pair;
import com.synchrofield.floaxial.central.configure.RuleConfigure;
import com.synchrofield.floaxial.central.statistics.IntegerHistory;
import com.synchrofield.floaxial.central.statistics.RuleStatistics;
import com.synchrofield.floaxial.server.MaterialPlaceReceive;
import com.synchrofield.library.math.MathUtility;
import com.synchrofield.library.terrain.Geometry;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;

public class RuleControl {

	public static final int SamplePerTickHistorySize = 100;

	public final int ruleIndex;
	public final Rule rule;
	public final RuleLevel ruleLevel;
	public final MaterialPlaceReceive materialPlaceReceive;

	public int dummyTick;

	public RuleStatistics statistics;
	public IntegerHistory samplePerTickHistory;

	protected RuleControl(int ruleIndex, Rule rule, RuleLevel ruleLevel,
			MaterialPlaceReceive materialPlaceReceive) {

		this.ruleIndex = ruleIndex;
		this.rule = rule;
		this.ruleLevel = ruleLevel;

		this.materialPlaceReceive = materialPlaceReceive;

		this.dummyTick = 0;

		this.statistics = RuleStatistics.of();
		this.samplePerTickHistory = IntegerHistory.of(SamplePerTickHistorySize);
	}

	public static RuleControl of(int ruleIndex, Rule rule, RuleLevel ruleLevel,
			MaterialPlaceReceive materialPlaceReceive) {

		return new RuleControl(ruleIndex, rule, ruleLevel, materialPlaceReceive);
	}

	public void onTick(ServerLevel level) {

		if (!rule.enableIs) {

			return;
		}

		boolean EnableIs = true;

		if (!EnableIs) {

			return;
		}

		float processChance = levelDeriveProcessChance();
		if (level.random.nextFloat() > processChance) {

			// processing too fast
			return;
		}

		statistics.sectionPerTick = rule.schedule.sectionPerTick;

		levelTick(level, rule.schedule.sectionPerTick);
	}

	public float levelDeriveProcessChance() {

		int sectionListSize = ruleLevel.size();
		if (sectionListSize <= 0) {

			// no section
			return 0.0f;
		}

		// number of tick to process whole level
		float naiveLevelTime = (float) sectionListSize / (float) rule.schedule.sectionPerTick;

		if (naiveLevelTime < rule.schedule.tickPerLevel) {

			// level would be processed too quickly
			float speedFactor = naiveLevelTime / rule.schedule.tickPerLevel;

			return speedFactor;
		}
		else {

			return 1.0f;
		}
	}

	public void levelTick(ServerLevel level, int sectionPerTick) {

		// for now tick all section once, ignore config
		Object[] sectionArray = ruleLevel.sectionMap.long2ObjectEntrySet()
				.toArray();

		if (sectionArray.length <= 0) {

			// no section
			return;
		}

		for (int i = 0; i < sectionPerTick; i++) {

			int sectionIndex = level.random.nextInt(sectionArray.length);

			@SuppressWarnings("unchecked")
			Long2ObjectMap.Entry<RuleSection> entry = (Long2ObjectMap.Entry<RuleSection>) sectionArray[sectionIndex];

			RuleSection ruleSection = entry.getValue();
			if (ruleSection != null) {

				SectionPos sectionLocation = SectionPos.of(entry.getLongKey());

				sectionProcess(level, sectionLocation);
			}
		}

		// prepare next history tick
		samplePerTickHistory.append(0);
	}

	public void sectionProcess(ServerLevel level, SectionPos location) {

		for (int sampleIndex = 0; sampleIndex < rule.schedule.blockPerSection; sampleIndex++) {

			sectionSample(level, location);
		}
	}

	public void sectionSample(ServerLevel level, SectionPos location) {

		BlockPos blockLocation = Geometry.sectionGetRandomBlockPosition(level.random, location);

		blockProcess(level, location, blockLocation);
	}

	public void blockProcess(ServerLevel level, SectionPos sectionLocation, BlockPos location) {

		if (rule.criteria.matchIs(level, location)) {

			Pair<BlockPos, Integer> locationAndOutputIndex = rule.process.run(level, location);
			if (locationAndOutputIndex != null) {

				int outputIndex = locationAndOutputIndex.getSecond();

				materialPlaceReceive.onRulePlaceMaterial(locationAndOutputIndex.getFirst(),
						ruleIndex, outputIndex);

				samplePerTickHistory.increment();
			}
		}
	}

	public void onMaterialPlace(BlockPos location) {

		if (!rule.enableIs) {

			return;
		}

		// create section if not already
		ruleLevel.sectionCreateByBlock(location);
	}

	public RuleStatistics statisticsDerive() {

		statistics.sectionListSize = ruleLevel.size();
		statistics.samplePerTickMean = samplePerTickHistory.mean();

		return statistics;
	}

	public int sectionPerTickCap(int sectionPerTick) {

		return MathUtility.cap(sectionPerTick, RuleConfigure.SectionPerTickMinimum,
				RuleConfigure.SectionPerTickMaximum);
	}

	// assume section contains this material
	public void onSectionLoadCenter(SectionPos sectionLocation) {

		if (!rule.enableIs) {

			return;
		}

		ruleLevel.sectionCreate(sectionLocation);
	}
}
