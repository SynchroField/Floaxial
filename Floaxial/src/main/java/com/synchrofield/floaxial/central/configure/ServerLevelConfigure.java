package com.synchrofield.floaxial.central.configure;

public class ServerLevelConfigure {

	public final ServerDropletConfigure drop;
	public final RuleConfigureList rule;

	protected ServerLevelConfigure(ServerDropletConfigure drop, RuleConfigureList rule) {

		this.drop = drop;
		this.rule = rule;
	}

	public static ServerLevelConfigure of() {

		return new ServerLevelConfigure(ServerDropletConfigure.of(), RuleConfigureList.of());
	}

	public ServerLevelConfigure withDrop(ServerDropletConfigure drop) {

		return new ServerLevelConfigure(drop, rule);
	}

	public ServerLevelConfigure withRule(RuleConfigureList rule) {

		return new ServerLevelConfigure(drop, rule);
	}
}
