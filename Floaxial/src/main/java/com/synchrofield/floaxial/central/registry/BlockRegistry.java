package com.synchrofield.floaxial.central.registry;

import com.synchrofield.floaxial.central.block.GhostBlock;
import com.synchrofield.floaxial.central.block.VanillaJungleLeafBlock;
import com.synchrofield.floaxial.central.block.VanillaJungleSaplingBlock;
import com.synchrofield.floaxial.central.block.MarkBlock;
import com.synchrofield.floaxial.central.block.PalmLeafXBlock;
import com.synchrofield.floaxial.central.block.PalmLeafXEndBlock;
import com.synchrofield.floaxial.central.block.PalmLeafZBlock;
import com.synchrofield.floaxial.central.block.SaltWaterBlock;
import com.synchrofield.floaxial.central.block.SaltWaterRenderBlock;
import com.synchrofield.floaxial.central.block.VanillaGravelBlock;
import com.synchrofield.floaxial.central.block.VanillaSandBlock;
import com.synchrofield.floaxial.central.configure.ProductConfigure;
import com.synchrofield.floaxial.central.configure.RegistryConfigure;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {

	public final DeferredRegister<Block> Registry = DeferredRegister.create(ForgeRegistries.BLOCKS,
			ProductConfigure.Name);

	public final RegistryObject<Block> VanillaSandBlockObject;
	public final RegistryObject<Block> VanillaGravelBlockObject;
	public final RegistryObject<Block> MarkObject;
	public final RegistryObject<Block> DropGhostObject;
	public final RegistryObject<Block> PalmLeafXObject;
	public final RegistryObject<Block> PalmLeafZObject;
	public final RegistryObject<Block> PalmLeafXEndObject;
	public final RegistryObject<Block> SaltWaterRenderObject;
	public final RegistryObject<Block> JungleSaplingObject;
	public final RegistryObject<Block> JungleLeafMobileObject;
	public final RegistryObject<LiquidBlock> SaltWaterObject;

	protected BlockRegistry(IEventBus bus, RegistryConfigure configure,
			FluidRegistry fluidRegistry) {

		registrySetBus(bus);

		VanillaSandBlockObject = Registry.register(configure.VanillaSandBlock.path(),
				() -> VanillaSandBlock.of());

		VanillaGravelBlockObject = Registry.register(configure.VanillaGravelBlock.path(),
				() -> VanillaGravelBlock.of());

		MarkObject = Registry.register(configure.MarkBlock.path(), () -> MarkBlock.of());

		DropGhostObject = Registry.register(configure.GhostBlock.path(),
				() -> new GhostBlock(GhostBlock.BehaveDefault, false));

		PalmLeafXObject = Registry.register(configure.PalmLeafXBlock.path(),
				() -> PalmLeafXBlock.of());

		PalmLeafZObject = Registry.register(configure.PalmLeafZBlock.path(),
				() -> PalmLeafZBlock.of());

		PalmLeafXEndObject = Registry.register(configure.PalmLeafXEndBlock.path(),
				() -> PalmLeafXEndBlock.of());

		SaltWaterObject = Registry.register(configure.SaltWaterBlock.path(),
				() -> new SaltWaterBlock(fluidRegistry.SaltWaterStillFluidObject,
						SaltWaterBlock.BehaveDefault));

		JungleSaplingObject = Registry.register(configure.VanillaJungleSaplingBlock.path(),
				() -> VanillaJungleSaplingBlock.of());

		JungleLeafMobileObject = Registry.register(configure.VanillaJungleLeafBlock.path(),
				() -> VanillaJungleLeafBlock.of());

		SaltWaterRenderObject = Registry.register(configure.SaltWaterRenderBlock.path(),
				() -> SaltWaterRenderBlock.of());
	}

	public static BlockRegistry of(IEventBus bus, RegistryConfigure configure,
			FluidRegistry fluidRegistry) {

		return new BlockRegistry(bus, configure, fluidRegistry);
	}

	public void onConfigure(boolean ghostIsVisible) {

		((GhostBlock) DropGhostObject.get()).onConfigure(ghostIsVisible);
	}

	protected void registrySetBus(IEventBus bus) {

		Registry.register(bus);
	}

	public Block blockGet(ResourceLocation location) {

		return ForgeRegistries.BLOCKS.getValue(location);
	}

	public void onRegisterColor(BlockColors blockColors) {

		blockColors.register(VanillaJungleLeafBlock.Color, JungleLeafMobileObject.get());
	}
}