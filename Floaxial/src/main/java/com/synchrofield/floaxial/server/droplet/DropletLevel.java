package com.synchrofield.floaxial.server.droplet;

public class DropletLevel {

	public final MaterialLevelList material;

	protected DropletLevel(MaterialLevelList material) {

		this.material = material;
	}

	public static DropletLevel of() {

		MaterialLevelList materialLevelList = MaterialLevelList.of();

		return new DropletLevel(materialLevelList);
	}

	public void clear() {

		material.clear();
	}
}
