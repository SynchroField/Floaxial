package com.synchrofield.library.network;

import net.minecraft.network.FriendlyByteBuf;

public class Network {

	// dword
	public static int SizeInteger = 1;
	public static int SizeLong = 2;
	public static int SizeFloat = 1;
	public static int SizeDouble = 2;

	public static int[] integerArrayFromBuffer(FriendlyByteBuf buffer) {

		int size = buffer.readInt();

		assert size >= 0;

		int[] list = new int[size];

		assert list != null;

		for (int i = 0; i < list.length; i++) {

			list[i] = buffer.readInt();
		}

		return list;
	}

	public static void integerArrayToBuffer(int listSize, int[] list, FriendlyByteBuf buffer) {

		assert list.length >= 0;

		buffer.writeInt(listSize);

		for (int i = 0; i < list.length; i++) {

			buffer.writeInt(list[i]);
		}
	}
}
