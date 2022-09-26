package com.synchrofield.floaxial.central.registry;

import com.synchrofield.floaxial.central.configure.CentralConfigure;
import com.synchrofield.floaxial.central.configure.RegistryConfigure;

import net.minecraftforge.eventbus.api.IEventBus;

public class CentralRegistry {

	protected static CentralRegistry instance;

	public final BlockRegistry block;
	public final ItemRegistry item;
	public final FluidRegistry fluid;
	public final EntityRegistry entity;
	public final GeneratorRegistry generate;
	public final TreeRegistry tree;
	public final CommandRegistry command;

	protected CentralRegistry(BlockRegistry block, ItemRegistry item, FluidRegistry fluid,
			EntityRegistry entity, GeneratorRegistry generate, TreeRegistry tree,
			CommandRegistry command) {

		assert instance == null;
		CentralRegistry.instance = this;

		this.block = block;
		this.item = item;
		this.fluid = fluid;
		this.entity = entity;
		this.generate = generate;
		this.tree = tree;
		this.command = command;
	}

	public static CentralRegistry of(IEventBus bus, RegistryConfigure configure) {

		FluidRegistry fluid = FluidRegistry.of(bus);
		BlockRegistry block = BlockRegistry.of(bus, configure, fluid);
		ItemRegistry item = ItemRegistry.of(bus, configure, block, fluid);
		EntityRegistry entity = EntityRegistry.of(bus);
		GeneratorRegistry generate = GeneratorRegistry.of();
		TreeRegistry tree = TreeRegistry.of();
		CommandRegistry command = CommandRegistry.of();

		return new CentralRegistry(block, item, fluid, entity, generate, tree, command);
	}

	public static CentralRegistry instance() {

		assert instance != null;

		return instance;
	}

	public void onConfigure(CentralConfigure centralConfigure) {

		block.onConfigure(centralConfigure.client.level.drop.ghostIsVisible);
	}
}
