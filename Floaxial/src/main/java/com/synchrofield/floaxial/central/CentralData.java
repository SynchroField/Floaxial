package com.synchrofield.floaxial.central;

import com.synchrofield.floaxial.central.configure.MaterialConfigureList;
import com.synchrofield.floaxial.central.droplet.MaterialTable;
import com.synchrofield.floaxial.central.registry.CentralRegistry;
import com.synchrofield.library.configure.ConfigureException;

// any shared data for client and server
public class CentralData {

	public final MaterialTable materialTable;

	protected CentralData(MaterialTable materialTable) {

		this.materialTable = materialTable;
	}

	public static CentralData of(MaterialConfigureList materialConfigure, CentralRegistry registry)
			throws ConfigureException {

		MaterialTable materialTable = MaterialTable.of(materialConfigure, registry);

		return new CentralData(materialTable);
	}
}
