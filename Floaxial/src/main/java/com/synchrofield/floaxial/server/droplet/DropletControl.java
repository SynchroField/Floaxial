package com.synchrofield.floaxial.server.droplet;

import com.synchrofield.floaxial.central.configure.MaterialConfigureList;
import com.synchrofield.floaxial.central.configure.ServerDropletConfigure;
import com.synchrofield.floaxial.central.droplet.MaterialTable;
import com.synchrofield.floaxial.central.statistics.IntegerHistory;
import com.synchrofield.floaxial.central.statistics.ServerDropStatistics;
import com.synchrofield.floaxial.server.MaterialPlaceReceive;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraftforge.network.simple.SimpleChannel;

public class DropletControl {

	public static final int GapHistorySize = 100;

	public static final int RainRadiusDefault = 10;

	public final ServerDropletConfigure configure;

	public final MaterialTable materialTable;
	public final MaterialControlList materialControlList;

	public final DropletLevel dropLevel;

	public final GapQueue gapQueue;

	public final DropletTerrain dropTerrain;

	public ServerDropStatistics statistics;
	public IntegerHistory gapAddHistory;
	public IntegerHistory gapProcessHistory;

	public boolean rainIs;
	public int rainRadius;
	public long rainEndTick;

	protected DropletControl(ServerDropletConfigure configure, MaterialTable materialTable,

			DropletLevel dropLevel, MaterialControlList materialControlList, GapQueue gapQueue,
			DropletTerrain dropTerrain, ServerDropStatistics statistics) {

		this.configure = configure;
		this.materialTable = materialTable;

		this.dropLevel = dropLevel;
		this.materialControlList = materialControlList;

		this.gapQueue = gapQueue;

		this.dropTerrain = dropTerrain;

		this.statistics = statistics;
		this.gapAddHistory = IntegerHistory.of(GapHistorySize);
		this.gapProcessHistory = IntegerHistory.of(GapHistorySize);

		this.rainIs = false;
		this.rainRadius = RainRadiusDefault;
	}

	public static DropletControl of(ServerDropletConfigure configure,
			MaterialConfigureList commonMaterialConfigure, SimpleChannel channel,
			MaterialTable materialTable, MaterialPlaceReceive materialPlaceReceive) {

		GapQueue gapQueue = GapQueue.of(configure.gapSize);

		DropletLevel dropLevel = DropletLevel.of();

		DropletTerrain dropTerrain = DropletTerrain.of(materialTable);

		MaterialControlList materialControlList = MaterialControlList.of();

		for (int materialIndex = 0; materialIndex < MaterialTable.Size; materialIndex++) {

			materialControlList.list[materialIndex] = MaterialControl.of(
					configure.material[materialIndex], channel, materialTable, materialPlaceReceive,
					materialIndex, commonMaterialConfigure.list[materialIndex], gapQueue,
					dropTerrain, dropLevel);
		}

		ServerDropStatistics statistics = ServerDropStatistics.of();

		return new DropletControl(configure, materialTable, dropLevel, materialControlList,
				gapQueue, dropTerrain, statistics);
	}

	public void clear() {

		materialControlList.clear();

		gapQueue.clear();

		statistics = ServerDropStatistics.of();
		gapAddHistory.clear();
		gapProcessHistory.clear();
	}

	public void tick(ServerLevel level) {

		boolean EnableIs = true;

		if (!EnableIs) {

			return;
		}

		long tick = level.getGameTime();

		// material
		materialControlList.tick(level, tick);

		// gap
		int gapProcessMaximum = Math.min(gapQueue.useSize(), configure.gapPerTick);
		int gapProcessSize = 0;

		while (!gapQueue.emptyIs() && (gapProcessSize <= gapProcessMaximum)) {

			gapProcess(level, gapQueue.consume());
		}

		// prepare next tick
		gapAddHistory.append(0);
		gapProcessHistory.append(0);
	}

	public ServerDropStatistics statisticsDerive() {

		// material
		statistics.networkSendByte = 0;
		statistics.networkSendPacket = 0;

		for (int material = 0; material < MaterialTable.Size; material++) {

			statistics.material[material] = materialControlList.list[material].statisticsDerive();

			statistics.networkSendByte += statistics.material[material].networkSendByte;
			statistics.networkSendPacket += statistics.material[material].networkSendPacket;
		}

		// gap
		statistics.gapQueueSize = gapQueue.useSize();
		statistics.gapAddPerTickMean = gapAddHistory.mean();
		statistics.gapProcessPerTickMean = gapProcessHistory.mean();

		return statistics;
	}

