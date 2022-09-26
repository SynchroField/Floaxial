package com.synchrofield.floaxial.central.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

// make this an entity that way it can occupy locations that have blocks already
public class MarkBlock extends Block {

	public static final BlockBehaviour.Properties BehaveDefault = BlockBehaviour.Properties.of(Material.AIR)
			.strength(1.0f).noOcclusion().isSuffocating(MarkBlock::never).isViewBlocking(MarkBlock::never).air()
			.instabreak().noCollission().noDrops();

	public MarkBlock(BlockBehaviour.Properties behave) {

		super(behave);
	}

	public static MarkBlock of() {

		return new MarkBlock(BehaveDefault);
	}

	private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {

		return false;
	}
}