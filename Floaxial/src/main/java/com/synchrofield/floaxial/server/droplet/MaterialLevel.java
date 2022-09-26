package com.synchrofield.floaxial.server.droplet;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;

public class MaterialLevel {

	// item can be null
	public Long2ObjectOpenHashMap<DropletSection> sectionMap;

	protected MaterialLevel(Long2ObjectOpenHashMap<DropletSection> sectionMap) {

		this.sectionMap = sectionMap;
	}

	public static MaterialLevel of() {

		Long2ObjectOpenHashMap<DropletSection> sectionMap = new Long2ObjectOpenHashMap<>();

		return new MaterialLevel(sectionMap);
	}

	public boolean sectionIsExist(SectionPos location) {

		return sectionIsExist(location.asLong());
	}

	public boolean sectionIsExist(long location) {

		return sectionMap.containsKey(location);
	}

	public DropletSection sectionGet(SectionPos location) {

		return sectionGet(location.asLong());
	}

	public DropletSection sectionGet(long location) {

		return sectionMap.get(location);
	}

	public DropletSection sectionGetByBlock(BlockPos dropLocation) {

		return sectionGet(SectionPos.of(dropLocation));
	}

	public void sectionSet(SectionPos location, DropletSection section) {

		sectionSet(location.asLong(), section);
	}

	public void sectionSet(long location, DropletSection section) {

		sectionMap.put(location, section);
	}

	public void clear() {

		sectionMap.clear();
	}

	public DropletSection sectionGetOrCreateBlock(BlockPos dropLocation) {

		return sectionGetOrCreate(SectionPos.of(dropLocation));
	}

	public DropletSection sectionGetOrCreate(SectionPos location) {

		DropletSection result = sectionGet(location);
		if (result == null) {

			result = DropletSection.of();

			sectionSet(location, result);
		}

		return result;
	}

	public void sectionRemove(SectionPos location) {

		sectionRemove(location.asLong());
	}

	public void sectionRemove(long location) {

		sectionMap.remove(location);
	}

	public void ghostAdd(BlockPos location, int drop) {

		// create section on demand
		DropletSection destinationSection = sectionGetOrCreateBlock(location);

		// just append drop
		destinationSection.ghostAdd(drop);
	}

	public void dropAdd(BlockPos location, int drop) {

		// create section on demand
		DropletSection destinationSection = sectionGetOrCreateBlock(location);

		// just append drop
		destinationSection.dropAdd(drop);
	}

	public int ghostUseDerive() {

		int useSize = 0;

		ObjectIterator<Long2ObjectMap.Entry<DropletSection>> iterator = sectionMap
				.long2ObjectEntrySet()
				.iterator();

		while (iterator.hasNext()) {

			Long2ObjectMap.Entry<DropletSection> entry = iterator.next();

			DropletSection dropSection = entry.getValue();

			useSize += dropSection.ghostUseSize();
		}

		return useSize;
	}

	public int dropUseDerive() {

		int useSize = 0;

		ObjectIterator<Long2ObjectMap.Entry<DropletSection>> iterator = sectionMap
				.long2ObjectEntrySet()
				.iterator();

		while (iterator.hasNext()) {

			Long2ObjectMap.Entry<DropletSection> entry = iterator.next();

			DropletSection dropSection = entry.getValue();

			useSize += dropSection.dropUseSize();
		}

		return useSize;
	}

	public boolean ghostIsExist(BlockPos location) {

		DropletSection section = sectionGet(SectionPos.of(location));
		if (section == null) {

			// not even section
			return false;
		}

		short locationPack = (short) Droplet.locationToPack(location);

		return section.ghostIsExist(locationPack);
	}

	public boolean dropIsExist(BlockPos location) {

		DropletSection section = sectionGet(SectionPos.of(location));
		if (section == null) {

			// not even section
			return false;
		}

		short locationPack = (short) Droplet.locationToPack(location);

		return section.dropIsExist(locationPack);
	}

	// assume exist
	public int ghostGet(BlockPos location) {

		DropletSection section = sectionGet(SectionPos.of(location));

		assert section != null;

		short locationPack = (short) Droplet.locationToPack(location);

		return section.ghostGet(locationPack);
	}

	// assume exist
	public int dropGet(BlockPos location) {

		DropletSection section = sectionGet(SectionPos.of(location));

		assert section != null;

		short locationPack = (short) Droplet.locationToPack(location);

		return section.dropGet(locationPack);
	}

	public void ghostRemove(BlockPos location) {

		DropletSection section = sectionGet(SectionPos.of(location));

		if (section == null) {

			return;
		}

		short locationPack = (short) Droplet.locationToPack(location);

		section.ghostRemove(locationPack);
	}

	public void dropRemove(BlockPos location) {

		DropletSection section = sectionGet(SectionPos.of(location));

		if (section == null) {

			return;
		}

		short locationPack = (short) Droplet.locationToPack(location);

		section.dropRemove(locationPack);
	}
}
