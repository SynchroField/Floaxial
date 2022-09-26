package com.synchrofield.floaxial.client;

import com.synchrofield.floaxial.central.configure.ClientConfigure;
import com.synchrofield.floaxial.central.configure.MaterialConfigureList;
import com.synchrofield.floaxial.central.network.DropPacket;
import com.synchrofield.floaxial.central.network.GameResetPacket;
import com.synchrofield.floaxial.central.network.ReloadPacket;
import com.synchrofield.floaxial.central.network.StatisticsPacket;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;

// dedicated server
public class ClientDedicated extends Client {

	public static ClientDedicated of() {

		return new ClientDedicated();
	}

	@Override
	public void receive(ReloadPacket packet) {

	}

	@Override
	public void receive(GameResetPacket packet) {

	}

	@Override
	public void receive(DropPacket packet) {

	}

	@Override
	public void receive(StatisticsPacket packet) {

	}

	@Override
	public void onConfigure(ClientLevel level, ClientConfigure configure,
			MaterialConfigureList commonMaterialConfigure) {

	}

	@Override
	public void reset(ClientLevel level) {

	}

	@Override
	public void onRender(ClientLevel level, RenderLevelStageEvent event) {

	}

	@Override
	public void onOverlayRender(RenderBlockOverlayEvent event) {

	}

	@Override
	public void onFogRender(RenderFogEvent event) {

	}

	@Override
	public void onFogColor(FogColors event) {

	}
}
