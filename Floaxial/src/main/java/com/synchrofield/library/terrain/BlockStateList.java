package com.synchrofield.library.terrain;

import java.util.ArrayList;

import net.minecraft.world.level.block.state.BlockState;

public class BlockStateList extends ArrayList<BlockState> {

	private static final long serialVersionUID = 1L;
	
	protected BlockStateList() {
	}
	
	public static BlockStateList of() {
		
		return new BlockStateList();
	}
}
