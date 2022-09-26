package com.synchrofield.floaxial.central.configure;

import com.mojang.math.Vector3f;

public class ClientConfigure {

	public final ClientLevelConfigure level;
	public final Vector3f waterFogColor = new Vector3f(0.1f, 0.2f, 0.8f);

	protected ClientConfigure(ClientLevelConfigure level) {

		this.level = level;
	}

	public static ClientConfigure of() {

		return new ClientConfigure(ClientLevelConfigure.of());
	}

	public ClientConfigure withLevel(ClientLevelConfigure level) {

		return new ClientConfigure(level);
	}
}
