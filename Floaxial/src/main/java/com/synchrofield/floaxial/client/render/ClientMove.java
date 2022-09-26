package com.synchrofield.floaxial.client.render;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

// display smooth movement from one block location to another
public class ClientMove {

	// array slot not in use
	public boolean existIs;

	public BlockPos sourceLocation;

	public int energyStart;
	public Direction direction;

	public float timeStart;

	protected ClientMove() {

		this.existIs = false;

		this.sourceLocation = BlockPos.ZERO;
		this.direction = Direction.NORTH;

		this.timeStart = 0.0f;
	}

	public static ClientMove of() {

		return new ClientMove();
	}

	public BlockPos destinationLocationDerive() {

		return sourceLocation.relative(direction, 1);
	}

}
