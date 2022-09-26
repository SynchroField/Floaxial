package com.synchrofield.floaxial.server.rule;

import com.synchrofield.library.configure.ConfigureException;

public class Rule {

	public boolean enableIs;

	public final RuleSchedule schedule;
	public final RuleCriteria criteria;
	public final RuleProcess process;

	protected Rule(RuleSchedule schedule, RuleCriteria criteria, RuleProcess process) {

		this.schedule = schedule;
		this.criteria = criteria;
		this.process = process;
	}

	public static Rule of(RuleSchedule schedule, RuleCriteria criteria, RuleProcess process) {

		return new Rule(schedule, criteria, process);
	}

	public void compile() throws ConfigureException {

		if (!enableIs) {

			return;
		}

		criteria.compile();
		process.compile();
	}
}
