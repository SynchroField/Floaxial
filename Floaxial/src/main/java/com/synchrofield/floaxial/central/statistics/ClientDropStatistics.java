package com.synchrofield.floaxial.central.statistics;

import com.synchrofield.floaxial.central.droplet.MaterialTable;

public class ClientDropStatistics {

	public int networkReceivePacket;
	public int networkReceiveByte;

	public final ClientMaterialStatistics material[];

	protected ClientDropStatistics() {

		material = new ClientMaterialStatistics[MaterialTable.Size];

		for (int i = 0; i < MaterialTable.Size; i++) {

			material[i] = ClientMaterialStatistics.of();
		}
	}

	public static ClientDropStatistics of() {

		return new ClientDropStatistics();
	}
}
