package com.synchrofield.library.configure;

import net.minecraft.resources.ResourceLocation;

// name of a game object
public class ResourceNameConfigure {

	public static final String NamespaceDefault = "minecraft";
	public static final String PathDefault = "example_game_object";
	public static final ResourceLocation ResourceLocationDefault = new ResourceLocation(
			NamespaceDefault, PathDefault);

	public final ResourceLocation resourceLocation;

	protected ResourceNameConfigure(ResourceLocation resourceLocation) {

		assert resourceLocation != null;
		assert resourceLocation.getNamespace() != null;
		assert resourceLocation.getPath() != null;

		this.resourceLocation = resourceLocation;
	}

	public static ResourceNameConfigure of() {

		return new ResourceNameConfigure(ResourceLocationDefault);
	}

	public static ResourceNameConfigure of(ResourceLocation resourceLocation) {

		return new ResourceNameConfigure(resourceLocation);
	}

	public static ResourceNameConfigure of(String namespace, String path) {

		return new ResourceNameConfigure(new ResourceLocation(namespace, path));
	}

	public static ResourceNameConfigure ofVanilla(String path) {

		return new ResourceNameConfigure(new ResourceLocation("minecraft", path));
	}

	public ResourceNameConfigure withName(ResourceLocation name) {

		return new ResourceNameConfigure(name);
	}

	public ResourceNameConfigure withNamespaceVanilla(String path) {

		ResourceLocation name = new ResourceLocation("minecraft", path);
		return new ResourceNameConfigure(name);
	}

	public String namespace() {

		return resourceLocation.getNamespace();
	}

	public String path() {

		return resourceLocation.getPath();
	}
}
