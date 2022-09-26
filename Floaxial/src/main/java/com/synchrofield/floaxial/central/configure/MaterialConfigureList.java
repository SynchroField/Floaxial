package com.synchrofield.floaxial.central.configure;

import com.synchrofield.floaxial.central.droplet.MaterialTable;

public class MaterialConfigureList {

	public final MaterialConfigure[] list;

	protected MaterialConfigureList(MaterialConfigure[] list) {

		this.list = list;
	}

	public static MaterialConfigureList of() {

		MaterialConfigure[] list = new MaterialConfigure[MaterialTable.Size];
		for (int i = 0; i < MaterialTable.Size; i++) {

			list[i] = MaterialConfigure.of();
		}

		return new MaterialConfigureList(list);
	}

	public MaterialConfigureList withList(MaterialConfigure[] list) {

		return new MaterialConfigureList(list);
	}
}