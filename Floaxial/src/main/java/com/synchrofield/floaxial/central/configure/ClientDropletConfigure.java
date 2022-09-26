package com.synchrofield.floaxial.central.configure;

import com.synchrofield.floaxial.central.droplet.MaterialTable;

public class ClientDropletConfigure {

	public static final boolean GhostIsVisibleDefault = false;

	public final boolean ghostIsVisible;

	public final ClientMaterialConfigure[] material;

	protected ClientDropletConfigure(boolean ghostIsVisible, ClientMaterialConfigure[] material) {

		this.ghostIsVisible = ghostIsVisible;
		this.material = material;
	}

	public static ClientDropletConfigure of() {

		ClientMaterialConfigure[] material = new ClientMaterialConfigure[MaterialTable.Size];
		for (int i = 0; i < MaterialTable.Size; i++) {

			material[i] = ClientMaterialConfigure.of();
		}

		return new ClientDropletConfigure(GhostIsVisibleDefault, material);
	}

	public ClientDropletConfigure withGhostIsVisible(boolean ghostIsVisible) {

		return new ClientDropletConfigure(ghostIsVisible, material);
	}

	public ClientDropletConfigure withMaterial(ClientMaterialConfigure[] material) {

		return new ClientDropletConfigure(ghostIsVisible, material);
	}
}
