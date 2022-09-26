package com.synchrofield.floaxial.central.configure;

public class ServerConfigure {

	public final ServerLevelConfigure level;

	protected ServerConfigure(ServerLevelConfigure level) {

		this.level = level;
	}

	public static ServerConfigure of() {

		return new ServerConfigure(ServerLevelConfigure.of());
	}

	public ServerConfigure withLevel(ServerLevelConfigure level) {

		return new ServerConfigure(level);
	}
}
