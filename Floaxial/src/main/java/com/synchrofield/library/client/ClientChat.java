package com.synchrofield.library.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;

public class ClientChat {

	protected ClientChat() {

	}

	public static ClientChat of() {

		return new ClientChat();
	}

	// adds text only on client server doesn't see it
	public void chatAdd(String text) {

		Style style = Style.EMPTY.withColor(0xffffffff);

		Component component = new TextComponent(text).withStyle(style);

		Minecraft minecraft = Minecraft.getInstance();
		minecraft.player.displayClientMessage(component, false);
	}

	// adds text only on client server doesn't see it
	public void chatAdd(String text, int color) {

		Style style = Style.EMPTY.withColor(color);

		Component component = new TextComponent(text).withStyle(style);

		Minecraft minecraft = Minecraft.getInstance();
		minecraft.player.displayClientMessage(component, false);
	}
}
