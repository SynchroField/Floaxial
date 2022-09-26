package com.synchrofield.library.configure;

import com.synchrofield.floaxial.central.configure.ProductConfigure;

import net.minecraft.resources.ResourceLocation;

public class BlockConfigure {

	public static final ResourceNameConfigure NameDefault = ResourceNameConfigure
			.of(ProductConfigure.Name, "example_block");

	public final ResourceNameConfigure name;

	protected BlockConfigure(ResourceNameConfigure name) {

		this.name = name;
	}

	public static BlockConfigure of() {

		return new BlockConfigure(NameDefault);
	}

	public static BlockConfigure of(ResourceNameConfigure name) {

		return new BlockConfigure(name);
	}

	public static BlockConfigure of(ResourceLocation resourceLocation) {

		return new BlockConfigure(ResourceNameConfigure.of(resourceLocation));
	}

	public static BlockConfigure ofVanilla(String path) {

		return new BlockConfigure(ResourceNameConfigure.ofVanilla(path));
	}

	public static BlockConfigure ofMod(String path) {

		return new BlockConfigure(ResourceNameConfigure.of(ProductConfigure.Name, path));
	}

	public BlockConfigure withName(ResourceNameConfigure name) {

		return new BlockConfigure(name);
	}

	public String namespace() {

		return name.namespace();
	}

	public String path() {

		return name.path();
	}
}
