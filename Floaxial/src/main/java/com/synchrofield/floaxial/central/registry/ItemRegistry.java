package com.synchrofield.floaxial.central.registry;

import com.synchrofield.floaxial.central.configure.ProductConfigure;
import com.synchrofield.floaxial.central.configure.RegistryConfigure;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// note items are registered before fluids by forge
public class ItemRegistry {

	public final DeferredRegister<Item> registry = DeferredRegister.create(ForgeRegistries.ITEMS,
			ProductConfigure.Name);

	public final RegistryObject<Item> SaltWaterBucketObject;

	public ItemRegistry(IEventBus bus, RegistryConfigure configure, BlockRegistry blockRegistry,
			FluidRegistry fluidRegistry) {

		registry.register(bus);

		registry.register(configure.VanillaSandBlock.path(),
				() -> new BlockItem(blockRegistry.VanillaSandBlockObject.get(),
						new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

		registry.register(configure.VanillaGravelBlock.path(),
				() -> new BlockItem(blockRegistry.VanillaGravelBlockObject.get(),
						new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

		registry.register(configure.GhostBlock.path(),
				() -> new BlockItem(blockRegistry.DropGhostObject.get(),
						new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

		registry.register(configure.PalmLeafXBlock.path(),
				() -> new BlockItem(blockRegistry.PalmLeafXObject.get(),
						new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

		registry.register(configure.PalmLeafZBlock.path(),
				() -> new BlockItem(blockRegistry.PalmLeafZObject.get(),
						new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

		registry.register(configure.PalmLeafXEndBlock.path(),
				() -> new BlockItem(blockRegistry.PalmLeafXEndObject.get(),
						new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

		registry.register(configure.SaltWaterBlock.path(),
				() -> new BlockItem(blockRegistry.SaltWaterRenderObject.get(),
						new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

		registry.register(configure.SaltWaterRenderBlock.path(),
				() -> new BlockItem(blockRegistry.SaltWaterRenderObject.get(),
						new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

		registry.register(configure.VanillaJungleLeafBlock.path(),
				() -> new BlockItem(blockRegistry.JungleLeafMobileObject.get(),
						new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

		registry.register(configure.VanillaJungleSaplingBlock.path(),
				() -> new BlockItem(blockRegistry.JungleSaplingObject.get(),
						new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

		SaltWaterBucketObject = registry.register(configure.SaltWaterBucketItem.path(),
				() -> new BucketItem(fluidRegistry.SaltWaterStillFluidObject,
						new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	}

	public static ItemRegistry of(IEventBus bus, RegistryConfigure configure,
			BlockRegistry blockRegistry, FluidRegistry fluidRegistry) {

		return new ItemRegistry(bus, configure, blockRegistry, fluidRegistry);
	}
}
