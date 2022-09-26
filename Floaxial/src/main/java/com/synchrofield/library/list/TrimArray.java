package com.synchrofield.library.list;

import com.synchrofield.library.math.MathUtility;

// array that is trimmed as you iterate it by shifting items down
//
// caller should update free index as it iterates
public class TrimArray {

	public static int InvalidIndex = -1;

	// this is used to replace element that move
	public final int removeValue;

	public final int initialAllocateSize;

	public int freeIndex;

	// number of slots used
	public int useSize;

	public int[] list;

	protected TrimArray(int[] list, int removeValue) {

		assert list.length > 0;

		this.initialAllocateSize = list.length;
		this.list = list;

		this.removeValue = removeValue;

		this.useSize = 0;
		this.freeIndex = InvalidIndex;
	}

	protected static TrimArray of(int initialAllocateSize, int removeValue) {

		int[] list = new int[initialAllocateSize];

		return new TrimArray(list, removeValue);
	}

	public void clear() {

		reallocate(initialAllocateSize);

		useSize = 0;
		freeIndex = InvalidIndex;
	}

	public int allocateSize() {

		return list.length;
	}

	public boolean indexCheck(int index) {

		// impossible to index something if list is empty
		return (useSize == 0) ? false : MathUtility.rangeCheck(index, 0, useSize);
	}

	public boolean freeIndexCheck() {

		if (useSize == 0) {

			return freeIndex == InvalidIndex;
		}
		else {

			return freeIndex < useSize;
		}
	}

	// attempt to move element down 
	// new index otherwise given index
	public int tryShiftElement(int sourceIndex) {

		assert freeIndexCheck();
		assert indexCheck(sourceIndex);

		if (freeIndex == InvalidIndex) {

			// no space to move into
			return sourceIndex;
		}
		else {

			int destinationIndex = freeIndex;

			// shift down
			list[destinationIndex] = list[sourceIndex];

			// update free
			if (sourceIndex == useSize - 1) {

				// last item so reduce size
				useSize--;

				freeIndex = InvalidIndex;
			}
			else {

				// clear old
				list[sourceIndex] = removeValue;

				// just use source even though might have missed an early free slot
				freeIndex = sourceIndex;
			}

			return destinationIndex;
		}
	}

	// enough to fit and keeps array a power 2 
	//
	// in future allow a max size for the doubling so it doesn't get out of control
	// and probably reallocate before it's 100% full
	public void tryReallocate(int newSize) {

		if (newSize >= list.length) {

			reallocate(newSize);
		}
	}

	public void reallocate(int newSize) {

		int[] newList = new int[MathUtility.power2Above(newSize)];

		System.arraycopy(list, 0, newList, 0, useSize);

		list = newList;
	}

	public void addSafe(int item) {

		// reallocate if the new list size demands it
		tryReallocate(useSize + 1);

		// always add to the end (for now, later use free index)
		list[useSize] = item;

		useSize++;
	}

	// when caller removes an item this will set as free index
	// will modify useSize
	public void removeUpdateFree(int removeIndex) {

		assert useSize > 0 : "Remove from empty list.";
		assert indexCheck(removeIndex);

		if (removeIndex == (useSize - 1)) {

			// last element seems to need special case
			useSize--;
			freeIndex = InvalidIndex;
			return;
		}

		if (freeIndex == InvalidIndex) {

			freeIndex = removeIndex;
		}
		else {

			// only if better than current one
			if (removeIndex < freeIndex) {

				freeIndex = removeIndex;
			}
		}

		assert freeIndexCheck();
	}
}
