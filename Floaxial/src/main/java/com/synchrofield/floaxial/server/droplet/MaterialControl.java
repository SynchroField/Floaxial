package com.synchrofield.floaxial.server.droplet;

import java.util.ArrayList;

import javax.annotation.Nullable;

import com.synchrofield.floaxial.central.configure.MaterialConfigure;
import com.synchrofield.floaxial.central.configure.ServerMaterialConfigure;
import com.synchrofield.floaxial.central.droplet.MaterialPhysics;
import com.synchrofield.floaxial.central.droplet.MaterialTable;
import com.synchrofield.floaxial.central.network.DropNetwork;
import com.synchrofield.floaxial.central.network.DropPacket;
import com.synchrofield.floaxial.central.network.DropSectionNetwork;
import com.synchrofield.floaxial.central.statistics.IntegerHistory;
import com.synchrofield.floaxial.central.statistics.ServerMaterialStatistics;
import com.synchrofield.floaxial.server.MaterialPlaceReceive;
import com.synchrofield.library.math.MathUtility;
import com.synchrofield.library.terrain.Geometry;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ShortMap;
import it.unimi.dsi.fastutil.shorts.Short2ShortOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.network.simple.SimpleChannel;

public class MaterialControl {

	public static final int HistorySize = 100;

	public final ServerMaterialConfigure configure;

	public final int materialIndex;
	public final MaterialTable materialTable;
	public final MaterialPlaceReceive materialPlaceReceive;

	public final MaterialPhysics dropPhysics;

	public final DropletLevel dropLevel;
	public final MaterialLevel materialLevel;

	public final LoadQueue loadQueue;

	@Nullable
	public SectionPos currentLoadSection;

	public final GapQueue gapQueue;

	public final DropletTerrain dropTerrain;

	public final DropletPacketPool packetPool;

	public ServerMaterialStatistics statistics;
	public IntegerHistory ghostProcessHistory;
	public IntegerHistory dropletProcessHistory;
	public IntegerHistory moveHistory;

	public long previousGhostTick;
	public long previousNormalTick;

	protected MaterialControl(ServerMaterialConfigure configure, MaterialTable materialTable,
			MaterialPlaceReceive materialPlaceReceive, int materialIndex,
			MaterialPhysics dropPhysics, DropletLevel dropLevel, MaterialLevel materialLevel,
			LoadQueue loadQueue, GapQueue gapQueue, DropletTerrain dropTerrain,
			DropletPacketPool packetPool, ServerMaterialStatistics statistics) {

		this.configure = configure;

		this.materialTable = materialTable;
		this.materialPlaceReceive = materialPlaceReceive;
		this.materialIndex = materialIndex;
		this.dropPhysics = dropPhysics;

		this.dropLevel = dropLevel;
		this.materialLevel = materialLevel;

		this.loadQueue = loadQueue;
		this.currentLoadSection = null;

		this.dropTerrain = dropTerrain;

		this.gapQueue = gapQueue;

		this.packetPool = packetPool;

		this.statistics = statistics;
		this.ghostProcessHistory = IntegerHistory.of(HistorySize);
		this.dropletProcessHistory = IntegerHistory.of(HistorySize);
		this.moveHistory = IntegerHistory.of(HistorySize);

		this.previousGhostTick = 0;
		this.previousNormalTick = 0;
	}

	public static MaterialControl of(ServerMaterialConfigure configure, SimpleChannel channel,
			MaterialTable materialTable, MaterialPlaceReceive materialPlaceReceive,
			int materialIndex, MaterialConfigure commonMaterialConfigure, GapQueue gapQueue,
			DropletTerrain dropTerrain, DropletLevel dropLevel) {

		MaterialPhysics dropPhysics = MaterialPhysics.of(commonMaterialConfigure);

		MaterialLevel materialLevel = dropLevel.material.list[materialIndex];

		LoadQueue loadQueue = LoadQueue.of(configure.loadSectionSize);

		DropletPacketPool packetPool = DropletPacketPool.of(channel, configure.packetSize);

		ServerMaterialStatistics statistics = ServerMaterialStatistics.of();

		return new MaterialControl(configure, materialTable, materialPlaceReceive, materialIndex,
				dropPhysics, dropLevel, materialLevel, loadQueue, gapQueue, dropTerrain, packetPool,
				statistics);
	}

	public void clear() {

		materialLevel.clear();

		loadQueue.clear();
		currentLoadSection = null;

		packetPool.clear();

		statistics = ServerMaterialStatistics.of();
		ghostProcessHistory.clear();
		dropletProcessHistory.clear();
		moveHistory.clear();

		previousGhostTick = 0;
		previousNormalTick = 0;
	}

