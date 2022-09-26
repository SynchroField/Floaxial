package com.synchrofield.floaxial.central.registry;

import com.synchrofield.floaxial.central.block.SaltWaterFluid;
import com.synchrofield.floaxial.central.block.SaltWaterFluid.SaltWaterFluidFlowing;
import com.synchrofield.floaxial.central.block.SaltWaterFluid.SaltWaterFluidSource;
import com.synchrofield.floaxial.central.configure.ProductConfigure;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidRegistry {

	public final DeferredRegister<Fluid> registry = DeferredRegister.create(ForgeRegistries.FLUIDS,
			ProductConfigure.Name);

	public ResourceLocation SaltWaterStillName = new ResourceLocation(ProductConfigure.Name,
			"salt_water");
	public RegistryObject<FlowingFluid> SaltWaterStillFluidObject;

	public ResourceLocation SaltWaterStillTextureName = new ResourceLocation(ProductConfigure.Name,
			"block/salt_water");

	public ResourceLocation SaltWaterFlowingName = new ResourceLocation(ProductConfigure.Name,
			"salt_water_flow");
	public RegistryObject<FlowingFluid> SaltWaterFlowingFluidObject;

	public ResourceLocation SaltWaterFlowingTextureName = new ResourceLocation(
			ProductConfigure.Name, "block/salt_water");

	public int SaltWaterColor = 0xee6278dd;

	public FluidRegistry(IEventBus bus) {

		registrySetBus(bus);

		SaltWaterStillFluidObject = registry.register(SaltWaterStillName.getPath(),
				() -> new SaltWaterFluidSource(
						SaltWaterFluid.propertyListGetDefault(SaltWaterColor)));

		SaltWaterFlowingFluidObject = registry.register(SaltWaterFlowingName.getPath(),
				() -> new SaltWaterFluidFlowing(
						SaltWaterFluid.propertyListGetDefault(SaltWaterColor)));
	}

	protected void registrySetBus(IEventBus bus) {

		registry.register(bus);
	}

	public static FluidRegistry of(IEventBus bus) {

		return new FluidRegistry(bus);
	}
}