	public void onGap(BlockPos location) {

		// blindly enqueue
		gapQueue.add(location);

		gapAddHistory.increment();
	}

	// Caller should just blindly add gaps and let this rate limited processing handle them.
	public void gapProcess(ServerLevel level, BlockPos location) {

		BlockState gapState = level.getBlockState(location);
		BlockState aboveState = level.getBlockState(location.above());

		if (gapState.isAir() && aboveState.isAir()) {

			// air column, only way a fluid can pyramid
			dearchiveTry(level, location.above(), true);

			dearchiveTry(level, location.above()
					.north(), true);

			dearchiveTry(level, location.above()
					.south(), true);

			dearchiveTry(level, location.above()
					.west(), true);

			dearchiveTry(level, location.above()
					.east(), true);
		}
		else {

			// mixed, only solid can fall
			dearchiveTry(level, location.above(), false);

			dearchiveTry(level, location.above()
					.north(), false);

			dearchiveTry(level, location.above()
					.south(), false);

			dearchiveTry(level, location.above()
					.west(), false);

			dearchiveTry(level, location.above()
					.east(), false);
		}

		gapProcessHistory.increment();
	}

	// only check archive because regular drops should be in list already
	public void dearchiveTry(ServerLevel level, BlockPos location, boolean fluidIs) {

		int material = dropTerrain.locationGetArchiveMaterial(level, location);

		if (material != MaterialTable.InvalidIndex) {

			if (!fluidIs) {

				// don't allow fluid
				if (!materialTable.list[material].solidIs) {

					return;
				}
			}

			// archive found
			if (!dropLevel.material.list[material].dropIsExist(location)) {

				// dearchive
				//				if (dropTerrain.archiveToMobile(level, material, location)) {

				materialControlList.list[material].dropPlace(location);
				//				}
			}
		}
	}

	public void onSectionLoad(SectionPos location, LevelChunkSection section) {

		PalettedContainer<BlockState> palette = section.getStates();

		for (int material = 0; material < MaterialTable.Size; material++) {

			// normal source block
			if (palette.maybeHas(materialTable.list[material].dropletEqualPredicate)) {

				// add to material
				materialControlList.list[material].onSectionLoad(location);
			}
		}
	}

	// return exist list as bit field
	@SuppressWarnings("unused")
	public int sectionDeriveMaterialList(LevelChunkSection section) {

		assert MaterialTable.Size <= 32 : "Bit field too small for material list.";

		PalettedContainer<BlockState> palette = section.getStates();

		int existList = 0;

		for (int material = 0; material < MaterialTable.Size; material++) {

			// normal source block
			if (palette.maybeHas(materialTable.list[material].dropletEqualPredicate)) {

				// has this material
				existList |= (1 << material);
			}
		}

		return existList;
	}

	public boolean sectionIsContainDrop(LevelChunkSection section) {

		PalettedContainer<BlockState> palette = section.getStates();

		for (int material = 0; material < MaterialTable.Size; material++) {

			// normal source block
			if (palette.maybeHas(materialTable.list[material].dropletEqualPredicate)) {

				// has this material
				return true;
			}
		}

		return false;
	}

	public void onBlockPlace(BlockPos location, BlockState state) {

		onMaterialPlace(location, materialTable.mobileStateToMaterial(state));
	}

	public void onMaterialPlace(BlockPos location, int materialIndex) {

		if (MaterialTable.indexCheck(materialIndex)) {

			materialControlList.list[materialIndex].dropPlace(location);
		}
	}

	public void rainStart(ServerLevel level, int timeSize) {

		rainIs = true;

		rainRadius = RainRadiusDefault;

		long tick = level.getGameTime();

		rainEndTick = tick + (long) timeSize;
	}

	public void rainTick(ServerLevel level, long tick, BlockPos center) {

		if (!rainIs) {

			return;
		}

		if (tick >= rainEndTick) {

			rainIs = false;
			return;
		}

		int material = 0;

		// add random drop near player
		int RainAddPerTick = 1;

		for (int i = 0; i < RainAddPerTick; i++) {

			int offsetX = level.random.nextInt(-rainRadius, rainRadius);
			int offsetZ = level.random.nextInt(-rainRadius, rainRadius);

			BlockPos dropLocation = center.offset(offsetX, rainRadius, offsetZ);

			if (dropTerrain.locationIsDestination(level, dropLocation,
					materialTable.list[material].solidIs)) {

				if (!level.setBlock(dropLocation, materialTable.list[material].dropletState,
						Block.UPDATE_CLIENTS)) {

					// fail
					return;
				}

				materialControlList.list[material].dropPlace(dropLocation);
			}
		}
	}
}
