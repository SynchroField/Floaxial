package com.synchrofield.floaxial.server.rule;

import com.synchrofield.floaxial.central.configure.RuleConfigure;
import com.synchrofield.floaxial.central.configure.RuleConfigureList;
import com.synchrofield.library.configure.ConfigureException;
import com.synchrofield.library.math.MathUtility;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class RuleTable {

	public static final int BitSize = 3;
	public static final int Maximum = (1 << BitSize) - 1;
	public static final int ListSize = Maximum + 1;

	public final Rule[] list;

	protected RuleTable(Rule[] list) {

		assert BitSize > 0;
		assert list.length == ListSize;

		this.list = list;
	}

	public static RuleTable of(RuleConfigureList ruleConfigureList) {

		Rule[] list = new Rule[ListSize];

		for (int i = 0; i < ListSize; i++) {

			RuleConfigure ruleConfigure = ruleConfigureList.list[i];

			RuleSchedule schedule = RuleSchedule.of(ruleConfigure.sectionPerTick,
					ruleConfigure.tickPerLevel, ruleConfigure.locationPerSection);

			RuleCriteria criteria;
			switch (ruleConfigure.criteriaType) {

			default:
			case RuleCriteriaType.Center: {

				BlockCriteria centerCriteria = BlockCriteria.of(ruleConfigure.centerCriteriaType,
						ruleConfigure.centerName);

				criteria = RuleCriteria.of(centerCriteria);

				break;
			}

			case RuleCriteriaType.Touch: {

				BlockCriteria centerCriteria = BlockCriteria.of(ruleConfigure.centerCriteriaType,
						ruleConfigure.centerName);

				BlockCriteria touchCriteria = BlockCriteria.of(ruleConfigure.touchType,
						ruleConfigure.touchName);

				criteria = RuleCriteriaTouch.of(centerCriteria, touchCriteria,
						ruleConfigure.touchIsAny, ruleConfigure.touchDirectionList);

				break;
			}

			case RuleCriteriaType.Row: {

				BlockCriteria centerCriteria = BlockCriteria.of(ruleConfigure.centerCriteriaType,
						ruleConfigure.centerName);

				BlockCriteria touchCriteria = BlockCriteria.of(ruleConfigure.touchType,
						ruleConfigure.touchName);

				criteria = RuleCriteriaRow.of(centerCriteria, touchCriteria,
						ruleConfigure.touchIsAny, ruleConfigure.touchDirectionList,
						ruleConfigure.rowDistance);

				break;
			}

			case RuleCriteriaType.Sample: {

				BlockCriteria centerCriteria = BlockCriteria.of(ruleConfigure.centerCriteriaType,
						ruleConfigure.centerName);

				BlockCriteria touchCriteria = BlockCriteria.of(ruleConfigure.touchType,
						ruleConfigure.touchName);

				criteria = RuleCriteriaSample.of(centerCriteria, touchCriteria,
						ruleConfigure.sampleRadius);

				break;
			}
			}

			RuleProcess process;
			if (ruleConfigure.destinationState1.trim()
					.length() <= 0) {

				// single destination
				process = RuleProcessReplace.of(ruleConfigure.destinationState0,
						ruleConfigure.destinationOffset);
			}
			else {

				// multiple
				process = RuleProcessReplace2.of(ruleConfigure.destinationState0,
						ruleConfigure.destinationWeight0, ruleConfigure.destinationState1,
						ruleConfigure.destinationWeight1, ruleConfigure.destinationOffset);
			}

			list[i] = Rule.of(schedule, criteria, process);
			list[i].enableIs = ruleConfigure.enableIs;
		}

		RuleTable table = new RuleTable(list);

		table.compile();

		return table;
	}

	public void compile() throws ConfigureException {

		for (int i = 0; i < ListSize; i++) {

			if (list[i].enableIs) {

				list[i].compile();
			}
		}
	}

	public static boolean indexCheck(int ruleIndex) {

		return MathUtility.msb(ruleIndex) < BitSize;
	}

	// return rule index or -1
	public int matchTry(ServerLevel level, BlockPos location) {

		for (int i = 0; i < ListSize; i++) {

			if (list[i].enableIs) {

				if (list[i].criteria.matchIs(level, location)) {

					return i;
				}
			}
		}

		return -1;
	}

	// return rule index or -1
	public int centerMatchTry(ServerLevel level, BlockPos location) {

		for (int i = 0; i < ListSize; i++) {

			if (list[i].enableIs) {

				if (list[i].criteria.centerMatchIs(level, location)) {

					return i;
				}
			}
		}

		return -1;
	}
}
