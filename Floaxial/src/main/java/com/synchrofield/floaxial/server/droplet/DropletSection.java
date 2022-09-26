package com.synchrofield.floaxial.server.droplet;

import it.unimi.dsi.fastutil.shorts.Short2ShortOpenHashMap;

public class DropletSection {

	public Short2ShortOpenHashMap ghostMap;
	public Short2ShortOpenHashMap dropMap;

	protected DropletSection(Short2ShortOpenHashMap ghostMap, Short2ShortOpenHashMap dropMap) {

		this.ghostMap = ghostMap;
		this.dropMap = dropMap;
	}

	public static DropletSection of() {

		Short2ShortOpenHashMap ghostMap = new Short2ShortOpenHashMap();
		Short2ShortOpenHashMap dropMap = new Short2ShortOpenHashMap();

		return new DropletSection(ghostMap, dropMap);
	}

	public static DropletSection of(int ghostListAllocateSize, int dropListAllocateSize) {

		Short2ShortOpenHashMap ghostMap = new Short2ShortOpenHashMap(ghostListAllocateSize);
		Short2ShortOpenHashMap dropMap = new Short2ShortOpenHashMap(dropListAllocateSize);

		return new DropletSection(ghostMap, dropMap);
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

	public boolean emptyIs() {

		return (ghostUseSize() == 0) && (dropUseSize() == 0);
	}

	public int ghostGet(short locationPack) {

		return ghostMap.get(locationPack);
	}

	public int dropGet(short locationPack) {

		return dropMap.get(locationPack);
	}

	public void ghostRemove(short locationPack) {

		ghostMap.remove(locationPack);
	}

	public void dropRemove(short locationPack) {

		dropMap.remove(locationPack);
	}
}
