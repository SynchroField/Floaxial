package com.synchrofield.floaxial.server.droplet;

import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import net.minecraft.core.BlockPos;

public class GapQueue {

	public final int sizeMaximum;

	public final LongArrayFIFOQueue queue;

	protected GapQueue(int sizeMaximum, LongArrayFIFOQueue queue) {

		assert sizeMaximum > 0;

		this.sizeMaximum = sizeMaximum;
		this.queue = queue;
	}

	public static GapQueue of(int sizeMaximum) {

		LongArrayFIFOQueue queue = new LongArrayFIFOQueue();

		return new GapQueue(sizeMaximum, queue);
	}

	public void clear() {

		queue.clear();
	}

	public void trim() {

		queue.trim();
	}

	public void add(BlockPos location) {

		addPack(location.asLong());
	}

	// silent fail if full
	public void addPack(long locationPack) {

		if (fullIs()) {

			return;
		}

		queue.enqueue(locationPack);
	}

	public boolean fullIs() {

		return useSize() >= sizeMaximum;
	}

	public long consumePack() {

		assert useSize() > 0;

		return queue.dequeueLong();
	}

	public BlockPos consume() {

		return BlockPos.of(consumePack());
	}

	public int useSize() {

		return queue.size();
	}

	public boolean emptyIs() {

		return queue.isEmpty();
	}
}
