package com.synchrofield.floaxial.central.configure;

import com.synchrofield.floaxial.server.droplet.Droplet;

public class ServerMaterialConfigure {

	public static final int GhostProcessPeriodMinimum = 1;
	public static final int GhostProcessPeriodMaximum = 100;
	public static final int GhostProcessPeriodDefault = 1;

	public static final int MobileProcessPeriodMinimum = 1;
	public static final int MobileProcessPeriodMaximum = 100;
	public static final int MobileProcessPeriodDefault = 20;

	public static final int PacketSizeMinimum = 1;
	public static final int PacketSizeMaximum = 0xffff;
	public static final int PacketSizeDefault = 50000;

	public static final int LoadSectionSizeMinimum = 1;
	public static final int LoadSectionSizeMaximum = 1024 * 1024;
	public static final int LoadSectionSizeDefault = 1024;

	public static final int LoadPerTickMinimum = 1;
	public static final int LoadPerTickMaximum = 4096;
	public static final int LoadPerTickDefault = 1;

	public static final int ArchivePeriodMinimum = 1;
	public static final int ArchivePeriodMaximum = Droplet.TimeMaximum;
	public static final int ArchivePeriodDefault = 300;

	public static final int GhostLookaheadMinimum = 0;
	public static final int GhostLookaheadMaximum = 255;
	public static final int GhostLookaheadDefault = 20;

	public static final ServerMaterialConfigure Default0 = ServerMaterialConfigure.of();
	public static final ServerMaterialConfigure Default1 = ServerMaterialConfigure.of();
	public static final ServerMaterialConfigure Default2 = ServerMaterialConfigure.of();
	public static final ServerMaterialConfigure Default3 = ServerMaterialConfigure.of();
	public static final ServerMaterialConfigure Default4 = ServerMaterialConfigure.of();
	public static final ServerMaterialConfigure Default5 = ServerMaterialConfigure.of();
	public static final ServerMaterialConfigure Default6 = ServerMaterialConfigure.of();
	public static final ServerMaterialConfigure Default7 = ServerMaterialConfigure.of();

	public static final ServerMaterialConfigure DefaultValue[] = {

			Default0, Default1, Default2, Default3, Default4, Default5, Default6, Default7,
	};

	public final int ghostProcessPeriod;
	public final int mobileProcessPeriod;
	public final int packetSize;
	public final int archivePeriod;
	public final int loadSectionSize;
	public final int loadLocationPerTick;
	public final int ghostLookahead;

	protected ServerMaterialConfigure(int ghostProcessPeriod, int mobileProcessPeriod,
			int packetSize, int archivePeriod, int loadSectionSize, int loadLocationPerTick,
			int ghostLookahead) {

		this.ghostProcessPeriod = ghostProcessPeriod;
		this.mobileProcessPeriod = mobileProcessPeriod;
		this.packetSize = packetSize;
		this.archivePeriod = archivePeriod;
		this.loadSectionSize = loadSectionSize;
		this.loadLocationPerTick = loadLocationPerTick;
		this.ghostLookahead = ghostLookahead;
	}

	public static ServerMaterialConfigure of() {

		return new ServerMaterialConfigure(GhostProcessPeriodDefault, MobileProcessPeriodDefault,
				PacketSizeDefault, ArchivePeriodMaximum, LoadSectionSizeDefault, LoadPerTickDefault,
				GhostLookaheadDefault);
	}

	public ServerMaterialConfigure withGhostProcessPeriod(int ghostProcessPeriod) {

		return new ServerMaterialConfigure(ghostProcessPeriod, mobileProcessPeriod, packetSize,
				archivePeriod, loadSectionSize, loadLocationPerTick, ghostLookahead);
	}

	public ServerMaterialConfigure withMobileProcessPeriod(int mobileProcessPeriod) {

		return new ServerMaterialConfigure(ghostProcessPeriod, mobileProcessPeriod, packetSize,
				archivePeriod, loadSectionSize, loadLocationPerTick, ghostLookahead);
	}

	public ServerMaterialConfigure withPacketSize(int packetSize) {

		return new ServerMaterialConfigure(ghostProcessPeriod, mobileProcessPeriod, packetSize,
				archivePeriod, loadSectionSize, loadLocationPerTick, ghostLookahead);
	}

	public ServerMaterialConfigure withArchivePeriod(int archivePeriod) {

		return new ServerMaterialConfigure(ghostProcessPeriod, mobileProcessPeriod, packetSize,
				archivePeriod, loadSectionSize, loadLocationPerTick, ghostLookahead);
	}

	public ServerMaterialConfigure withLoadSectionSize(int loadSectionSize) {

		return new ServerMaterialConfigure(ghostProcessPeriod, mobileProcessPeriod, packetSize,
				archivePeriod, loadSectionSize, loadLocationPerTick, ghostLookahead);
	}

	public ServerMaterialConfigure withLoadQueueLocationPerTick(int loadQueueLocationPerTick) {

		return new ServerMaterialConfigure(ghostProcessPeriod, mobileProcessPeriod, packetSize,
				archivePeriod, loadSectionSize, loadQueueLocationPerTick, ghostLookahead);
	}

	public ServerMaterialConfigure withGhostLookahead(int ghostLookahead) {

		return new ServerMaterialConfigure(ghostProcessPeriod, mobileProcessPeriod, packetSize,
				archivePeriod, loadSectionSize, loadLocationPerTick, ghostLookahead);
	}
}
