package com.synchrofield.floaxial.client.render;

import com.synchrofield.library.list.IndexQueue;

// Uses a flat array to store animate objects.
// The index queue holds all free slots of the array.
public class MovePool {

	public final int allocateSize;
	public final ClientMove[] animateList;
	public final IndexQueue indexQueue;

	protected MovePool(int allocateSize, ClientMove[] animateList, IndexQueue indexQueue) {

		this.allocateSize = allocateSize;
		this.animateList = animateList;
		this.indexQueue = indexQueue;
	}

	public static MovePool of(int allocateSize) {

		ClientMove[] animateList = new ClientMove[allocateSize];
		IndexQueue indexQueue = IndexQueue.of(allocateSize);

		return new MovePool(allocateSize, animateList, indexQueue);
	}

	// put all index into queue and fill array with dormant animate item
	public void animateListFill() {

		// queue
		indexQueue.fill();

		// array
		for (int i = 0; i < allocateSize; i++) {

			animateList[i] = ClientMove.of();

			animateList[i].existIs = false;
		}
	}

	public int freeSize() {

		return indexQueue.size();
	}

	public int useSize() {

		return allocateSize - indexQueue.size();
	}

	public boolean fullIs() {

		return freeSize() <= 0;
	}

	// caller should clear dormant when ready
	public int consumeTry() {

		return indexQueue.consumeTry();
	}

	public void release(int poolIndex) {

		if (poolIndex == IndexQueue.IndexInvalid) {

			// shouldn't happen
			return;
		}

		indexQueue.release(poolIndex);
	}
}
