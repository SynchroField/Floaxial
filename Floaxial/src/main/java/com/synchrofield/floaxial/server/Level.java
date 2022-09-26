package com.synchrofield.floaxial.server;

import com.synchrofield.floaxial.central.CentralData;
import com.synchrofield.floaxial.central.configure.MaterialConfigureList;
import com.synchrofield.floaxial.central.configure.ServerLevelConfigure;
import com.synchrofield.floaxial.central.statistics.ServerLevelStatistics;
import com.synchrofield.floaxial.server.droplet.DropletControl;
import com.synchrofield.floaxial.server.rule.RuleControlTable;
import com.synchrofield.floaxial.server.rule.RuleLevelTable;
import com.synchrofield.floaxial.server.rule.RuleMaterialMap;
import com.synchrofield.floaxial.server.rule.RuleTable;
import com.synchrofield.library.math.MathUtility;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.network.simple.SimpleChannel;

public class Level {

	public final DropletControl dropControl;

	public final RuleTable ruleTable;
	public final RuleMaterialMap ruleMaterialMap;
	public final RuleLevelTable ruleLevelTable;
	public final RuleControlTable ruleControlTable;

	public ServerLevelConfigure configure;

	protected Level(ServerLevelConfigure configure, SimpleChannel channel,
			DropletControl dropControl, RuleTable ruleTable, RuleMaterialMap ruleMaterialMap,
			RuleLevelTable ruleLevelTable, RuleControlTable ruleControlTable) {

		this.configure = configure;
		this.dropControl = dropControl;
		this.ruleTable = ruleTable;
		this.ruleMaterialMap = ruleMaterialMap;
		this.ruleLevelTable = ruleLevelTable;
		this.ruleControlTable = ruleControlTable;
	}

	public static Level of(ServerLevelConfigure configure,
			MaterialConfigureList commonMaterialConfigure, SimpleChannel channel,
			CentralData centralData, MaterialPlaceReceive materialPlaceReceive) {

		RuleTable ruleTable = RuleTable.of(configure.rule);

		RuleMaterialMap ruleMaterialMap = RuleMaterialMap.of(ruleTable, centralData.materialTable);

		RuleLevelTable ruleLevelTable = RuleLevelTable.of();

		DropletControl dropControl = DropletControl.of(configure.drop, commonMaterialConfigure,
				channel, centralData.materialTable, materialPlaceReceive);

		RuleControlTable ruleControlList = RuleControlTable.of(ruleTable, ruleLevelTable,
				materialPlaceReceive);

		return new Level(configure, channel, dropControl, ruleTable, ruleMaterialMap,
				ruleLevelTable, ruleControlList);
	}

	public void onTick(ServerLevel level) {

		if (level.getServer()
				.getPlayerCount() <= 0) {

			return;
		}

		long tick = level.getGameTime();

		// drop
		dropControl.tick(level);

		// rain
		ServerPlayer player = level.getRandomPlayer();
		if (player == null) {

			// no players on server
			return;
		}

		dropControl.rainTick(level, tick, player.blockPosition());

		// rule
		ruleControlTable.onTick(level);
	}

	public void onBlockPlace(ServerLevel level, BlockPos location, BlockState state) {

		dropControl.onBlockPlace(location, state);

		ruleControlTable.onBlockPlace(level, location, state);
	}

	public ServerLevelStatistics statisticsDerive(ServerLevel level) {

		ServerLevelStatistics result = ServerLevelStatistics.of();

		result.tick = level.getGameTime();
		result.drop = dropControl.statisticsDerive();
		result.rule = ruleControlTable.statisticsDerive();

		return result;
	}

	public void onGap(BlockPos location) {

		dropControl.onGap(location);
	}

	public boolean sectionIsContainDrop(LevelChunkSection section) {

		return dropControl.sectionIsContainDrop(section);
	}

	public void onSectionLoad(SectionPos location, LevelChunkSection section) {

		dropControl.onSectionLoad(location, section);

		ruleControlTable.onSectionLoad(location, section);
	}

	public void rainStart(ServerLevel level, int timeSize) {

		dropControl.rainStart(level, timeSize);
	}

	// blindly place
	public void onRulePlaceMaterial(BlockPos location, int ruleIndex, int outputIndex) {

		int materialIndex;
		int destinationRuleField;
		if (outputIndex == 0) {

			materialIndex = ruleMaterialMap.ruleOutput0ToMaterial[ruleIndex];
			destinationRuleField = ruleMaterialMap.ruleOutput0ToRule[ruleIndex];
		}
		else {

			materialIndex = ruleMaterialMap.ruleOutput1ToMaterial[ruleIndex];
			destinationRuleField = ruleMaterialMap.ruleOutput1ToRule[ruleIndex];
		}

		// rule to material
		if (materialIndex != -1) {

			dropControl.materialControlList.list[materialIndex].dropPlace(location);
		}

		// rule output to rule input
		while (destinationRuleField != 0) {

			int destinationRuleIndex = MathUtility.msb(destinationRuleField);
			assert destinationRuleIndex != -1;

			// clear bit
			destinationRuleField &= (~(1 << destinationRuleIndex));

			ruleControlTable.list[destinationRuleIndex].onMaterialPlace(location);
		}
	}

	// blindly place
	public void onDropArchive(BlockPos location, int materialIndex) {

		int ruleField = ruleMaterialMap.materialToRuleInput[materialIndex];

		while (ruleField != 0) {

			int destinationRuleIndex = MathUtility.msb(ruleField);
			assert destinationRuleIndex != -1;

			// clear bit
			ruleField &= (~(1 << destinationRuleIndex));

			ruleControlTable.list[destinationRuleIndex].onMaterialPlace(location);
		}
	}
}
