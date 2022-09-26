package com.synchrofield.floaxial.central.block;

import java.util.Random;

import com.synchrofield.floaxial.central.registry.TreeRegistry;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class VanillaJungleTreeGrower extends AbstractMegaTreeGrower {

	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random p_204326_,
			boolean p_204327_) {

		return TreeRegistry.JungleTree;
	}

	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredMegaFeature(Random p_204324_) {

		return TreeRegistry.JungleTreeMega;
	}
}