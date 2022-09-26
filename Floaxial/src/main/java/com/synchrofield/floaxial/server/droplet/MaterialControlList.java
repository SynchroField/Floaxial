package com.synchrofield.floaxial.server.droplet;

import com.synchrofield.floaxial.central.droplet.MaterialTable;

import net.minecraft.server.level.ServerLevel;

public class MaterialControlList {

	public MaterialControl[] list;

	protected MaterialControlList(MaterialControl[] list) {

		this.list = list;
	}

	public static MaterialControlList of() {

		MaterialControl[] list = new MaterialControl[MaterialTable.Size];

		return new MaterialControlList(list);
	}

	public void clear() {

		for (int i = 0; i < MaterialTable.Size; i++) {

			list[i].clear();
		}
	}

	public void tick(ServerLevel level, long tick) {

		for (int i = 0; i < MaterialTable.Size; i++) {

			list[i].tick(level, tick);
		}
	}
}
