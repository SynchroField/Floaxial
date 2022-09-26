package com.synchrofield.floaxial.central.statistics;

public class ClientStatistics {

	public long tick;
	public ClientDropStatistics drop;

	protected ClientStatistics() {

	}

	public static ClientStatistics of() {

		ClientStatistics result = new ClientStatistics();
		result.tick = 0;
		result.drop = ClientDropStatistics.of();
		return result;
	}
}
