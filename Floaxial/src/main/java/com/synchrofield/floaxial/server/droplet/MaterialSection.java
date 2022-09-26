package com.synchrofield.floaxial.server.droplet;

import it.unimi.dsi.fastutil.shorts.Short2ShortOpenHashMap;

public class MaterialSection {

	public Short2ShortOpenHashMap ghostMap;
	public Short2ShortOpenHashMap dropMap;

	protected MaterialSection(Short2ShortOpenHashMap ghostMap, Short2ShortOpenHashMap dropMap) {

		this.ghostMap = ghostMap;
		this.dropMap = dropMap;
	}

	public static MaterialSection of() {

		Short2ShortOpenHashMap ghostMap = new Short2ShortOpenHashMap();
		Short2ShortOpenHashMap dropMap = new Short2ShortOpenHashMap();

		return new MaterialSection(ghostMap, dropMap);
	}

	public static MaterialSection of(int ghostListAllocateSize, int dropListAllocateSize) {

		Short2ShortOpenHashMap ghostMap = new Short2ShortOpenHashMap(ghostListAllocateSize);
		Short2ShortOpenHashMap dropMap = new Short2ShortOpenHashMap(dropListAllocateSize);

		return new MaterialSection(ghostMap, dropMap);
	}

	public void clear() {

		ghostMap.clear();
		dropMap.clear();
	}

	public void ghostAdd(int ghost) {

		ghostMap.put((short) ghost, (short) (ghost >>> 16));
	}

	public void ghostAdd(short locationPack, short data) {

		ghostMap.put(locationPack, data);
	}

	public void dropAdd(int drop) {

		dropMap.put((short) drop, (short) (drop >>> 16));
	}

	public void dropAdd(short locationPack, short data) {

		dropMap.put(locationPack, data);
	}

	public int ghostUseSize() {

		return ghostMap.short2ShortEntrySet()
				.size();
	}

	public int dropUseSize() {

		return dropMap.short2ShortEntrySet()
				.size();
	}

	public boolean ghostIsExist(short locationPack) {

		return ghostMap.containsKey(locationPack);
	}

	public boolean dropIsExist(short locationPack) {

		return dropMap.containsKey(locationPack);
	}
}
