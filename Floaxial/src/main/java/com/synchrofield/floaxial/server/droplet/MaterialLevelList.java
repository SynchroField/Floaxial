package com.synchrofield.floaxial.server.droplet;

import com.synchrofield.floaxial.central.droplet.MaterialTable;

public class MaterialLevelList {

	public MaterialLevel[] list;

	protected MaterialLevelList(MaterialLevel[] list) {

		this.list = list;
	}

	public static MaterialLevelList of() {

		MaterialLevel[] list = new MaterialLevel[MaterialTable.Size];

		for (int material = 0; material < MaterialTable.Size; material++) {

			list[material] = MaterialLevel.of();
		}

		return new MaterialLevelList(list);
	}

	public void clear() {

		for (int i = 0; i < MaterialTable.Size; i++) {

			list[i].clear();
		}
	}
}
