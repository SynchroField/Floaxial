package com.synchrofield.floaxial.central.statistics;

public class RuleStatistics {

	public int sectionListSize;
	public int sectionPerTick;
	public float samplePerTickMean;

	protected RuleStatistics() {

	}

	public static RuleStatistics of() {

		return new RuleStatistics();
	}
}
