package com.synchrofield.floaxial.central.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class VanillaGravelBlock extends Block {

	public static final BlockBehaviour.Properties BehaveDefault = BlockBehaviour.Properties
			.of(Material.SAND, MaterialColor.STONE)
			.strength(0.6F)
			.sound(SoundType.GRAVEL);

	public VanillaGravelBlock(BlockBehaviour.Properties p_55968_) {

		super(p_55968_);
	}

	public static VanillaGravelBlock of() {

		return new VanillaGravelBlock(BehaveDefault);
	}
}