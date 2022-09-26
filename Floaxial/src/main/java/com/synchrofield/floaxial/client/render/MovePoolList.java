package com.synchrofield.floaxial.client.render;

import com.synchrofield.floaxial.central.configure.ClientDropletConfigure;
import com.synchrofield.floaxial.central.droplet.MaterialTable;

public class MovePoolList {

	public final MovePool[] list;

	protected MovePoolList(MovePool[] list) {

		this.list = list;
	}

	public static MovePoolList of(ClientDropletConfigure dropConfigure) {

		MovePool[] list = new MovePool[MaterialTable.Size];

		for (int i = 0; i < MaterialTable.Size; i++) {

			list[i] = MovePool.of(dropConfigure.material[i].animateSize);
		}

		return new MovePoolList(list);
	}

	public void animateListFill() {

		for (int i = 0; i < MaterialTable.Size; i++) {

			list[i].animateListFill();
		}
	}

	public int useSize() {

		int result = 0;

		for (int i = 0; i < MaterialTable.Size; i++) {

			result += list[i].useSize();
		}

		return result;
	}
}
