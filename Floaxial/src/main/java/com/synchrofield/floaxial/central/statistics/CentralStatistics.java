package com.synchrofield.floaxial.central.statistics;

public class CentralStatistics {

	public ServerStatistics server;
	public ClientStatistics client;

	protected CentralStatistics() {

	}

	public static CentralStatistics of() {

		CentralStatistics result = new CentralStatistics();
		result.server = ServerStatistics.of();
		result.client = ClientStatistics.of();
		return result;
	}
}
