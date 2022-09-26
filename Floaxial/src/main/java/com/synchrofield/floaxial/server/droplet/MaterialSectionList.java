package com.synchrofield.floaxial.server.droplet;

import com.synchrofield.floaxial.central.droplet.MaterialTable;

public class MaterialSectionList {

	public MaterialSection[] list;

	protected MaterialSectionList(MaterialSection[] list) {

		assert list.length == MaterialTable.Size;

		this.list = list;
	}

	public static MaterialSectionList of() {

		MaterialSection[] list = new MaterialSection[MaterialTable.Size];

		for (int material = 0; material < MaterialTable.Size; material++) {

			list[material] = MaterialSection.of();
		}

		return new MaterialSectionList(list);
	}

	public void clear() {

		for (int i = 0; i < MaterialTable.Size; i++) {

			list[i].clear();
		}
	}

	public void ghostAdd(int material, int drop) {

		list[material].ghostAdd(drop);
	}

	public void dropAdd(int material, int drop) {

		list[material].dropAdd(drop);
	}

	public int ghostUseSize() {

		int result = 0;

		for (int i = 0; i < MaterialTable.Size; i++) {

			result += list[i].ghostUseSize();
		}

		return result;
	}

	public int dropUseSize() {

		int result = 0;

		for (int i = 0; i < MaterialTable.Size; i++) {

			result += list[i].dropUseSize();
		}

		return result;
	}

	public boolean ghostIsExist(int material, short locationPack) {

		return list[material].ghostIsExist(locationPack);
	}

	public boolean dropIsExist(int material, short locationPack) {

		return list[material].dropIsExist(locationPack);
	}

	public boolean emptyIs() {

		return (ghostUseSize() == 0) && (dropUseSize() == 0);
	}
}
