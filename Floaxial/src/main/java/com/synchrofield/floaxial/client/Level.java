package com.synchrofield.floaxial.client;

import com.synchrofield.floaxial.central.configure.ClientLevelConfigure;
import com.synchrofield.floaxial.central.configure.MaterialConfigureList;
import com.synchrofield.floaxial.central.network.DropPacket;
import com.synchrofield.floaxial.central.registry.CentralRegistry;
import com.synchrofield.floaxial.central.statistics.ClientDropStatistics;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.client.event.RenderLevelStageEvent;

public class Level {

	public final DropControl dropletControl;

	protected Level(DropControl dropletControl) {

		this.dropletControl = dropletControl;
	}

	public static Level of(ClientLevel level, ClientLevelConfigure configure,
			MaterialConfigureList commonMaterialConfigure, CentralRegistry registry) {

		DropControl dropletControl = DropControl.of(configure.drop, commonMaterialConfigure,
				registry);

		return new Level(dropletControl);
	}

	public void onTick(ClientLevel level) {

	}

	public void onRender(ClientLevel level, RenderLevelStageEvent event) {

		dropletControl.onRender(level, event);
	}

	public ClientDropStatistics statisticsDerive() {

		return dropletControl.statisticsDerive();
	}

	public void animateAdd(ClientLevel level, DropPacket packet) {

		dropletControl.moveAdd(level, packet);
	}
}
