package com.synchrofield.library.terrain;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectSortedMap;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;

// Build chunk list incrementally over ticks.
public class ChunkListIncrement {

	public static final int IteratePerCycleDefault = 100;
	public static final int IteratePerCycleMinimum = 1;
	public static final int IteratePerCycleMaximum = 100000;

	protected State state;

	protected final int iteratePerCycle;
	protected final ServerChunkCache chunkCache;

	public ChunkLocationList currentLocationList;
	protected ChunkLocationList buildLocationList;

	protected Long2ObjectSortedMap.FastSortedEntrySet<ChunkHolder> chunkSet;
	protected ObjectBidirectionalIterator<Long2ObjectMap.Entry<ChunkHolder>> iterator;

	protected ChunkListIncrement(int iteratePerCycle, ServerChunkCache chunkCache) {

		assert iteratePerCycle >= IteratePerCycleMinimum;
		assert iteratePerCycle <= IteratePerCycleMaximum;
		this.iteratePerCycle = iteratePerCycle;

		this.chunkCache = chunkCache;

		this.state = State.Stop;

		// always kept valid for caller to use
		this.currentLocationList = ChunkLocationList.of();

		// rest built on demand
		this.buildLocationList = null;
		this.chunkSet = null;
		this.iterator = null;
	}

	public static ChunkListIncrement of(int iteratePerCycle, ServerLevel level) {

		return new ChunkListIncrement(iteratePerCycle, level.getChunkSource());
	}

	public void start() {

		state = State.ChunkSetDerive;
	}

	public void stop() {

		state = State.Stop;
	}

	public void reset() {

		iterator = chunkSet.iterator();
	}

	public void cycle() {

		switch (state) {

		default:
		case Stop: {

			return;
		}

		case ChunkSetDerive: {

			cycleChunkSetDerive();
			break;
		}

		case Start: {

			cycleNormal();
			break;
		}
		}
	}

	public void cycleChunkSetDerive() {

		// just derive key list all at once for now, later can do it item by item
		chunkSet = chunkSetDerive();

		buildLocationList = ChunkLocationList.of();

		reset();

		state = State.Start;
	}

	protected void currentListFlip() {

		currentLocationList = buildLocationList;

		buildLocationList = ChunkLocationList.of();
	}

	public void cycleNormal() {

		for (int iterateIndex = 0; iterateIndex < iteratePerCycle; iterateIndex++) {

			if (!iterator.hasNext()) {

				// list build complete, replace current
				currentListFlip();
				return;
			}

			// add chunk if exist
			Long2ObjectMap.Entry<ChunkHolder> entry = iterator.next();
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

			buildLocationList.add(holder.getPos());
		}
	}

	protected Long2ObjectSortedMap.FastSortedEntrySet<ChunkHolder> chunkSetDerive() {

		return chunkCache.chunkMap.visibleChunkMap.long2ObjectEntrySet();
	}

	public enum State {

		Stop, ChunkSetDerive, Start,
	}
}
