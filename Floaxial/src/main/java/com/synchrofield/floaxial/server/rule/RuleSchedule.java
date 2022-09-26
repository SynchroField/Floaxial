package com.synchrofield.floaxial.server.rule;

public class RuleSchedule {

	public final int sectionPerTick;
	public final int tickPerLevel;
	public final int blockPerSection;

	protected RuleSchedule(int sectionPerTick, int tickPerLevel, int blockPerSection) {

		this.sectionPerTick = sectionPerTick;
		this.tickPerLevel = tickPerLevel;
		this.blockPerSection = blockPerSection;
	}

	public static RuleSchedule of(int sectionPerTick, int tickPerLevel, int blockPerSection) {

		return new RuleSchedule(sectionPerTick, tickPerLevel, blockPerSection);
	}
}
