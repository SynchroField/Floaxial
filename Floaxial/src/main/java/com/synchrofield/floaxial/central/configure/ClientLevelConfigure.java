package com.synchrofield.floaxial.central.configure;

public class ClientLevelConfigure {

	public final ClientDropletConfigure drop;

	protected ClientLevelConfigure(ClientDropletConfigure drop) {

		this.drop = drop;
	}

	public static ClientLevelConfigure of() {

		return new ClientLevelConfigure(ClientDropletConfigure.of());
	}

	public ClientLevelConfigure withDrop(ClientDropletConfigure drop) {

		return new ClientLevelConfigure(drop);
	}
}
