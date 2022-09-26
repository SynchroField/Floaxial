package com.synchrofield.floaxial.client;

import com.synchrofield.floaxial.central.configure.ClientConfigure;
import com.synchrofield.floaxial.central.configure.MaterialConfigureList;
import com.synchrofield.floaxial.central.network.ClientNetworkReceive;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;

public abstract class Client implements ClientNetworkReceive {

	public abstract void onConfigure(ClientLevel level, ClientConfigure configure,
			MaterialConfigureList commonMaterialConfigure);

	public abstract void reset(ClientLevel level);

	public abstract void onTick(ClientLevel level);

	public abstract void onRender(ClientLevel level, RenderLevelStageEvent event);

	public abstract void onFogRender(EntityViewRenderEvent.RenderFogEvent event);
	public abstract void onFogColor(EntityViewRenderEvent.FogColors event);

	public abstract void onOverlayRender(RenderBlockOverlayEvent event);

	public ClientLevel clientLevel() {

		Minecraft minecraft = Minecraft.getInstance();
		return minecraft.level;
	}
}
