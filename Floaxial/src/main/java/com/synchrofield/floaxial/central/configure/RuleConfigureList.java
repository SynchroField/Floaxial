package com.synchrofield.floaxial.central.configure;

import com.synchrofield.floaxial.server.rule.RuleTable;

public class RuleConfigureList {

	public final RuleConfigure[] list;

	protected RuleConfigureList(RuleConfigure[] list) {

		this.list = list;
	}

	public static RuleConfigureList of() {

		RuleConfigure[] list = new RuleConfigure[RuleTable.ListSize];
		for (int i = 0; i < RuleTable.ListSize; i++) {

			list[i] = RuleConfigure.of();
		}

		return new RuleConfigureList(list);
	}

	public RuleConfigureList withList(RuleConfigure[] list) {

		return new RuleConfigureList(list);
	}
}