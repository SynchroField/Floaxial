package com.synchrofield.floaxial.central.registry;

import com.synchrofield.floaxial.central.configure.ProductConfigure;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityRegistry {

	public static final DeferredRegister<EntityType<?>> registry = DeferredRegister
			.create(ForgeRegistries.ENTITIES, ProductConfigure.Name);

	public EntityRegistry(IEventBus bus) {

		registrySetBus(bus);
	}

	public static EntityRegistry of(IEventBus bus) {

		return new EntityRegistry(bus);
	}

	protected void registrySetBus(IEventBus bus) {

		registry.register(bus);
	}
}
