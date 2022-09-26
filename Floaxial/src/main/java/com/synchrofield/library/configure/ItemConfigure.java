package com.synchrofield.library.configure;

import com.synchrofield.floaxial.central.configure.ProductConfigure;

import net.minecraft.resources.ResourceLocation;

public class ItemConfigure {

	public static final ResourceNameConfigure NameDefault = ResourceNameConfigure.of();

	public final ResourceNameConfigure name;

	protected ItemConfigure(ResourceNameConfigure name) {

		this.name = name;
	}

	public static ItemConfigure of() {

		return new ItemConfigure(NameDefault);
	}

	public static ItemConfigure of(ResourceNameConfigure name) {

		return new ItemConfigure(name);
	}

	public static ItemConfigure of(ResourceLocation resourceLocation) {

		return new ItemConfigure(ResourceNameConfigure.of(resourceLocation));
	}

	public static ItemConfigure ofVanilla(String path) {

		return new ItemConfigure(ResourceNameConfigure.ofVanilla(path));
	}

	public static ItemConfigure ofMod(String path) {

		return new ItemConfigure(ResourceNameConfigure.of(ProductConfigure.Name, path));
	}

	public ItemConfigure withName(ResourceNameConfigure name) {

		return new ItemConfigure(name);
	}

	public String namespace() {

		return name.namespace();
	}

	public String path() {

		return name.path();
	}
}
