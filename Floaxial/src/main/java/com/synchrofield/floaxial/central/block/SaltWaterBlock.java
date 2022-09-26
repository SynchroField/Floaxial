package com.synchrofield.floaxial.central.block;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SaltWaterBlock extends LiquidBlock {

	public static final BlockBehaviour.Properties BehaveDefault = BlockBehaviour.Properties
			.of(Material.WATER)
			.noCollission()
			.strength(100.0F)
			.noDrops();

	public SaltWaterBlock(Supplier<? extends FlowingFluid> p_54694_,
			BlockBehaviour.Properties p_54695_) {

		super(p_54694_, p_54695_);
	}

	public static SaltWaterBlock of(Supplier<? extends FlowingFluid> p_54694_) {

		return new SaltWaterBlock(p_54694_, BehaveDefault);
	}

	@Override
	public FluidState getFluidState(BlockState p_60577_) {

		return super.getFluidState(p_60577_);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState p_54745_, BlockGetter p_54746_,
			BlockPos p_54747_) {

		return false;
	}

	@Override
	public boolean isPathfindable(BlockState p_54704_, BlockGetter p_54705_, BlockPos p_54706_,
			PathComputationType p_54707_) {

		return false;
	}

	@Override
	public boolean skipRendering(BlockState p_54716_, BlockState p_54717_, Direction p_54718_) {

		// not sure
		//		return p_54716_.equals(p_54717_);

		return p_54717_.getFluidState()
				.getType()
				.isSame(this.getFluid());
	}

	@Override
	public RenderShape getRenderShape(BlockState p_54738_) {

		//		return RenderShape.MODEL;
		// allow sheet render to take place instead of individual block
		return RenderShape.INVISIBLE;
	}

	@Override
	public List<ItemStack> getDrops(BlockState p_54720_, LootContext.Builder p_54721_) {

		return Collections.emptyList();
	}

	@Override
	public VoxelShape getShape(BlockState p_54749_, BlockGetter p_54750_, BlockPos p_54751_,
			CollisionContext p_54752_) {

		return super.getShape(p_54749_, p_54750_, p_54751_, p_54752_);
	}

	@Override
	public BlockState updateShape(BlockState p_54723_, Direction p_54724_, BlockState p_54725_,
			LevelAccessor p_54726_, BlockPos p_54727_, BlockPos p_54728_) {

		return p_54723_;
	}

	@Override
	public void tick(BlockState p_60462_, ServerLevel p_60463_, BlockPos p_60464_,
			Random p_60465_) {

	}

	@Override
	public void randomTick(BlockState p_54740_, ServerLevel p_54741_, BlockPos p_54742_,
			Random p_54743_) {

	}

	@Override
	public boolean isRandomlyTicking(BlockState p_54732_) {

		return false;
	}

	@Override
	public void onPlace(BlockState p_54754_, Level p_54755_, BlockPos p_54756_, BlockState p_54757_,
			boolean p_54758_) {

	}

	@Override
	public void neighborChanged(BlockState p_54709_, Level p_54710_, BlockPos p_54711_,
			Block p_54712_, BlockPos p_54713_, boolean p_54714_) {

	}
}