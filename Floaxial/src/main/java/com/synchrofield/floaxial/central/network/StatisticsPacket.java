package com.synchrofield.floaxial.central.network;

import javax.annotation.Nullable;

import com.synchrofield.floaxial.central.droplet.MaterialTable;
import com.synchrofield.floaxial.central.statistics.ServerStatistics;
import com.synchrofield.floaxial.server.rule.RuleTable;

import net.minecraft.network.FriendlyByteBuf;

public class StatisticsPacket {

	@Nullable
	public ServerStatistics server;

	protected StatisticsPacket(ServerStatistics server) {

		this.server = server;
	}

	public static StatisticsPacket ofEmpty() {

		return new StatisticsPacket(null);
	}

	public static StatisticsPacket of(ServerStatistics server) {

		return new StatisticsPacket(server);
	}

	public static StatisticsPacket fromBuffer(FriendlyByteBuf buffer) {

		StatisticsPacket packet = StatisticsPacket.ofEmpty();

		packet.server = ServerStatistics.of();

		packet.server.level.tick = buffer.readLong();

		packet.server.level.drop.loadSection = buffer.readInt();
		packet.server.level.drop.loadSectionFail = buffer.readInt();
		packet.server.level.drop.loadSectionUse = buffer.readInt();
		packet.server.level.drop.processLevel = buffer.readInt();
		packet.server.level.drop.processSection = buffer.readInt();
		packet.server.level.drop.processMaterialSection = buffer.readInt();
		packet.server.level.drop.networkSendByte = buffer.readInt();
		packet.server.level.drop.networkSendPacket = buffer.readInt();
		packet.server.level.drop.gapQueueSize = buffer.readInt();
		packet.server.level.drop.gapAddPerTickMean = buffer.readFloat();
		packet.server.level.drop.gapProcessPerTickMean = buffer.readFloat();

		for (int materialIndex = 0; materialIndex < MaterialTable.Size; materialIndex++) {

			packet.server.level.drop.material[materialIndex].loadProcessDrop = buffer.readInt();
			packet.server.level.drop.material[materialIndex].processSection = buffer.readInt();
			packet.server.level.drop.material[materialIndex].processGhost = buffer.readInt();
			packet.server.level.drop.material[materialIndex].processDrop = buffer.readInt();
			packet.server.level.drop.material[materialIndex].ghostProcessPerTickMean = buffer
					.readFloat();
			packet.server.level.drop.material[materialIndex].dropletProcessPerTickMean = buffer
					.readFloat();
			packet.server.level.drop.material[materialIndex].movePerTickMean = buffer.readFloat();
			packet.server.level.drop.material[materialIndex].levelGhost = buffer.readInt();
			packet.server.level.drop.material[materialIndex].levelDrop = buffer.readInt();
			packet.server.level.drop.material[materialIndex].networkSendByte = buffer.readInt();
			packet.server.level.drop.material[materialIndex].networkSendPacket = buffer.readInt();
			packet.server.level.drop.material[materialIndex].networkSendSection = buffer.readInt();
			packet.server.level.drop.material[materialIndex].networkSendDrop = buffer.readInt();
			packet.server.level.drop.material[materialIndex].networkSendDropFail = buffer.readInt();
			packet.server.level.drop.material[materialIndex].ghostAllocate = buffer.readInt();
			packet.server.level.drop.material[materialIndex].dropAllocate = buffer.readInt();
		}

		for (int ruleIndex = 0; ruleIndex < RuleTable.ListSize; ruleIndex++) {

			packet.server.level.rule.list[ruleIndex].sectionListSize = buffer.readInt();
			packet.server.level.rule.list[ruleIndex].sectionPerTick = buffer.readInt();
			packet.server.level.rule.list[ruleIndex].samplePerTickMean = buffer.readFloat();
		}

		return packet;
	}

	public static void toBuffer(StatisticsPacket packet, FriendlyByteBuf buffer) {

		buffer.writeLong(packet.server.level.tick);

		buffer.writeInt(packet.server.level.drop.loadSection);
		buffer.writeInt(packet.server.level.drop.loadSectionFail);
		buffer.writeInt(packet.server.level.drop.loadSectionUse);
		buffer.writeInt(packet.server.level.drop.processLevel);
		buffer.writeInt(packet.server.level.drop.processSection);
		buffer.writeInt(packet.server.level.drop.processMaterialSection);
		buffer.writeInt(packet.server.level.drop.networkSendByte);
		buffer.writeInt(packet.server.level.drop.networkSendPacket);
		buffer.writeInt(packet.server.level.drop.gapQueueSize);
		buffer.writeFloat(packet.server.level.drop.gapAddPerTickMean);
		buffer.writeFloat(packet.server.level.drop.gapProcessPerTickMean);

		for (int materialIndex = 0; materialIndex < MaterialTable.Size; materialIndex++) {

			buffer.writeInt(packet.server.level.drop.material[materialIndex].loadProcessDrop);
			buffer.writeInt(packet.server.level.drop.material[materialIndex].processSection);
			buffer.writeInt(packet.server.level.drop.material[materialIndex].processGhost);
			buffer.writeInt(packet.server.level.drop.material[materialIndex].processDrop);
			buffer.writeFloat(
					packet.server.level.drop.material[materialIndex].ghostProcessPerTickMean);
			buffer.writeFloat(
					packet.server.level.drop.material[materialIndex].dropletProcessPerTickMean);
			buffer.writeFloat(packet.server.level.drop.material[materialIndex].movePerTickMean);
			buffer.writeInt(packet.server.level.drop.material[materialIndex].levelGhost);
			buffer.writeInt(packet.server.level.drop.material[materialIndex].levelDrop);
			buffer.writeInt(packet.server.level.drop.material[materialIndex].networkSendByte);
			buffer.writeInt(packet.server.level.drop.material[materialIndex].networkSendPacket);
			buffer.writeInt(packet.server.level.drop.material[materialIndex].networkSendSection);
			buffer.writeInt(packet.server.level.drop.material[materialIndex].networkSendDrop);
			buffer.writeInt(packet.server.level.drop.material[materialIndex].networkSendDropFail);
			buffer.writeInt(packet.server.level.drop.material[materialIndex].ghostAllocate);
			buffer.writeInt(packet.server.level.drop.material[materialIndex].dropAllocate);
		}

		for (int ruleIndex = 0; ruleIndex < RuleTable.ListSize; ruleIndex++) {

			buffer.writeInt(packet.server.level.rule.list[ruleIndex].sectionListSize);
			buffer.writeInt(packet.server.level.rule.list[ruleIndex].sectionPerTick);
			buffer.writeFloat(packet.server.level.rule.list[ruleIndex].samplePerTickMean);
		}
	}
}
