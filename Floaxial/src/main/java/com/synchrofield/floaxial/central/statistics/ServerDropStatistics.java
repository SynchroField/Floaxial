package com.synchrofield.floaxial.central.statistics;

import com.synchrofield.floaxial.central.droplet.MaterialTable;

public class ServerDropStatistics {

	// section process during level load
	public int loadSection;
	public int loadSectionFail;
	
	// currently waiting processing
	public int loadSectionUse;

	public int processLevel;
	public int processSection;
	public int processMaterialSection;

	public int networkSendByte;
	public int networkSendPacket;
	
	public int gapQueueSize;
	public float gapAddPerTickMean;
	public float gapProcessPerTickMean;
	
	public final ServerMaterialStatistics material[];

	protected ServerDropStatistics() {

		material = new ServerMaterialStatistics[MaterialTable.Size];

		for (int i = 0; i < MaterialTable.Size; i++) {

			material[i] = ServerMaterialStatistics.of();
		}
	}

	public static ServerDropStatistics of() {

		return new ServerDropStatistics();
	}
}
