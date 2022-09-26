package com.synchrofield.floaxial.central.statistics;

public class ClientMaterialStatistics {

	// drop being animated
	public int animateUse;

	// allocate size in element
	public int animateAllocate;

	public int animateTotal;

	// animate list full
	public int animateFailTotal;

	// total receive
	public int networkReceiveSection;
	public int networkReceiveDrop;
	
	protected ClientMaterialStatistics() {
		
	}
	
	public static ClientMaterialStatistics of() {
		
		return new ClientMaterialStatistics();
	}
}
