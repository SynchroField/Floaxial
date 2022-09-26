package com.synchrofield.floaxial.server.rule;

import com.synchrofield.floaxial.central.statistics.RuleStatisticsTable;
import com.synchrofield.floaxial.server.MaterialPlaceReceive;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;

public class RuleControlTable {

	public final RuleControl[] list;

	public final RuleTable ruleTable;

	protected RuleControlTable(RuleControl[] list, RuleTable ruleTable) {

		assert list.length == RuleTable.ListSize;

		this.list = list;

		this.ruleTable = ruleTable;
	}

	public static RuleControlTable of(RuleTable ruleTable, RuleLevelTable levelTable,
			MaterialPlaceReceive materialPlaceReceive) {

		RuleControl[] list = new RuleControl[RuleTable.ListSize];

		for (int i = 0; i < RuleTable.ListSize; i++) {

			Rule rule = ruleTable.list[i];

			RuleLevel ruleLevel = levelTable.list[i];

			list[i] = RuleControl.of(i, rule, ruleLevel, materialPlaceReceive);
		}

		return new RuleControlTable(list, ruleTable);
	}

	public void onTick(ServerLevel level) {

		for (int i = 0; i < RuleTable.ListSize; i++) {

			if (list[i].rule.enableIs) {

				list[i].onTick(level);
			}
		}
	}

	public void onBlockPlace(ServerLevel level, BlockPos location, BlockState state) {

		int ruleIndex = ruleTable.centerMatchTry(level, location);
		if (ruleIndex != -1) {

			// rule block was placed, track the section
			list[ruleIndex].onMaterialPlace(location);
		}
	}

	public RuleStatisticsTable statisticsDerive() {

		RuleStatisticsTable result = RuleStatisticsTable.of();

		for (int i = 0; i < RuleTable.ListSize; i++) {

			result.list[i] = list[i].statisticsDerive();
		}

		return result;
	}

	public void onSectionLoad(SectionPos location, LevelChunkSection section) {

		PalettedContainer<BlockState> palette = section.getStates();

		for (int ruleIndex = 0; ruleIndex < RuleTable.ListSize; ruleIndex++) {

			if (list[ruleIndex].rule.enableIs) {

				// normal source block
				if (palette.maybeHas(
						ruleTable.list[ruleIndex].criteria.centerCriteria.equalPredicate)) {

					// add to material
					list[ruleIndex].onSectionLoadCenter(location);
				}
			}
		}
	}

}
