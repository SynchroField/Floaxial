package com.synchrofield.floaxial.central.statistics;

import com.synchrofield.floaxial.server.rule.RuleTable;

public class RuleStatisticsTable {

	public final RuleStatistics list[];

	protected RuleStatisticsTable(RuleStatistics list[]) {

		this.list = list;
	}

	public static RuleStatisticsTable of() {

		RuleStatistics[] list = new RuleStatistics[RuleTable.ListSize];

		for (int i = 0; i < RuleTable.ListSize; i++) {

			list[i] = RuleStatistics.of();
		}

		return new RuleStatisticsTable(list);
	}
}
