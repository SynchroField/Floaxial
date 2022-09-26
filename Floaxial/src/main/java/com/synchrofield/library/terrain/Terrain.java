package com.synchrofield.library.terrain;

import java.util.Map;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.ForgeRegistries;

// level and block related
public class Terrain {

	// filter out any chunk with null key
	public static ChunkLocationList chunkGetLoadList(ServerLevel level) {

		ServerChunkCache chunkCache = level.getChunkSource();

		// access transform
		Long2ObjectSortedMap.FastSortedEntrySet<ChunkHolder> chunkHolderSet = chunkCache.chunkMap.visibleChunkMap
				.long2ObjectEntrySet();

		int chunkListSize = chunkHolderSet.size();
		if (chunkListSize <= 0) {

			return ChunkLocationList.of();
		}

		ChunkLocationList chunkLocationList = ChunkLocationList.of();

		for (Long2ObjectMap.Entry<ChunkHolder> entry : chunkHolderSet) {

			if (entry == null) {

				continue;
			}

			ChunkHolder holder = entry.getValue();
			if (holder == null) {

				continue;
			}

			LevelChunk levelChunk = holder.getTickingChunk();
			if (levelChunk == null) {

				continue;
			}

			ChunkPos chunkLocation = holder.getPos();

			chunkLocationList.add(chunkLocation);
		}

		return chunkLocationList;
	}

	public static SectionLocationList sectionGetLoadList(ServerLevel level,
			ChunkLocationList chunkLocationList) {

		SectionLocationList sectionLocationList = SectionLocationList.of();

		for (ChunkPos chunkLocation : chunkLocationList) {

			LevelChunk chunk = level.getChunk(chunkLocation.x, chunkLocation.z);

			for (LevelChunkSection section : chunk.getSections()) {

				SectionPos sectionLocation = SectionPos.of(chunkLocation,
						section.bottomBlockY() / 16);

				sectionLocationList.add(sectionLocation);
			}
		}

		return sectionLocationList;
	}

	public static SectionLocationList sectionGetLoadList(ServerLevel level) {

		ChunkLocationList chunkLoadList = chunkGetLoadList(level);

		return sectionGetLoadList(level, chunkLoadList);
	}

	public static SectionLocationList sectionFilterList(ServerLevel level,
			SectionLocationList sourceList, BlockStateList stateList) {

		SectionLocationList destinationList = SectionLocationList.of();

		for (SectionPos sectionLocation : sourceList) {

			LevelChunkSection section = sectionGet(level, sectionLocation);

			if (sectionFindState(section, stateList)) {

				destinationList.add(sectionLocation);
			}
		}

		return destinationList;
	}

	public static LevelChunk chunkGet(ServerLevel level, ChunkPos chunkLocation) {

		return level.getChunk(chunkLocation.x, chunkLocation.z);
	}

	public static LevelChunkSection sectionGet(ServerLevel level, SectionPos sectionLocation) {

		LevelChunk chunk = chunkGet(level, sectionLocation.chunk());
		if (chunk == null) {

			return null;
		}

		// might need to convert to pack not sure
		return sectionGet(chunk, sectionLocation.y());
	}

	public static LevelChunkSection sectionGet(LevelChunk chunk, int sectionIndex) {

		return chunk.getSection(sectionIndex);
	}

	public static int sectionGetIndex(LevelChunkSection section) {

		return section.bottomBlockY() >> 4;
	}

	public static boolean sectionFindState(LevelChunkSection section, BlockStateList stateList) {

		PalettedContainer<BlockState> palette = section.getStates();

		for (int blockStateIndex = 0; blockStateIndex < stateList.size(); blockStateIndex++) {

			final BlockState blockState = stateList.get(blockStateIndex);

			// normal source block
			if (palette.maybeHas(t -> (t == blockState))) {

				return true;
			}
		}

		return false;
	}

	public static boolean chunkIsTick(ChunkHolder holder) {

		ChunkHolder.FullChunkStatus fullStatus = holder.getFullStatus();

		return (fullStatus == ChunkHolder.FullChunkStatus.TICKING)
				|| (fullStatus == ChunkHolder.FullChunkStatus.ENTITY_TICKING);
	}

	public static void stateSet(ServerLevel level, BlockPos location, BlockState state) {

		level.setBlock(location, state, 2);
	}

	public static BlockState stateGet(ServerLevel level, BlockPos location) {

		return level.getBlockState(location);
	}

