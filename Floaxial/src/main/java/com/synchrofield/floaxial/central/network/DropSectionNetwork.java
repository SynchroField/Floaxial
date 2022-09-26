package com.synchrofield.floaxial.central.network;

import com.synchrofield.floaxial.central.droplet.MaterialTable;
import com.synchrofield.library.network.Network;

import net.minecraft.core.SectionPos;
import net.minecraft.network.FriendlyByteBuf;

public class DropSectionNetwork {

	// that should keep it within 64k packet size for now
	public static int MoveListSizeMaximum = 64000 / 4;

	// 1 extra for move list size sent before the list itself
	public static int HeaderSize = Network.SizeLong + Network.SizeInteger
			+ Network.SizeInteger;

	public SectionPos location;
	public int material;

	// used size
	public int moveListSize;

	public int[] moveList;

	protected DropSectionNetwork() {

	}

	protected DropSectionNetwork(SectionPos location, int material, int moveListSize,
			int[] moveList) {

		assert MaterialTable.indexCheck(material);

		assert moveList.length > 0 : "Empty section.";
		assert moveList.length <= MoveListSizeMaximum;

		this.location = location;
		this.material = material;

		this.moveListSize = moveListSize;
		this.moveList = moveList;
	}

	public static DropSectionNetwork ofEmpty() {

		return new DropSectionNetwork();
	}

	public static DropSectionNetwork of(SectionPos location, int material, int[] moveList) {

		int moveListSize = 0;
		return new DropSectionNetwork(location, material, moveListSize, moveList);
	}

	public static DropSectionNetwork ofBuffer(FriendlyByteBuf buffer) {

		DropSectionNetwork section = DropSectionNetwork.ofEmpty();

		section.location = buffer.readSectionPos();
		section.material = buffer.readInt();

		section.moveList = Network.integerArrayFromBuffer(buffer);
		section.moveListSize = section.moveList.length;

		return section;
	}

	// dword
	public int networkSize() {

		return (HeaderSize + moveListSize * 4);
	}

	public void toBuffer(FriendlyByteBuf buffer) {

		buffer.writeSectionPos(location);
		buffer.writeInt(material);

		Network.integerArrayToBuffer(moveListSize, moveList, buffer);
	}

	public void moveClear(int move) {

		moveListSize = 0;
	}

	public void moveAdd(int move) {

		/// return false and make new packet
		assert moveListSize < moveList.length;

		moveList[moveListSize] = move;

		moveListSize++;
	}
}
