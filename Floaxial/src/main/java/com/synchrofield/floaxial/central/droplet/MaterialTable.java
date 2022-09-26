package com.synchrofield.floaxial.central.droplet;

import com.synchrofield.floaxial.central.configure.MaterialConfigureList;
import com.synchrofield.floaxial.central.registry.CentralRegistry;
import com.synchrofield.library.configure.ConfigureException;
import com.synchrofield.library.math.MathUtility;

import net.minecraft.world.level.block.state.BlockState;

// hold all material in a fix size array
// shared between client and server
public class MaterialTable {

	public static final int InvalidIndex = -1;

	public static final int BitSize = 3;
	public static final int Maximum = (1 << BitSize) - 1;
	public static final int Size = Maximum + 1;

	public final DropletMaterial[] list;

	protected MaterialTable(DropletMaterial[] list) {

		assert BitSize > 0;
		assert list.length == Size;

		this.list = list;
	}

	public static MaterialTable of(MaterialConfigureList materialConfigure,
			CentralRegistry registry) {

		DropletMaterial[] list = new DropletMaterial[Size];

		for (int i = 0; i < Size; i++) {

			list[i] = DropletMaterial.of(materialConfigure.list[i]);
		}

		MaterialTable materialTable = new MaterialTable(list);

		materialTable.cacheDerive(registry);

		return materialTable;
	}

	public void cacheDerive(CentralRegistry registry) throws ConfigureException {

		for (int i = 0; i < Size; i++) {

			list[i].cacheDerive(i, registry);
		}
	}

	public static boolean indexCheck(int material) {

		return MathUtility.msb(material) < BitSize;
	}

	public int mobileStateToMaterial(BlockState state) {

		for (int i = 0; i < Size; i++) {

			if (list[i].dropletStateIs(state)) {

				return i;
			}
		}

		return InvalidIndex;
	}

	public boolean dropIs(BlockState state) {

		return mobileStateToMaterial(state) == InvalidIndex;
	}

	public boolean materialIs(int index, BlockState state) {

		if (list[index].dropletStateIs(state)) {

			return true;
		}

		// also check ghost
		if (state == list[index].ghostState) {

			return true;
		}

		return false;
	}
}
