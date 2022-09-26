package com.synchrofield.floaxial.server.droplet;

import com.synchrofield.floaxial.central.droplet.MaterialTable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class DropletMove {

	public boolean sourceIsGhost;
	public BlockPos source;
	public int sourceDrop;

	public Direction direction;

	public boolean destinationIsExternal;
	public BlockPos destination;
	public int destinationMaterial;
	public boolean destinationIsArchive;

	protected DropletMove(boolean sourceIsGhost, BlockPos source, int sourceDrop, Direction direction,
			boolean destinationIsExternal, BlockPos destination, int destinationMaterial,
			boolean destinationIsArchive) {

		this.sourceIsGhost = sourceIsGhost;
		this.source = source;
		this.sourceDrop = sourceDrop;
		this.direction = direction;
		this.destinationIsExternal = destinationIsExternal;
		this.destination = destination;
		this.destinationMaterial = destinationMaterial;
		this.destinationIsArchive = destinationIsArchive;
	}

	public static DropletMove of() {

		return new DropletMove(false, null, 0, Direction.DOWN, false, null,
				MaterialTable.InvalidIndex, false);
	}

	public static DropletMove of(boolean sourceIsGhost, BlockPos source, int sourceDrop,
			Direction direction, boolean destinationIsExternal, BlockPos destination,
			int destinationMaterial, boolean destinationIsArchive) {

		return new DropletMove(sourceIsGhost, source, sourceDrop, direction, destinationIsExternal,
				destination, destinationMaterial, destinationIsArchive);
	}
}
