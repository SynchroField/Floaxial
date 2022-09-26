package com.synchrofield.floaxial.central.registry;

import com.google.common.collect.ImmutableList;
import com.synchrofield.floaxial.central.configure.ProductConfigure;

import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaJungleFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class TreeRegistry {

	public static final String FeatureRenamePrefix = ProductConfigure.Name + "_";

	public String FeatureOakName = "oak_test";
	public static Holder<ConfiguredFeature<TreeConfiguration, ?>> FeatureOakHolder;

	public String JungleTreeName = "jungle_replace";
	public static Holder<ConfiguredFeature<TreeConfiguration, ?>> JungleTree;

	public String JungleTreeMegaName = "jungle_mega_replace";
	public static Holder<ConfiguredFeature<TreeConfiguration, ?>> JungleTreeMega;

	protected TreeRegistry() {

	}

	public static TreeRegistry of() {

		return new TreeRegistry();
	}

	public void register() {

		JungleTree = featureRegister(JungleTreeName, Feature.TREE, createJungleTree().ignoreVines()
				.build());

		JungleTreeMega = featureRegister(JungleTreeMegaName, Feature.TREE,
				(new TreeConfiguration.TreeConfigurationBuilder(
						BlockStateProvider.simple(Blocks.JUNGLE_LOG),
						new MegaJungleTrunkPlacer(10, 2, 19),
						BlockStateProvider.simple(
								CentralRegistry.instance().block.JungleLeafMobileObject.get()),
						new MegaJungleFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 2),
						new TwoLayersFeatureSize(1, 1, 2)))
						.decorators(ImmutableList.of(TrunkVineDecorator.INSTANCE,
								LeaveVineDecorator.INSTANCE))
						.build());
	}

	// from FeatureUtils
	public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<FC, ?>> featureRegister(
			String p_206489_, F p_206490_, FC p_206491_) {

		return BuiltinRegistries.registerExact(BuiltinRegistries.CONFIGURED_FEATURE, p_206489_,
				new ConfiguredFeature<>(p_206490_, p_206491_));
	}

	// TreeFeatures class
	private static TreeConfiguration.TreeConfigurationBuilder createJungleTree() {

		return createStraightBlobTree2(Blocks.JUNGLE_LOG,
				CentralRegistry.instance().block.JungleLeafMobileObject.get(), 4, 8, 0, 2);
	}

	private static TreeConfiguration.TreeConfigurationBuilder createStraightBlobTree2(
			Block p_195147_, Block p_195148_, int p_195149_, int p_195150_, int p_195151_,
			int p_195152_) {

		return new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(p_195147_),
				new StraightTrunkPlacer(p_195149_, p_195150_, p_195151_),
				BlockStateProvider.simple(p_195148_),
				new BlobFoliagePlacer(ConstantInt.of(p_195152_), ConstantInt.of(0), 3),
				new TwoLayersFeatureSize(1, 0, 1));
	}

}
