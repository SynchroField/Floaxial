package com.synchrofield.floaxial.central.configure.store;

import java.util.Optional;

import com.synchrofield.floaxial.central.droplet.MaterialTable;
import com.synchrofield.library.configure.ConfigureStore;

// per material store
public class MaterialStoreTable {

	public final MaterialStore list[];

	protected MaterialStoreTable(MaterialStore list[]) {

		this.list = list;
	}

	public static MaterialStoreTable of(Optional<String> folder) {

		MaterialStore list[] = new MaterialStore[MaterialTable.Size];

		for (int i = 0; i < MaterialTable.Size; i++) {

			String filename = filenameDerive(i);
			list[i] = MaterialStore.of(folder, filename, i);
		}

		return new MaterialStoreTable(list);
	}

	public static String filenameDerive(int materialIndex) {

		return MaterialStore.FilenameBase + materialIndex + "." + ConfigureStore.FileExtension;
	}

	public void copyFromFile() {

		for (MaterialStore store : list) {

			store.copyFromFile();
		}
	}
}