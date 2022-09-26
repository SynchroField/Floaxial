package com.synchrofield.floaxial.central.configure;

import com.synchrofield.floaxial.central.droplet.MaterialTable;

public class ServerDropletConfigure {

	public static final int GapSizeMinimum = 1;
	public static final int GapSizeMaximum = 1024 * 1024;
	public static final int GapSizeDefault = 1024;

	public static final int GapPerTickMinimum = 1;
	public static final int GapPerTickMaximum = 4096;
	public static final int GapPerTickDefault = 1;

	public final int gapSize;
	public final int gapPerTick;

	public final ServerMaterialConfigure[] material;

	protected ServerDropletConfigure(int gapSize, int gapPerTick,
			ServerMaterialConfigure[] material) {

		this.gapSize = gapSize;
		this.gapPerTick = gapPerTick;
		this.material = material;
	}

	public static ServerDropletConfigure of() {

		ServerMaterialConfigure[] material = new ServerMaterialConfigure[MaterialTable.Size];
		for (int i = 0; i < MaterialTable.Size; i++) {

			material[i] = ServerMaterialConfigure.of();
		}

		return new ServerDropletConfigure(GapSizeDefault, GapPerTickDefault, material);
	}

	public ServerDropletConfigure withGapSize(int gapSize) {

		return new ServerDropletConfigure(gapSize, gapPerTick, material);
	}

	public ServerDropletConfigure withGapPerTick(int gapProcessPerTickMaximum) {

		return new ServerDropletConfigure(gapSize, gapPerTick, material);
	}

	public ServerDropletConfigure withMaterial(ServerMaterialConfigure[] material) {

		return new ServerDropletConfigure(gapSize, gapPerTick, material);
	}
}
