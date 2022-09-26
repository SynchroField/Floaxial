package com.synchrofield.library.terrain;

import java.util.ArrayList;

import net.minecraft.world.level.ChunkPos;

public class ChunkLocationList extends ArrayList<ChunkPos>{

	private static final long serialVersionUID = 1L;
	
	protected ChunkLocationList() {
	}
	
	public static ChunkLocationList of() {
		
		return new ChunkLocationList();
	}
}
