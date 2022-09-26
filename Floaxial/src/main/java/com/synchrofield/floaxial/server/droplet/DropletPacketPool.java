package com.synchrofield.floaxial.server.droplet;

import com.synchrofield.floaxial.central.network.DropPacket;
import com.synchrofield.floaxial.central.network.DropSectionNetwork;

import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class DropletPacketPool {

	// just have a single packet for now and send after each section
	public DropPacket currentPacket;

	public DropSectionNetwork currentSection;

	public SimpleChannel channel;

	public int movePerSectionMaximum;

	protected DropletPacketPool(SimpleChannel channel, int movePerSectionMaximum) {

		this.channel = channel;
		this.movePerSectionMaximum = movePerSectionMaximum;

		currentPacket = null;
		currentSection = null;
	}

	public static DropletPacketPool of(SimpleChannel channel, int movePerSectionMaximum) {

		return new DropletPacketPool(channel, movePerSectionMaximum);
	}

	public void clear() {
		
		currentPacket = null;
		currentSection = null;
	}
	public DropPacket packetCreate(ServerLevel level, SectionPos sectionLocation, int material) {

		return DropPacket.ofSection(level, sectionLocation, material, movePerSectionMaximum);
	}

	public DropPacket currentPacket() {

		return currentPacket;
	}

	public DropSectionNetwork currentSection() {

		return currentSection;
	}

	public long currentPacketTime() {

		return currentPacket.createTime;
	}

	public void send() {

		channel.send(PacketDistributor.ALL.noArg(), currentPacket);
	}
}
