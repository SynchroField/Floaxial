package com.synchrofield.floaxial.central.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class VanillaSandBlock extends Block {

	public static final BlockBehaviour.Properties BehaveDefault = BlockBehaviour.Properties
			.of(Material.SAND, MaterialColor.SAND)
			.strength(0.5F)
			.sound(SoundType.SAND);

	public VanillaSandBlock(BlockBehaviour.Properties p_55968_) {

		super(p_55968_);
	}

	public static VanillaSandBlock of() {

		return new VanillaSandBlock(BehaveDefault);
	}
}