	public static BlockState blockGetDefaultState(ResourceLocation name) {

		return ForgeRegistries.BLOCKS.getValue(name)
				.defaultBlockState();
	}

	// State should be in square brackets in the form [key=value, key2=value2] .
	// For example "minecraft:furnace[facing=south]" .  Curly braces are not supported.
	public static BlockState blockStateFromString(String text) throws TerrainException {

		StringReader reader = new StringReader(text);

		BlockStateParser parser = new BlockStateParser(reader, false);

		try {

			parser.parse(true);
		}
		catch (CommandSyntaxException e) {

			throw new RuntimeException(e.getMessage());
		}

		return parser.getState();
	}

	public static void blockStateValidate(String text) throws TerrainException {

		blockStateFromString(text);
	}

	public static Map<Property<?>, Comparable<?>> blockStatePropertyMapFromString(String text)
			throws TerrainException {

		StringReader reader = new StringReader(text);

		BlockStateParser parser = new BlockStateParser(reader, false);

		try {

			parser.parse(true);
		}
		catch (CommandSyntaxException e) {

			throw new RuntimeException(e.getMessage());
		}

		return parser.getProperties();
	}

	// check each property in criteria against target
	// ignore non-specified property in target
	public static boolean blockStateCompareSubset(BlockState target, Block criteriaBlock,
			Map<Property<?>, Comparable<?>> criteriaPropertyMap) {

		if (!target.is(criteriaBlock)) {

			// different block
			return false;
		}

		for (Map.Entry<Property<?>, Comparable<?>> entry : criteriaPropertyMap.entrySet()) {

			if (!target.hasProperty(entry.getKey())) {

				// doesn't even have property
				return false;
			}

			if (!target.getValue(entry.getKey())
					.equals(entry.getValue())) {

				// different value
				return false;
			}
		}

		return true;
	}

	public static void entityAddDirect(ServerLevel level, Entity entity) {

		ServerChunkCache chunkCache = level.getChunkSource();

		chunkCache.addEntity(entity);
	}

	public static void entityRemoveDirect(ServerLevel level, Entity entity) {

		ServerChunkCache chunkCache = level.getChunkSource();

		chunkCache.removeEntity(entity);
	}

	public static Block blockFromString(String text) throws TerrainException {

		ResourceLocation resourceLocation = new ResourceLocation(text);

		if (!ForgeRegistries.BLOCKS.containsKey(resourceLocation)) {

			throw TerrainException.of("Block \"" + text + "\" not found.");
		}

		return ForgeRegistries.BLOCKS.getValue(resourceLocation);
	}

	public static Material blockMaterialFromString(String text) throws TerrainException {

		switch (text.toLowerCase()) {

		default: {

			throw TerrainException.of("Block material \"" + text + "\" not found.");
		}

		case "air": {

			return Material.AIR;
		}

		case "clay": {

			return Material.CLAY;
		}

		case "dirt": {

			return Material.DIRT;
		}

		case "fire": {

			return Material.FIRE;
		}

		case "glass": {

			return Material.GLASS;
		}

		case "grass": {

			return Material.GRASS;
		}

		case "ice": {

			return Material.ICE;
		}

		case "lava": {

			return Material.LAVA;
		}

		case "leaves": {

			return Material.LEAVES;
		}

		case "metal": {

			return Material.METAL;
		}

		case "plant": {

			return Material.PLANT;
		}

		case "portal": {

			return Material.PORTAL;
		}

		case "powder_snow": {

			return Material.POWDER_SNOW;
		}

		case "sand": {

			return Material.SAND;
		}

		case "snow": {

			return Material.SNOW;
		}

		case "water": {

			return Material.WATER;
		}

		case "wood": {

			return Material.WOOD;
		}

		case "wool": {

			return Material.WOOL;
		}
		}
	}

	public static boolean stateIsSolid(BlockState state) {

		if (state.isAir()) {

			return false;
		}

		if (state.getMaterial() == Material.WATER) {

			return false;
		}

		return true;
	}

	public static boolean stateIsHeavy(BlockState state) {

		if (state.getMaterial() == Material.STONE) {

			return true;
		}

		if (state.getMaterial() == Material.DIRT) {

			return true;
		}

		// not sand for now

		return false;
	}

	public static boolean stateIsReplaceable(BlockState state) {

		if (state.isAir()) {

			return true;
		}

		return false;
	}
}