	public void tick(ServerLevel level, long tick) {

		boolean processIsGhost = (tick % configure.ghostProcessPeriod) == 0;
		boolean processIsNormal = (tick % configure.mobileProcessPeriod) == 0;

		if (processIsGhost) {

			int TickDeltaMaximum = 1000;

			int ghostDeltaTick;
			if (previousGhostTick == 0) {

				ghostDeltaTick = 1;
			}
			else {

				ghostDeltaTick = (int) (tick - previousGhostTick);

				ghostDeltaTick = MathUtility.cap(ghostDeltaTick, TickDeltaMaximum);
			}

			previousGhostTick = tick;

			if (processIsNormal) {

				int normalDeltaTick;
				if (previousNormalTick == 0) {

					normalDeltaTick = 1;
				}
				else {

					normalDeltaTick = (int) (tick - previousNormalTick);

					normalDeltaTick = MathUtility.cap(normalDeltaTick, TickDeltaMaximum);
				}

				previousNormalTick = tick;

				dropLevelTick(level, tick, ghostDeltaTick, normalDeltaTick, true);
			}
			else {

				dropLevelTick(level, tick, ghostDeltaTick, 0, false);
			}
		}

		// load queue
		//
		// ignore fluid for now otherwise will try to load whole ocean
		// later will use different block type for archive
		final boolean loadQueueIsEnable = materialTable.list[materialIndex].solidIs;
		if (loadQueueIsEnable) {

			loadQueueProcess(level, tick);
		}
	}

	public void dropLevelTick(ServerLevel level, long tick, int ghostDeltaTick, int normalDeltaTick,
			boolean processNormalIs) {

		Object[] sectionArray = materialLevel.sectionMap.long2ObjectEntrySet()
				.toArray();

		ArrayList<Long> removeList = new ArrayList<>();

		for (int i = 0; i < sectionArray.length; i++) {

			@SuppressWarnings("unchecked")
			Long2ObjectMap.Entry<DropletSection> entry = (Long2ObjectMap.Entry<DropletSection>) sectionArray[i];

			DropletSection dropSection = entry.getValue();
			if (dropSection == null) {

				continue;
			}

			if (dropSection.emptyIs()) {

				// empty, remove
				removeList.add(entry.getLongKey());
				continue;
			}

			SectionPos sectionLocation = SectionPos.of(entry.getLongKey());

			dropSectionTick(level, tick, ghostDeltaTick, normalDeltaTick, processNormalIs,
					sectionLocation, dropSection);
		}

		// remove
		for (Long sectionLong : removeList) {

			materialLevel.sectionMap.remove(sectionLong.longValue());
		}

		// prepare next history tick
		ghostProcessHistory.append(0);
		dropletProcessHistory.append(0);
		moveHistory.append(0);
	}

