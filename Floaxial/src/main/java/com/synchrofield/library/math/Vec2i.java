package com.synchrofield.library.math;

import net.minecraft.core.BlockPos;

public class Vec2i {

	public int x;
	public int y;

	protected Vec2i(int x, int y) {

		this.x = x;
		this.y = y;
	}

	public static Vec2i of() {

		return new Vec2i(0, 0);
	}

	public static Vec2i ofX(int x) {

		return new Vec2i(x, 0);
	}

	public static Vec2i ofY(int y) {

		return new Vec2i(0, y);
	}

	public static Vec2i of(int x, int y) {

		return new Vec2i(x, y);
	}

	public BlockPos toBlockLocationXz() {

		return new BlockPos(x, 0, y);
	}
}
