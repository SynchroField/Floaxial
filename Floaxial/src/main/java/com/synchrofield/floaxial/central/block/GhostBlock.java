package com.synchrofield.floaxial.central.block;

import com.synchrofield.floaxial.central.droplet.MaterialTable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

// used for move destination while move is in progress
public class GhostBlock extends Block {

	public static final int MaterialMinimum = 0;
	public static final int MaterialMaximum = MaterialTable.Maximum;

	// test2
	public static final IntegerProperty MaterialProperty = IntegerProperty.create("drop_material",
			MaterialMinimum, MaterialMaximum);

	public static final BlockBehaviour.Properties BehaveDefault = BlockBehaviour.Properties
			.of(Material.STONE)
			.strength(1.0f)
			.noOcclusion()
			.isSuffocating(GhostBlock::never)
			.isViewBlocking(GhostBlock::never)
			.instabreak()
			.noCollission()
			.noDrops();

	// shows as a translucent block
	public boolean ghostIsVisible;

	public GhostBlock(BlockBehaviour.Properties behave, boolean ghostIsVisible) {

		super(behave);

		this.registerDefaultState(this.stateDefinition.any()
				.setValue(MaterialProperty, Integer.valueOf(MaterialMinimum)));

		this.ghostIsVisible = ghostIsVisible;
	}

	public static GhostBlock of(boolean ghostIsOutline) {

		return new GhostBlock(BehaveDefault, ghostIsOutline);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> p_49915_) {

		p_49915_.add(MaterialProperty);
	}

	private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {

		return false;
	}

	public void onConfigure(boolean ghostIsOutline) {

		this.ghostIsVisible = ghostIsOutline;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean skipRendering(BlockState p_60532_, BlockState p_60533_, Direction p_60534_) {

		if (ghostIsVisible) {

			return super.skipRendering(p_60532_, p_60533_, p_60534_);
		}
		else {

			return true;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public RenderShape getRenderShape(BlockState p_48758_) {

		if (ghostIsVisible) {

			return super.getRenderShape(p_48758_);
		}
		else {

			return RenderShape.INVISIBLE;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(BlockState p_48760_, BlockGetter p_48761_, BlockPos p_48762_,
			CollisionContext p_48763_) {

		if (ghostIsVisible) {

			return super.getShape(p_48760_, p_48761_, p_48762_, p_48763_);
		}
		else {

			return Shapes.empty();
		}
	}
}