	public void dropSectionTick(ServerLevel level, long tick, int ghostDeltaTick,
			int normalDeltaTick, boolean processNormalIs, SectionPos sectionLocation,
			DropletSection section) {

		statistics.processSection++;

		// network
		packetPool.currentPacket = DropPacket.ofEmpty();
		packetPool.currentPacket.createTime = level.getGameTime();

		// mark new section
		int[] packetMoveList = new int[DropSectionNetwork.MoveListSizeMaximum];

		packetPool.currentPacket.section = DropSectionNetwork.of(sectionLocation, materialIndex,
				packetMoveList);

		packetPool.currentSection = packetPool.currentPacket.section;

		/// first ensure this section and all 6 touch are loaded
		/// if not send a mask of which sides should be treated as solid wall
		ObjectIterator<Short2ShortMap.Entry> ghostIterator = section.ghostMap.short2ShortEntrySet()
				.fastIterator();

		DropletMoveList moveList = DropletMoveList.of();

		ArrayList<Short> removeList = new ArrayList<>();

		while (ghostIterator.hasNext()) {

			statistics.processGhost++;

			Short2ShortMap.Entry entry;

			entry = ghostIterator.next();

			if (!ghostProcess(level, tick, ghostDeltaTick, sectionLocation, section, entry,
					moveList)) {

				// move or delete
				removeList.add(entry.getShortKey());
			}
		}

		if (processNormalIs) {

			ObjectIterator<Short2ShortMap.Entry> normalIterator = section.dropMap
					.short2ShortEntrySet()
					.fastIterator();

			while (normalIterator.hasNext()) {

				statistics.processDrop++;

				Short2ShortMap.Entry entry = normalIterator.next();

				final int expireDeltaExact = 0;

				if (!dropProcess(level, tick, normalDeltaTick, sectionLocation, section, entry,
						moveList, false, expireDeltaExact)) {

					// delete
					removeList.add(entry.getShortKey());
				}
			}
		}

		// only actual remove, not move source
		for (short removeLocation : removeList) {

			section.dropMap.remove(removeLocation);

			// dearchive blocks near this hole
			BlockPos gapLocation = Droplet.locationFromPack(sectionLocation, removeLocation);
			gapQueue.add(gapLocation);
		}

		// must be written in exact order of moves, otherwise this will swap with blocks that
		// have moved twice and no longer there
		for (DropletMove move : moveList) {

			// source map
			Short2ShortOpenHashMap sourceMap;
			if (move.sourceIsGhost) {

				sourceMap = section.ghostMap;
			}
			else {

				sourceMap = section.dropMap;
			}

			int sourceLocationPack = Droplet.locationToPack(move.source);

			if (move.source.equals(move.destination)) {

				// convert ghost to normal
				// just convert don't move
				section.ghostMap.remove((short) sourceLocationPack);
				section.dropAdd(move.sourceDrop);
				continue;
			}

			// swap fluid destination
			if (move.destinationMaterial != MaterialTable.InvalidIndex) {

				assert !materialTable.list[move.destinationMaterial].solidIs
						: "swap solid destination";

				// assume fluid destination is never ghost for now
				if (dropLevel.material.list[move.destinationMaterial]
						.dropIsExist(move.destination)) {

					// remove existing destination before swap
					dropLevel.material.list[move.destinationMaterial].dropRemove(move.destination);
				}

				// set as source
				int newSourceDrop = Droplet.locationSet(Droplet.DefaultValue, sourceLocationPack);
				newSourceDrop = Droplet.timeSet(newSourceDrop, configure.archivePeriod);

				dropLevel.material.list[move.destinationMaterial].dropAdd(move.source,
						newSourceDrop);
			}
			else {

				// no swap, just remove source
				sourceMap.remove((short) sourceLocationPack);

				gapQueue.add(move.source);
			}

			// add new destination drop
			dropLevel.material.list[materialIndex].ghostAdd(move.destination, move.sourceDrop);
		}

		// one packet per section for now
		if (packetPool.currentSection().moveListSize > 0) {

			statistics.networkSendByte += packetPool.currentPacket.networkSize();
			statistics.networkSendPacket++;

			statistics.networkSendSection++;
			statistics.networkSendDrop += packetPool.currentPacket().section.moveListSize;

			packetPool.send();
		}
	}

	// external move are implicitly ghosts
	// return false if move or delete
	public boolean ghostProcess(ServerLevel level, long tick, int ghostDeltaTick,
			SectionPos sectionLocation, DropletSection section, Short2ShortMap.Entry entry,
			DropletMoveList moveList) {

		ghostProcessHistory.increment();

		int drop = Droplet.fromShort(entry.getShortKey(), entry.getShortValue());

		int GhostProcessWindow = configure.ghostLookahead << Droplet.TimeDecimalSize;

		int expireDeltaExact = Droplet.timeGet(drop);

		expireDeltaExact -= (ghostDeltaTick << Droplet.TimeDecimalSize);
		if (expireDeltaExact > GhostProcessWindow) {

			// too soon to process
			drop = Droplet.timeSet(drop, expireDeltaExact);
			entry.setValue(Droplet.toData(drop));

			return true;
		}

		// try expire
		BlockPos sourceLocation = Droplet.locationGetUnpack(drop, sectionLocation);

		if (expireDeltaExact <= 0) {

			// expire, convert and wait for normal tick
			if (!dropTerrain.ghostExpire(level, materialIndex, sourceLocation)) {

				// ghost is gone so don't bother converting back to drop, just remove
				return false;
			}

			// set archive time
			int archiveDelta = configure.archivePeriod;
			drop = Droplet.timeSet(drop, archiveDelta);

			// add to normal map
			// remove from ghost map
			DropletMove moveGhostToNormal = DropletMove.of(true, sourceLocation, drop,
					Direction.DOWN, false, sourceLocation, MaterialTable.InvalidIndex, false);

			moveList.add(moveGhostToNormal);

			// notify rule
			materialPlaceReceive.onDropArchive(sourceLocation, materialIndex);

			return true;
		}

		// decrement time
		drop = Droplet.timeSet(drop, expireDeltaExact);
		entry.setValue(Droplet.toData(drop));

		// process slightly before turning back to normal block
		if (!dropProcess(level, tick, 0, sectionLocation, section, entry, moveList, true,
				expireDeltaExact)) {

			// remove
			return false;
		}

		return true;
	}

