package com.synchrofield.library.terrain;

import java.util.ArrayList;

import net.minecraft.core.BlockPos;

public class BlockLocationList extends ArrayList<BlockPos>{

	private static final long serialVersionUID = 1L;
	
	protected BlockLocationList() {
	}
	
	public static BlockLocationList of() {
		
		return new BlockLocationList();
	}
}
