package com.synchrofield.floaxial.server.droplet;

import com.synchrofield.floaxial.central.droplet.MaterialTable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class DropletTerrain {

	public final MaterialTable materialTable;

	protected DropletTerrain(MaterialTable materialTable) {

		this.materialTable = materialTable;
	}

	public static DropletTerrain of(MaterialTable materialTable) {

		return new DropletTerrain(materialTable);
	}

	public boolean stateIsAir(BlockState state) {

		return state.isAir();
	}

	public int locationGetNormalMaterial(ServerLevel level, BlockPos location) {

		BlockState state = level.getBlockState(location);

		return materialTable.mobileStateToMaterial(state);
	}

	public int locationGetArchiveMaterial(ServerLevel level, BlockPos location) {

		BlockState state = level.getBlockState(location);

		return materialTable.mobileStateToMaterial(state);
	}

	public boolean ghostExpire(ServerLevel level, int material, BlockPos location) {

		if (level.getBlockState(location) != materialTable.list[material].ghostState) {

			return false;
		}

		if (!level.setBlock(location, materialTable.list[material].dropletState,
				Block.UPDATE_CLIENTS)) {

		}

		return true;
	}

	public boolean locationIsDestination(ServerLevel level, BlockPos location, boolean solidIs) {

		BlockState state = level.getBlockState(location);

		if (stateIsAir(state)) {

			return true;
		}
		else {

			// sink in water
			if (solidIs) {

				if (state.getMaterial() == Material.WATER) {

					return true;
				}
			}

			return false;
		}
	}
}
