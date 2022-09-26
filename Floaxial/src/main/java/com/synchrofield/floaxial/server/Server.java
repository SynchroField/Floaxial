package com.synchrofield.floaxial.server;

import javax.annotation.Nullable;

import com.synchrofield.floaxial.central.CentralData;
import com.synchrofield.floaxial.central.configure.MaterialConfigureList;
import com.synchrofield.floaxial.central.configure.ServerConfigure;
import com.synchrofield.floaxial.central.statistics.ServerStatistics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.network.simple.SimpleChannel;

public class Server implements MaterialPlaceReceive {

	@Nullable
	protected Level levelControl;

	protected Server() {

	}

	public static Server of() {

		return new Server();
	}

	public void reset(ServerConfigure configure, MaterialConfigureList commonMaterialConfigure,
			SimpleChannel channel, CentralData centralData) {

		levelControl = Level.of(configure.level, commonMaterialConfigure, channel, centralData,
				this);
	}

	public boolean levelCheck() {

		return levelControl != null;
	}

	public void onTick(ServerLevel level) {

		if (!levelCheck()) {

			return;
		}

		// ensure at least one player on server
		if (level.getRandomPlayer() != null) {

			levelControl.onTick(level);
		}
	}

	public void onBlockPlace(ServerLevel level, BlockPos location, BlockState state) {

		if (!levelCheck()) {

			return;
		}

		levelControl.onBlockPlace(level, location, state);
	}

	public ServerStatistics statisticsDerive(ServerLevel level) {

		assert levelCheck();

		ServerStatistics result = ServerStatistics.of();

		result.level = levelControl.statisticsDerive(level);

		return result;
	}

	public Level levelControl() {

		assert levelCheck();

		return levelControl;
	}

	public void rainStart(ServerLevel level, int timeSize) {

		assert levelCheck();

		levelControl.rainStart(level, timeSize);
	}

	public void onGap(BlockPos location) {

		assert levelCheck();

		levelControl.onGap(location);
	}

	public void onSectionLoad(SectionPos location, LevelChunkSection section) {

		assert levelCheck();

		levelControl.onSectionLoad(location, section);
	}

	public boolean sectionIsContainDrop(LevelChunkSection section) {

		assert levelCheck();

		return levelControl.sectionIsContainDrop(section);
	}

	@Override
	public void onRulePlaceMaterial(BlockPos location, int ruleIndex, int outputIndex) {

		assert levelCheck();

		levelControl.onRulePlaceMaterial(location, ruleIndex, outputIndex);
	}

	@Override
	public void onDropArchive(BlockPos location, int materialIndex) {

		assert levelCheck();

		levelControl.onDropArchive(location, materialIndex);

	}

}
