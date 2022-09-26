package com.synchrofield.floaxial.central.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class VanillaJungleSaplingBlock extends SaplingBlock {

	private final AbstractTreeGrower treeGrowerReplace;

	public static final BlockBehaviour.Properties BehaveDefault = BlockBehaviour.Properties
			.of(Material.PLANT)
			.noCollission()
			.randomTicks()
			.instabreak()
			.sound(SoundType.GRASS);

	public VanillaJungleSaplingBlock(AbstractTreeGrower p_55978_, Properties p_55979_) {

		super(p_55978_, p_55979_);

		this.treeGrowerReplace = p_55978_;
	}

	public static VanillaJungleSaplingBlock of() {

		//		OakTreeGrowerReplace treeGrower = new OakTreeGrowerReplace();
		VanillaJungleTreeGrower treeGrower = new VanillaJungleTreeGrower();

		return new VanillaJungleSaplingBlock(treeGrower, BehaveDefault);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void randomTick(BlockState p_56003_, ServerLevel p_56004_, BlockPos p_56005_,
			Random p_56006_) {

		if (p_56004_.getMaxLocalRawBrightness(p_56005_.above()) >= 9 && p_56006_.nextInt(7) == 0) {

			if (!p_56004_.isAreaLoaded(p_56005_, 1))
				return; // Forge: prevent loading unloaded chunks when checking neighbor's light
			this.advanceTree(p_56004_, p_56005_, p_56003_, p_56006_);
		}
	}

	@Override
	public void advanceTree(ServerLevel p_55981_, BlockPos p_55982_, BlockState p_55983_,
			Random p_55984_) {

		if (p_55983_.getValue(STAGE) == 0) {

			p_55981_.setBlock(p_55982_, p_55983_.cycle(STAGE), 4);
		}
		else {

			if (!net.minecraftforge.event.ForgeEventFactory.saplingGrowTree(p_55981_, p_55984_,
					p_55982_))
				return;
			this.treeGrowerReplace.growTree(p_55981_, p_55981_.getChunkSource()
					.getGenerator(), p_55982_, p_55983_, p_55984_);
		}
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter p_55991_, BlockPos p_55992_,
			BlockState p_55993_, boolean p_55994_) {

		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level p_55996_, Random p_55997_, BlockPos p_55998_,
			BlockState p_55999_) {

		return (double) p_55996_.random.nextFloat() < 0.45D;
	}

	@Override
	public void performBonemeal(ServerLevel p_55986_, Random p_55987_, BlockPos p_55988_,
			BlockState p_55989_) {

		this.advanceTree(p_55986_, p_55988_, p_55989_, p_55987_);
	}
}
