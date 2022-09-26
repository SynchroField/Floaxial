package com.synchrofield.library.configure;

import com.synchrofield.floaxial.central.configure.ProductConfigure;

public class EntityConfigure {

	public static final ResourceNameConfigure NameDefault = ResourceNameConfigure
			.of(ProductConfigure.Name, "example_entity");

	public static final float SizeXzDefault = 0.98f;
	public static final float SizeYDefault = 0.98f;
	public static final int ClientViewDistance = 4;
	public static final int ClientUpdatePeriod = 20;

	public final ResourceNameConfigure name;
	public final float sizeXz;
	public final float sizeY;
	public final int clientViewDistance;
	public final int clientUpdatePeriod;

	protected EntityConfigure(ResourceNameConfigure name, float sizeXz, float sizeY,
			int clientViewDistance, int clientUpdatePeriod) {

		this.name = name;
		this.sizeXz = sizeXz;
		this.sizeY = sizeY;
		this.clientViewDistance = clientViewDistance;
		this.clientUpdatePeriod = clientUpdatePeriod;
	}

	public static EntityConfigure of() {

		return new EntityConfigure(NameDefault, SizeXzDefault, SizeYDefault, ClientViewDistance,
				ClientUpdatePeriod);
	}

	public static EntityConfigure of(ResourceNameConfigure name) {

		return new EntityConfigure(name, SizeXzDefault, SizeYDefault, ClientViewDistance,
				ClientUpdatePeriod);
	}

	public static EntityConfigure ofMod(String path) {

		return new EntityConfigure(ResourceNameConfigure.of(ProductConfigure.Name, path),
				SizeXzDefault, SizeYDefault, ClientViewDistance, ClientUpdatePeriod);
	}

	public EntityConfigure withName(ResourceNameConfigure name) {

		return new EntityConfigure(name, sizeXz, sizeY, clientViewDistance, clientUpdatePeriod);
	}

	public EntityConfigure withSizeXz(float sizeXz) {

		return new EntityConfigure(name, sizeXz, sizeY, clientViewDistance, clientUpdatePeriod);
	}

	public EntityConfigure withSizeY(float sizeY) {

		return new EntityConfigure(name, sizeXz, sizeY, clientViewDistance, clientUpdatePeriod);
	}

	public EntityConfigure withSizeAll(float sizeAll) {

		return new EntityConfigure(name, sizeAll, sizeAll, clientViewDistance, clientUpdatePeriod);
	}

	public EntityConfigure withClientViewDistance(int clientViewDistance) {

		return new EntityConfigure(name, sizeXz, sizeY, clientViewDistance, clientUpdatePeriod);
	}

	public EntityConfigure withClientUpdatePeriod(int clientUpdatePeriod) {

		return new EntityConfigure(name, sizeXz, sizeY, clientViewDistance, clientUpdatePeriod);
	}

	public String namespace() {

		return name.namespace();
	}

	public String path() {

		return name.path();
	}
}