	// normalDeltaTick only valid if not ghost
	public boolean dropProcess(ServerLevel level, long tick, int normalDeltaTick,
			SectionPos sectionLocation, DropletSection section, Short2ShortMap.Entry entry,
			DropletMoveList moveList, boolean ghostIs, int expireDeltaExact) {

		int drop = Droplet.fromShort(entry.getShortKey(), entry.getShortValue());

		BlockPos sourceLocation = Droplet.locationGetUnpack(drop, sectionLocation);

		if (!ghostIs) {

			dropletProcessHistory.increment();

			// try archive
			int archiveDelta = Droplet.timeGet(drop);

			archiveDelta -= normalDeltaTick;
			if (archiveDelta <= 0) {

				// remove if above sea level (for now)
				if (!materialTable.list[materialIndex].solidIs) {

					int OceanLevel = 7;
					if (sourceLocation.getY() > OceanLevel) {

						if (materialTable.list[materialIndex]
								.dropletStateIs(level.getBlockState(sourceLocation))) {

							level.setBlock(sourceLocation, Blocks.AIR.defaultBlockState(), 2);
							return false;
						}
					}
				}
				// stop tracking archive
				return false;
			}
			else {

				// still waiting, write new time
				drop = Droplet.timeSet(drop, archiveDelta);
				entry.setValue(Droplet.toData(drop));
			}
		}

		int energy0 = Droplet.energyGet(drop);

		if (energy0 > 0) {

			// damp more if going fast
			double dampEnergyChance = 0.5
					* ((double) (energy0 + 1) / (double) (Droplet.EnergyMaximum + 1));

			double dampChance = MathUtility.capNormal(0.5 + dampEnergyChance);

			if (level.random.nextDouble() <= dampChance) {

				energy0 = dropPhysics.energyCap(energy0 - 1);

				drop = Droplet.energySet(drop, energy0);

				// update hash value in situ
				entry.setValue(Droplet.toData(drop));
			}
		}

		Direction direction0 = Geometry.DirectionFromPack[Droplet.directionGet(drop)];

		DropletMove move = levelTryAllMove(level, sourceLocation, energy0, direction0, ghostIs);
		if (move == null) {

			return true;
		}

		// write new drop always a ghost
		int destinationDrop = drop;

		destinationDrop = Droplet.locationSetUnpack(destinationDrop, move.destination);

		destinationDrop = Droplet.directionSet(destinationDrop,
				Geometry.DirectionToPack(move.direction));

		int destinationEnergy;
		if (move.direction == Direction.DOWN) {

			// gain 1 ke from 1 pe fall
			destinationEnergy = dropPhysics.energyCap(energy0 + 1);
		}
		else {

			if (energy0 == 0) {

				// must be hole search so wake the block up
				energy0 = 1;
			}

			destinationEnergy = energy0;
		}

		destinationDrop = Droplet.energySet(destinationDrop, destinationEnergy);

		double fallTimeExact;
		if (move.direction == Direction.DOWN) {

			// gravity
			fallTimeExact = dropPhysics.fallDeriveTime(energy0, 1);
		}
		else {

			// sideways
			fallTimeExact = dropPhysics.moveDeriveTimeConstant(energy0);
		}

		fallTimeExact /= dropPhysics.configure.timeScale;

		fallTimeExact *= (double) Droplet.TimeDecimalScale;
		fallTimeExact += expireDeltaExact;

		int fallTimePack = MathUtility.cap((int) fallTimeExact, Droplet.TimeMaximum);

		destinationDrop = Droplet.timeSet(destinationDrop, fallTimePack);

		// confirm move
		move.sourceDrop = destinationDrop;
		moveList.add(move);

		// network
		networkAddMove(sourceLocation, expireDeltaExact, energy0, move.direction);

		moveHistory.increment();

		// move
		return false;
	}

