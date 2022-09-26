package com.synchrofield.floaxial.central.statistics;

public class ServerMaterialStatistics {

	// load block process
	public int loadProcessDrop;

	public int processSection;
	public int processGhost;
	public int processDrop;

	// ghost exist in level
	public int levelGhost;

	public float ghostProcessPerTickMean;
	public float dropletProcessPerTickMean;
	public float movePerTickMean;

	// drop exist in level
	public int levelDrop;

	public int networkSendByte;

	public int networkSendPacket;
	
	public int networkSendSection;

	// move send to client
	public int networkSendDrop;

	// unable to send due to full packet etc.
	public int networkSendDropFail;
	
	// memory use in dword
	// include any empty allocation
	public int ghostAllocate;
	public int dropAllocate;

	protected ServerMaterialStatistics() {

	}

	public static ServerMaterialStatistics of() {

		return new ServerMaterialStatistics();
	}
}
