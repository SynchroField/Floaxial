package com.synchrofield.floaxial.server.rule;

public class RuleLevelTable {

	public final RuleLevel[] list;

	protected RuleLevelTable(RuleLevel[] list) {

		assert list.length == RuleTable.ListSize;

		this.list = list;
	}

	public static RuleLevelTable of() {

		RuleLevel[] list = new RuleLevel[RuleTable.ListSize];

		for (int i = 0; i < RuleTable.ListSize; i++) {

			list[i] = RuleLevel.of();
		}

		return new RuleLevelTable(list);
	}
}
