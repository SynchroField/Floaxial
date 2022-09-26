package com.synchrofield.library.list;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

// queue holding array indexes of a static table 
public class IndexQueue {

	public static int IndexInvalid = -1;

	public static final int IndexListSizeDefault = 1024;

	public final int indexListSize;
	public final Queue<Integer> queue;

	protected IndexQueue(int indexListSize, Queue<Integer> queue) {

		this.indexListSize = indexListSize;
		this.queue = queue;
	}

	public static IndexQueue of() {

		int indexListSize = IndexListSizeDefault;
		int allocateSize = indexListSize;
		return new IndexQueue(indexListSize, queueCreate(allocateSize));
	}

	public static IndexQueue of(int indexListSize) {

		int allocateSize = indexListSize;
		return new IndexQueue(indexListSize, queueCreate(allocateSize));
	}

	public static Queue<Integer> queueCreate(int allocateSize) {

		return new ArrayBlockingQueue<>(allocateSize);
	}

	// fill with contiguous indexes
	public void fill() {

		queue.clear();
		
		for (int i = 0; i < indexListSize; i++) {

			queue.add(i);
		}
	}

	public int size() {

		return queue.size();
	}

	// IndexInvalid if full
	public int consumeTry() {

		return queue.poll();
	}

	// give back to queue
	public void release(int index) {

		if (index == IndexInvalid) {

			// shouldn't happen
			return;
		}

		queue.add(index);
	}

	// put all entity back into queue
	public void clear() {

		queue.clear();
	}
}
