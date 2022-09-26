package com.synchrofield.floaxial.central.droplet;

import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.synchrofield.floaxial.central.block.GhostBlock;
import com.synchrofield.floaxial.central.configure.MaterialConfigure;
import com.synchrofield.floaxial.central.registry.CentralRegistry;
import com.synchrofield.library.configure.ConfigureException;
import com.synchrofield.library.terrain.Terrain;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class DropletMaterial {

	// droplet (mobile and archive)
	public final String dropletStateName;
	public boolean dropletStateIsSubset;
	public Map<Property<?>, Comparable<?>> dropletPropertyMap;

	@Nullable
	public BlockState dropletState;

	// ghost
	@Nullable
	public BlockState ghostState;

	// render
	public final String renderStateName;
	@Nullable
	public BlockState renderState;

	// doesn't include ghost or render state
	@Nullable
	public Predicate<BlockState> dropletEqualPredicate;

	public final int holeRadius;
	public final int pyramidSizeY;

	// sink in fluid
	public final boolean solidIs;

	protected DropletMaterial(String dropletStateName, boolean dropletStateIsSubset,
			String renderStateName, int holeRadius, int pyramidSizeY, boolean solidIs) {

		this.dropletStateName = dropletStateName;
		this.dropletStateIsSubset = dropletStateIsSubset;
		this.renderStateName = renderStateName;

		this.holeRadius = holeRadius;
		this.pyramidSizeY = pyramidSizeY;
		this.solidIs = solidIs;
	}

	public static DropletMaterial of(String dropletStateName, boolean dropletStateIsSubset,
			String renderStateName, boolean renderStateIsSubset, int holeRadius, int pyramidSizeY,
			boolean solidIs) {

		return new DropletMaterial(dropletStateName, dropletStateIsSubset, renderStateName,
				pyramidSizeY, holeRadius, solidIs);
	}

	public static DropletMaterial of(MaterialConfigure configure) {

		String mobileStateName = configure.mobileState;
		boolean mobileStateIsSubset = configure.mobileStateIsSubset;
		String renderStateName = configure.renderState;

		return new DropletMaterial(mobileStateName, mobileStateIsSubset, renderStateName,
				configure.holeRadius, configure.pyramidSizeY, configure.denseIs);
	}

	public void cacheDerive(int materialIndex, CentralRegistry registry) throws ConfigureException {

		dropletState = Terrain.blockStateFromString(dropletStateName);
		dropletPropertyMap = Terrain.blockStatePropertyMapFromString(dropletStateName);

		renderState = Terrain.blockStateFromString(renderStateName);

		// embed material id in ghost block state
		this.ghostState = registry.block.DropGhostObject.get()
				.defaultBlockState()
				.setValue(GhostBlock.MaterialProperty, materialIndex);

		this.dropletEqualPredicate = (blockState) -> {

			return dropletStateIs(blockState);
		};
	}

	public boolean dropletStateIs(BlockState target) {

		if (dropletStateIsSubset) {

			return Terrain.blockStateCompareSubset(target, dropletState.getBlock(),
					dropletPropertyMap);
		}
		else {

			return target == dropletState;
		}
	}
}
