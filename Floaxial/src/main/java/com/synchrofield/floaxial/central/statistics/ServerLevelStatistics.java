package com.synchrofield.floaxial.central.statistics;

public class ServerLevelStatistics {

	public long tick;
	public ServerDropStatistics drop;
	public RuleStatisticsTable rule;

	protected ServerLevelStatistics() {

	}

	public static ServerLevelStatistics of() {

		ServerLevelStatistics result = new ServerLevelStatistics();
		result.drop = ServerDropStatistics.of();
		result.rule = RuleStatisticsTable.of();
		return result;
	}
}
