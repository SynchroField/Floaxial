package com.synchrofield.floaxial.server.rule;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;

// Each rule has its own list of sections.  
public class RuleLevel {

	public Long2ObjectOpenHashMap<RuleSection> sectionMap;

	protected RuleLevel(Long2ObjectOpenHashMap<RuleSection> sectionMap) {

		this.sectionMap = sectionMap;
	}

	public static RuleLevel of() {

		Long2ObjectOpenHashMap<RuleSection> sectionMap = new Long2ObjectOpenHashMap<>();

		return new RuleLevel(sectionMap);
	}

	public boolean sectionIsExist(SectionPos location) {

		return sectionIsExist(location.asLong());
	}

	public boolean sectionIsExist(long location) {

		return sectionMap.containsKey(location);
	}

	public RuleSection sectionGet(SectionPos location) {

		return sectionGet(location.asLong());
	}

	public RuleSection sectionGet(long location) {

		return sectionMap.get(location);
	}

	public RuleSection sectionGetByBlock(BlockPos blockLocation) {

		return sectionGet(SectionPos.of(blockLocation));
	}

	public void sectionSet(SectionPos location, RuleSection section) {

		sectionSet(location.asLong(), section);
	}

	public void sectionSet(long location, RuleSection section) {

		sectionMap.put(location, section);
	}

	public void clear() {

		sectionMap.clear();
	}

	public RuleSection sectionGetOrCreateBlock(BlockPos blockLocation) {

		return sectionGetOrCreate(SectionPos.of(blockLocation));
	}

	public RuleSection sectionGetOrCreate(SectionPos location) {

		RuleSection result = sectionGet(location);
		if (result == null) {

			result = RuleSection.of();

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

	public void sectionCreateByBlock(BlockPos blockLocation) {

		SectionPos sectionLocation = SectionPos.of(blockLocation);

		sectionCreate(sectionLocation);
	}

	public void sectionCreate(SectionPos location) {

		if (sectionGet(location) == null) {

			RuleSection ruleSection = RuleSection.of();

			sectionSet(location, ruleSection);
		}
	}
	
	public int size() {
		
		return sectionMap.size();
	}
}
