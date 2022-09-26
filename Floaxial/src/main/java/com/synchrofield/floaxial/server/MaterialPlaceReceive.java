package com.synchrofield.floaxial.server;

import net.minecraft.core.BlockPos;

public interface MaterialPlaceReceive {

	public void onRulePlaceMaterial(BlockPos location, int ruleIndex, int outputIndex);

	public void onDropArchive(BlockPos location, int materialIndex);
}
