package com.synchrofield.floaxial.server.droplet;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import net.minecraft.core.SectionPos;

public class LoadQueue {

	public final int allocateSize;
	public LongArrayFIFOQueue queue;

	protected LoadQueue(int allocateSize, LongArrayFIFOQueue queue) {

		this.allocateSize = allocateSize;
		this.queue = queue;
	}

	public static LoadQueue of(int allocateSize) {

		LongArrayFIFOQueue queue = new LongArrayFIFOQueue(allocateSize);

		return new LoadQueue(allocateSize, queue);
	}

	// warning no lock
	public boolean fullIsUnsafe() {

		return queue.size() >= allocateSize;
	}

	// warning no lock
	public boolean emptyIsUnsafe() {

		return queue.isEmpty();
	}

	public boolean tryAdd(SectionPos sectionLocation) {

		if (fullIsUnsafe()) {

			// full ignore
			return false;
		}

		queue.enqueue(sectionLocation.asLong());

		return true;
	}

	@Nullable
	public SectionPos tryConsume() {

		if (emptyIsUnsafe()) {

			return null;
		}

		return SectionPos.of(queue.dequeueLong());
	}

	public int useSize() {

		return queue.size();
	}

	public void clear() {

		queue.clear();
	}
}
