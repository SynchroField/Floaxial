package com.synchrofield.floaxial.central.statistics;

public class IntegerHistory {

	public int index;
	public final int[] list;

	protected IntegerHistory(int[] list) {

		assert list.length > 0;

		this.list = list;

		index = 0;
	}

	public static IntegerHistory of(int size) {

		int[] list = new int[size];

		return new IntegerHistory(list);
	}

	public void clear() {

		index = 0;

		for (int i = 0; i < list.length; i++) {

			list[i] = 0;
		}
	}

	public int total() {

		int result = 0;

		for (int item : list) {

			result += item;
		}

		return result;
	}

	public float mean() {

		return (float) total() / (float) size();
	}

	public int size() {

		return list.length;
	}

	public void append(int item) {

		index++;
		if (index >= size()) {

			// wrap
			index = 0;
		}

		list[index] = item;
	}

	public void increment() {

		list[index]++;
	}
}
