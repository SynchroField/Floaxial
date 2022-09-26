package com.synchrofield.floaxial.server.rule;

import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.synchrofield.library.configure.ConfigureException;
import com.synchrofield.library.terrain.Terrain;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;

// search criteria for a block
public class BlockCriteria {

	public final int criteriaType;
	public final String name;
	@Nullable
	public final BlockState blockState;
	@Nullable
	public final Map<Property<?>, Comparable<?>> propertyMap;
	@Nullable
	public final Block block;
	@Nullable
	public final Material blockMaterial;
	@Nullable
	public final Predicate<BlockState> equalPredicate;

	protected BlockCriteria(int criteriaType, String name, BlockState blockState,
			Map<Property<?>, Comparable<?>> propertyMap, Block block, Material blockMaterial,
			Predicate<BlockState> equalPredicate) {

		this.criteriaType = criteriaType;
		this.name = name;
		this.blockState = blockState;
		this.propertyMap = propertyMap;
		this.block = block;
		this.blockMaterial = blockMaterial;
		this.equalPredicate = equalPredicate;
	}

	public static BlockCriteria of(int criteriaType, String name) {

		return new BlockCriteria(criteriaType, name, null, null, null, null, null);
	}

	public BlockCriteria withCriteriaType(int criteriaType) {

		return new BlockCriteria(criteriaType, name, blockState, propertyMap, block, blockMaterial,
				equalPredicate);
	}

	public BlockCriteria withName(String name) {

		return new BlockCriteria(criteriaType, name, blockState, propertyMap, block, blockMaterial,
				equalPredicate);
	}

	public BlockCriteria withBlockState(BlockState blockState) {

		return new BlockCriteria(criteriaType, name, blockState, propertyMap, block, blockMaterial,
				equalPredicate);
	}

	public BlockCriteria withPropertyMap(Map<Property<?>, Comparable<?>> propertyMap) {

		return new BlockCriteria(criteriaType, name, blockState, propertyMap, block, blockMaterial,
				equalPredicate);
	}

	public BlockCriteria withBlock(Block block) {

		return new BlockCriteria(criteriaType, name, blockState, propertyMap, block, blockMaterial,
				equalPredicate);
	}

	public BlockCriteria withBlockMaterial(Material blockMaterial) {

		return new BlockCriteria(criteriaType, name, blockState, propertyMap, block, blockMaterial,
				equalPredicate);
	}

	public BlockCriteria withEqualPredicate(Predicate<BlockState> equalPredicate) {

		return new BlockCriteria(criteriaType, name, blockState, propertyMap, block, blockMaterial,
				equalPredicate);
	}

	public BlockCriteria withCompile() throws ConfigureException {

		switch (criteriaType) {

		default: {

			throw ConfigureException.of("Invalid criteria type.");
		}

		case BlockCriteriaType.BlockState: {

			BlockCriteria compile = withBlockState(Terrain.blockStateFromString(name));

			return compile.withEqualPredicate(compile::matchIs);
		}

		case BlockCriteriaType.BlockStateSubset: {

			BlockCriteria compile = withBlockState(Terrain.blockStateFromString(name))
					.withPropertyMap(Terrain.blockStatePropertyMapFromString(name));

			return compile.withEqualPredicate(compile::matchIs);
		}

		case BlockCriteriaType.Block: {

			BlockCriteria compile = withBlock(Terrain.blockFromString(name));

			return compile.withEqualPredicate(compile::matchIs);
		}

		case BlockCriteriaType.Material: {

			BlockCriteria compile = withBlockMaterial(Terrain.blockMaterialFromString(name));

			return compile.withEqualPredicate(compile::matchIs);
		}

		case BlockCriteriaType.FluidIs: {

			return withEqualPredicate(this::matchIs);
		}

		case BlockCriteriaType.SolidIs: {

			return withEqualPredicate(this::matchIs);
		}

		case BlockCriteriaType.HeavyIs: {

			return withEqualPredicate(this::matchIs);
		}

		case BlockCriteriaType.ReplaceableIs: {

			return withEqualPredicate(this::matchIs);
		}
		}
	}

	public void validate() throws ConfigureException {

		// just try compiling to dummy result
		withCompile();
	}

	public static void validate(int criteriaType, String name) throws ConfigureException {

		// just try compiling to dummy result
		BlockCriteria dummy = BlockCriteria.of(criteriaType, name);
		dummy = dummy.withCompile();
	}

	public boolean matchIs(ServerLevel level, BlockPos location) {

		return matchIs(level.getBlockState(location));
	}

	public boolean matchIs(BlockState sourceState) {

		switch (criteriaType) {

		default: {

			return false;
		}

		case BlockCriteriaType.BlockState: {

			return sourceState == this.blockState;
		}

		case BlockCriteriaType.BlockStateSubset: {

			return Terrain.blockStateCompareSubset(sourceState, blockState.getBlock(), propertyMap);
		}

		case BlockCriteriaType.Block: {

			return sourceState.getBlock() == block;
		}

		case BlockCriteriaType.Material: {

			return sourceState.getMaterial() == this.blockMaterial;
		}

		case BlockCriteriaType.FluidIs: {

			return sourceState.getFluidState() != null;
		}

		case BlockCriteriaType.SolidIs: {

			return Terrain.stateIsSolid(sourceState);
		}

		case BlockCriteriaType.HeavyIs: {

			return Terrain.stateIsHeavy(sourceState);
		}

		case BlockCriteriaType.ReplaceableIs: {

			return Terrain.stateIsReplaceable(sourceState);
		}
		}
	}
}
