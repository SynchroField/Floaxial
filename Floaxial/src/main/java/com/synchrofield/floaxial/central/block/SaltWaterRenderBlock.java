package com.synchrofield.floaxial.central.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class SaltWaterRenderBlock extends Block {

	public static final BlockBehaviour.Properties BehaveDefault = BlockBehaviour.Properties
			.of(Material.STONE)
			.noCollission()
			.noOcclusion()
			.strength(100.0F)
			.noDrops();

	public SaltWaterRenderBlock(Properties p_49795_) {

		super(p_49795_);
	}

	public static SaltWaterRenderBlock of() {

		return new SaltWaterRenderBlock(BehaveDefault);
	}

	@Override
	public RenderShape getRenderShape(BlockState p_54738_) {

		return RenderShape.MODEL;
	}
}
