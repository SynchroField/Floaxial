package com.synchrofield.floaxial.server.rule;

import com.synchrofield.floaxial.central.droplet.MaterialTable;

import net.minecraft.world.level.block.state.BlockState;

// Map rule to drop material table index.  Used to notify rule system if a drop
// moves into a new section etc..
public class RuleMaterialMap {

	// 2 rules might have same criteria block so have to use bit field
	public final int[] ruleOutput0ToMaterial;
	public final int[] ruleOutput0ToRule;
	public final int[] ruleOutput1ToMaterial;
	public final int[] ruleOutput1ToRule;
	public final int[] materialToRuleInput;

	protected RuleMaterialMap(int[] ruleOutput0ToMaterial, int[] ruleOutput0ToRule,
			int[] ruleOutput1ToMaterial, int[] ruleOutput1ToRule, int[] materialToRuleInput) {

		this.ruleOutput0ToMaterial = ruleOutput0ToMaterial;
		this.ruleOutput0ToRule = ruleOutput0ToRule;
		this.ruleOutput1ToMaterial = ruleOutput1ToMaterial;
		this.ruleOutput1ToRule = ruleOutput1ToRule;
		this.materialToRuleInput = materialToRuleInput;
	}

	public static RuleMaterialMap of(RuleTable ruleTable, MaterialTable materialTable) {

		int[] ruleOutput0ToMaterial = new int[RuleTable.ListSize];
		int[] ruleOutput0ToRule = new int[RuleTable.ListSize];
		int[] ruleOutput1ToMaterial = new int[RuleTable.ListSize];
		int[] ruleOutput1ToRule = new int[RuleTable.ListSize];

		toMaterialDerive(ruleTable, materialTable, ruleOutput0ToMaterial, ruleOutput0ToRule,
				ruleOutput1ToMaterial, ruleOutput1ToRule);

		int[] materialToRuleInput = new int[MaterialTable.Size];
		toRuleDerive(materialTable, ruleTable, materialToRuleInput);

		return new RuleMaterialMap(ruleOutput0ToMaterial, ruleOutput0ToRule, ruleOutput1ToMaterial,
				ruleOutput1ToRule, materialToRuleInput);
	}

	public static void toMaterialDerive(RuleTable ruleTable, MaterialTable materialTable,
			int[] ruleOutput0ToMaterial, int[] ruleOutput0ToRule, int[] ruleOutput1ToMaterial,
			int[] ruleOutput1ToRule) {

		for (int ruleIndex = 0; ruleIndex < RuleTable.ListSize; ruleIndex++) {

			// to material
			ruleOutput0ToMaterial[ruleIndex] = -1;
			ruleOutput1ToMaterial[ruleIndex] = -1;

			if (!ruleTable.list[ruleIndex].enableIs) {

				continue;
			}

			for (int materialIndex = 0; materialIndex < MaterialTable.Size; materialIndex++) {

				// output0
				if ((ruleTable.list[ruleIndex].process
						.output0State() == materialTable.list[materialIndex].dropletState)) {

					// same block state
					ruleOutput0ToMaterial[ruleIndex] = materialIndex;
				}

				// output1
				if ((ruleTable.list[ruleIndex].process
						.output1State() == materialTable.list[materialIndex].dropletState)) {

					// same block state
					ruleOutput1ToMaterial[ruleIndex] = materialIndex;
				}
			}

			// to rule input
			ruleOutput0ToRule[ruleIndex] = 0;
			ruleOutput1ToRule[ruleIndex] = 0;

			for (int destinationRuleIndex = 0; destinationRuleIndex < RuleTable.ListSize; destinationRuleIndex++) {

				if (ruleIndex == destinationRuleIndex) {

					// link to self
					continue;
				}

				if (!ruleTable.list[destinationRuleIndex].enableIs) {

					continue;
				}

				// output0
				BlockState output0State = ruleTable.list[ruleIndex].process.output0State();
				if (ruleTable.list[destinationRuleIndex].criteria.centerCriteria
						.matchIs(output0State)) {

					ruleOutput0ToRule[ruleIndex] |= (1 << destinationRuleIndex);
				}

				// output1
				BlockState output1State = ruleTable.list[ruleIndex].process.output1State();
				if (output1State != null) {

					if (ruleTable.list[destinationRuleIndex].criteria.centerCriteria
							.matchIs(output1State)) {

						ruleOutput1ToRule[ruleIndex] |= (1 << destinationRuleIndex);
					}
				}
			}
		}
	}

	public static void toRuleDerive(MaterialTable materialTable, RuleTable ruleTable,
			int[] materialToRuleInput) {

		for (int materialIndex = 0; materialIndex < MaterialTable.Size; materialIndex++) {

			materialToRuleInput[materialIndex] = 0;

			for (int destinationRuleIndex = 0; destinationRuleIndex < RuleTable.ListSize; destinationRuleIndex++) {

				if (!ruleTable.list[destinationRuleIndex].enableIs) {

					continue;
				}

				BlockState materialOutputState = materialTable.list[materialIndex].dropletState;

				if (ruleTable.list[destinationRuleIndex].criteria.centerCriteria
						.matchIs(materialOutputState)) {

					materialToRuleInput[materialIndex] |= (1 << destinationRuleIndex);
				}
			}
		}
	}
}