	// try direct hole 
	// then sideways move if energy > 0
	// then the 4 pyramid location
	@Nullable
	public DropletMove levelTryAllMove(ServerLevel level, BlockPos sourceLocation, int energy0,
			Direction direction0, boolean sourceIsGhost) {

		// direct gravity
		DropletMove move = levelTryMove(level, sourceLocation, Direction.DOWN, sourceIsGhost);
		if (move != null) {

			return move;
		}

		// sideways energy
		if (energy0 > 0) {

			move = levelTryMove(level, sourceLocation, direction0, sourceIsGhost);
			if (move != null) {

				return move;
			}
		}

		// hole search
		if (materialTable.list[materialIndex].holeRadius > 0) {

			// pyramid
			move = levelTryPyramid(level, sourceLocation,
					materialTable.list[materialIndex].pyramidSizeY, sourceIsGhost);
			if (move != null) {

				return move;
			}
		}

		return null;
	}

	// assume try direct gravity and sideways energy already
	@Nullable
	public DropletMove levelTryPyramid(ServerLevel level, BlockPos sourceLocation, int sizeY,
			boolean sourceIsGhost) {

		Direction candidateDirection = Direction.NORTH;
		DropletMove move = levelTryPyramid(level, sourceLocation, sizeY, sourceIsGhost,
				candidateDirection);
		if (move != null) {

			return move;
		}

		candidateDirection = Direction.SOUTH;
		move = levelTryPyramid(level, sourceLocation, sizeY, sourceIsGhost, candidateDirection);
		if (move != null) {

			return move;
		}

		candidateDirection = Direction.WEST;
		move = levelTryPyramid(level, sourceLocation, sizeY, sourceIsGhost, candidateDirection);
		if (move != null) {

			return move;
		}

		candidateDirection = Direction.EAST;
		move = levelTryPyramid(level, sourceLocation, sizeY, sourceIsGhost, candidateDirection);
		if (move != null) {

			return move;
		}

		return null;
	}

	public DropletMove levelTryPyramid(ServerLevel level, BlockPos sourceLocation, int sizeY,
			boolean sourceIsGhost, Direction direction) {

		assert sizeY > 0;

		BlockPos destinationLocation = sourceLocation.relative(direction);

		for (int dY = 0; dY < sizeY; dY++) {

			if (!dropTerrain.locationIsDestination(level, destinationLocation.below(dY + 1),
					materialTable.list[materialIndex].solidIs)) {

				// hit solid
				return null;
			}
		}

		// column is clear, now try moving sideways to the column
		return levelTryMove(level, sourceLocation, direction, sourceIsGhost);
	}

	// moves a drop or ghost to a ghost at destination
	@Nullable
	public DropletMove levelTryMove(ServerLevel level, BlockPos sourceLocation, Direction direction,
			boolean sourceIsGhost) {

		BlockPos destinationLocation = sourceLocation.relative(direction);

		BlockState sourceState = level.getBlockState(sourceLocation);

		// always become ghost upon move
		BlockState newSourceState = materialTable.list[materialIndex].ghostState;

		BlockState destinationState = level.getBlockState(destinationLocation);

		if (!materialTable.materialIs(materialIndex, sourceState)) {

			// source disappear
			return null;
		}

		boolean destinationIsExternal = !SectionPos.of(sourceLocation)
				.equals(SectionPos.of(destinationLocation));

		if (destinationState.isAir()) {

			// normal move
			if (!level.setBlock(destinationLocation, newSourceState, 2)) {

				return null;
			}

			if (!level.setBlock(sourceLocation, Blocks.AIR.defaultBlockState(), 2)) {

				return null;
			}

			return DropletMove.of(sourceIsGhost, sourceLocation, materialIndex, direction,
					destinationIsExternal, destinationLocation, MaterialTable.InvalidIndex, false);
		}
		else {

			if (!materialTable.list[materialIndex].solidIs) {

				// fluid with no air destination
				return null;
			}

			// something at destination, check if fluid to swap
			int destinationMaterial = materialTable.mobileStateToMaterial(destinationState);
			if (destinationMaterial == MaterialTable.InvalidIndex) {

				// not a drop
				// if vanilla fluid just overwrite without swap
				if (destinationState.getMaterial() == Material.WATER) {

					// normal overwrite move
					if (!level.setBlock(destinationLocation, newSourceState, 2)) {

						return null;
					}

					if (!level.setBlock(sourceLocation, Blocks.AIR.defaultBlockState(), 2)) {

						return null;
					}

					return DropletMove.of(sourceIsGhost, sourceLocation, materialIndex, direction,
							destinationIsExternal, destinationLocation, MaterialTable.InvalidIndex,
							false);
				}
				else {

					return null;
				}
			}
			else {

				if (materialTable.list[destinationMaterial].solidIs) {

					// solid destination
					return null;
				}

				// fluid destination
				if (!level.setBlock(destinationLocation, newSourceState, 2)) {

					return null;
				}

				if (!level.setBlock(sourceLocation, destinationState, 2)) {

					return null;
				}

				return DropletMove.of(sourceIsGhost, sourceLocation, materialIndex, direction,
						destinationIsExternal, destinationLocation, destinationMaterial, false);
			}
		}
	}

