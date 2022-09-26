package com.synchrofield.floaxial.central.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

// make this an entity that way it can occupy locations that have blocks already
public class PalmLeafXBlock extends Block {

	public static final BlockBehaviour.Properties BehaveDefault = BlockBehaviour.Properties
			.of(Material.LEAVES)
			.strength(0.2F)
			.sound(SoundType.GRASS)
			.noOcclusion()
			.isSuffocating(PalmLeafXBlock::never)
			.isViewBlocking(PalmLeafXBlock::never);

	public PalmLeafXBlock(BlockBehaviour.Properties behave) {

		super(behave);
	}

	public static PalmLeafXBlock of() {

		return new PalmLeafXBlock(BehaveDefault);
	}

	private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {

		return false;
	}
}