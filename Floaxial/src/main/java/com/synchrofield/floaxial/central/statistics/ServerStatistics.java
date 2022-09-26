package com.synchrofield.floaxial.central.statistics;

public class ServerStatistics {

	public ServerLevelStatistics level;

	protected ServerStatistics() {

	}

	public static ServerStatistics of() {

		ServerStatistics result = new ServerStatistics();
		result.level = ServerLevelStatistics.of();
		return result;
	}
}
