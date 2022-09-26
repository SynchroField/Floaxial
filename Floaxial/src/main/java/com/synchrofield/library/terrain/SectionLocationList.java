package com.synchrofield.library.terrain;

import java.util.ArrayList;

import net.minecraft.core.SectionPos;

public class SectionLocationList extends ArrayList<SectionPos>{

	private static final long serialVersionUID = 1L;
	
	protected SectionLocationList() {
	}
	
	public static SectionLocationList of() {
		
		return new SectionLocationList();
	}
}
