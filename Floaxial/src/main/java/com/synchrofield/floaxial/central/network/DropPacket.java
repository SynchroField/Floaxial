package com.synchrofield.floaxial.central.network;

import com.synchrofield.library.network.Network;

import net.minecraft.core.SectionPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;

public class DropPacket {

	// serialize size dword
	public static int HeaderSize = Network.SizeLong;

	public long createTime;

	// later have a list of these
	public DropSectionNetwork section;

	public DropPacket() {

	}

	protected DropPacket(long createTime, DropSectionNetwork section) {

		assert createTime >= 0;

		this.createTime = createTime;
		this.section = section;
	}

	public static DropPacket ofEmpty() {

		return new DropPacket();
	}

	public static DropPacket ofSection(ServerLevel level, SectionPos sectionLocation, int material,
			int moveListSize) {

		long createTime = level.getGameTime();

		int[] moveList = new int[moveListSize];
		DropSectionNetwork section = DropSectionNetwork.of(sectionLocation, material, moveList);

		return new DropPacket(createTime, section);
	}

	public int networkSize() {

		return (HeaderSize * 4) + section.networkSize();
	}

	public static DropPacket fromBuffer(FriendlyByteBuf buffer) {

		DropPacket packet = DropPacket.ofEmpty();

		packet.createTime = buffer.readLong();
		packet.section = DropSectionNetwork.ofBuffer(buffer);

		return packet;
	}

	public static void toBuffer(DropPacket packet, FriendlyByteBuf buffer) {

		buffer.writeLong(packet.createTime);
		packet.section.toBuffer(buffer);
	}
}
