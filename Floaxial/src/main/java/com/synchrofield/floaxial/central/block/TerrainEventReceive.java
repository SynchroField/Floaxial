package com.synchrofield.floaxial.central.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

// receive events for terrain changes
public interface TerrainEventReceive {

	void onBlockPlace(ServerLevel level, BlockPos location, BlockState state);
}
