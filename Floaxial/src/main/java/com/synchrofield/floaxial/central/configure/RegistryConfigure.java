package com.synchrofield.floaxial.central.configure;

import com.synchrofield.library.configure.BlockConfigure;
import com.synchrofield.library.configure.ItemConfigure;

public class RegistryConfigure {

	// block
	public final BlockConfigure VanillaSandBlock = BlockConfigure.ofMod("vanilla_sand");
	public final BlockConfigure VanillaGravelBlock = BlockConfigure.ofMod("vanilla_gravel");
	public final BlockConfigure VanillaJungleSaplingBlock = BlockConfigure.ofMod("vanilla_jungle_sapling");
	public final BlockConfigure VanillaJungleLeafBlock = BlockConfigure
			.ofMod("vanilla_jungle_leaf");

	public final BlockConfigure MarkBlock = BlockConfigure.ofMod("mark");

	public final BlockConfigure GhostBlock = BlockConfigure.ofMod("ghost");

	public final BlockConfigure PalmLeafXBlock = BlockConfigure.ofMod("palm_leaf_x");
	public final BlockConfigure PalmLeafZBlock = BlockConfigure.ofMod("palm_leaf_z");
	public final BlockConfigure PalmLeafXEndBlock = BlockConfigure.ofMod("palm_leaf_x_end");
	public final BlockConfigure CoconutBlock = BlockConfigure.ofMod("coconut");

	public final BlockConfigure SaltWaterBlock = BlockConfigure.ofMod("salt_water");
	public final BlockConfigure SaltWaterRenderBlock = BlockConfigure.ofMod("salt_water_render");

	// item
	public final ItemConfigure SaltWaterBucketItem = ItemConfigure.ofMod("salt_water_bucket");

	protected RegistryConfigure() {

	}

	public static RegistryConfigure of() {

		return new RegistryConfigure();
	}
}