	public void dropPlace(BlockPos location) {

		int drop = Droplet.locationSetUnpack(Droplet.DefaultValue, location);

		int archiveDelta = configure.archivePeriod;
		drop = Droplet.timeSet(drop, archiveDelta);

		// should check if exist
		materialLevel.dropAdd(location, drop);
	}

	// input and output all in tick resolution
	public int dropGetArchiveDelta(long tick, int deltaTick, int expireTick) {

		// handles wrap to give a positive result
		int expireDelta = (expireTick - (int) tick) & Droplet.TimeMaximum;

		int inverseExpire = Droplet.TimeMaximum - expireDelta;

		if (deltaTick > inverseExpire) {

			// went past expire since last cycle
			return 0;
		}
		else {

			return expireDelta;
		}
	}

	public void networkAddMove(BlockPos sourceLocation, int startTimeDeltaExact, int energy,
			Direction direction) {

		// packet item
		int networkMove = DropNetwork.Default;

		networkMove = DropNetwork.locationSetUnpack(networkMove, sourceLocation);

		networkMove = DropNetwork.timeSet(networkMove, startTimeDeltaExact);

		networkMove = DropNetwork.energySet(networkMove, energy);

		int directionPack = Geometry.DirectionToPack(direction);
		networkMove = DropNetwork.directionSet(networkMove, directionPack);

		packetPool.currentSection.moveAdd(networkMove);
	}

	// one section per tick for now
	public void loadQueueProcess(ServerLevel level, long tick) {

		if (currentLoadSection == null) {

			currentLoadSection = loadQueue.tryConsume();

			if (currentLoadSection == null) {

				// complete
				return;
			}
		}

		LevelChunk chunk = level.getChunk(currentLoadSection.getX(), currentLoadSection.getZ());
		if (chunk == null) {

			// chunk gone
			currentLoadSection = null;
			return;
		}

		int sectionIndex = chunk.getSectionIndexFromSectionY(currentLoadSection.getY());
		if (sectionIndex < 0) {

			// section gone
			currentLoadSection = null;
			return;
		}

		// blindly process, section list is already filtered
		LevelChunkSection section = chunk.getSection(sectionIndex);

		loadQueueProcessSection(tick, currentLoadSection, section);

		// section complete
		currentLoadSection = null;
	}

	// process entire section at a time
	public void loadQueueProcessSection(long tick, SectionPos sectionLocation,
			LevelChunkSection section) {

		BlockPos sectionOrigin = sectionLocation.origin();

		// drop template
		int drop = Droplet.DefaultValue;

		int archiveDelta = configure.archivePeriod;
		drop = Droplet.timeSet(drop, archiveDelta);

		for (int x = 0; x < 16; x++) {

			for (int z = 0; z < 16; z++) {

				for (int y = 0; y < 16; y++) {

					BlockState state = section.getBlockState(x, y, z);
					if (state == materialTable.list[materialIndex].dropletState) {

						BlockPos location = sectionOrigin.offset(x, y, z);

						if (!materialLevel.dropIsExist(location)) {

							statistics.loadProcessDrop++;

							// update drop location
							drop = Droplet.locationSetUnpack(drop, location);

							materialLevel.dropAdd(location, drop);
						}
					}
				}
			}
		}
	}

	public void onSectionLoad(SectionPos section) {

		final boolean loadQueueIsEnable = materialTable.list[materialIndex].solidIs;
		if (!loadQueueIsEnable) {

			return;
		}

		if (loadQueue.tryAdd(section)) {

			// queue full ignore
		}
	}

	public ServerMaterialStatistics statisticsDerive() {

		statistics.levelGhost = materialLevel.ghostUseDerive();
		statistics.levelDrop = materialLevel.dropUseDerive();

		statistics.ghostProcessPerTickMean = ghostProcessHistory.mean();
		statistics.dropletProcessPerTickMean = dropletProcessHistory.mean();
		statistics.movePerTickMean = moveHistory.mean();

		// can't find a way to get real allocate size of fast map
		statistics.ghostAllocate = 0;
		statistics.dropAllocate = 0;

		return statistics;
	}
}