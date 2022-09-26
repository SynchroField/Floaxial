package com.synchrofield.floaxial.central.block;

import java.util.Random;

import com.synchrofield.floaxial.central.CentralControl;
import com.synchrofield.floaxial.central.registry.CentralRegistry;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class VanillaJungleLeafBlock extends LeavesBlock {

	public static final BlockBehaviour.Properties DefaultBehavior = BlockBehaviour.Properties
			.of(Material.LEAVES)
			.strength(0.2F)
			.sound(SoundType.GRASS)
			.randomTicks()
			.noOcclusion()
			.isValidSpawn(VanillaJungleLeafBlock::ocelotOrParrot)
			.isSuffocating(VanillaJungleLeafBlock::never)
			.isViewBlocking(VanillaJungleLeafBlock::never);

	public static final BlockColor Color = (p_92567_, p_92568_, p_92569_, p_92570_) -> {

		return 0xff66bb44;
	};

	public VanillaJungleLeafBlock() {

		super(DefaultBehavior);
	}

	public VanillaJungleLeafBlock(BlockBehaviour.Properties p_54422_) {

		super(p_54422_);
	}

	public static VanillaJungleLeafBlock of() {

		return new VanillaJungleLeafBlock();
	}

	private static Boolean ocelotOrParrot(BlockState p_50822_, BlockGetter p_50823_,
			BlockPos p_50824_, EntityType<?> p_50825_) {

		return p_50825_ == EntityType.OCELOT || p_50825_ == EntityType.PARROT;
	}

	private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {

		return false;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos location, Random random) {

		if (!state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 7) {

			// trunk gone
			BlockState mobileState = CentralRegistry.instance().block.JungleLeafMobileObject.get()
					.defaultBlockState()
					.setValue(PERSISTENT, true);

			// convert to mobile so the leaf drops
			level.setBlock(location, mobileState, UPDATE_CLIENTS);

			// notify material control
			CentralControl.terrainEventReceive()
					.onBlockPlace(level, location, mobileState);
		}
	}
}
