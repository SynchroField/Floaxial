package com.synchrofield.floaxial.server.rule;

public class BlockCriteriaType {

	public static final int BitSize = 3;
	public static final int ListSize = 1 << BitSize;
	public static final int Maximum = ListSize - 1;

	public static final int BlockState = 0;
	public static final int BlockStateSubset = 1;
	public static final int Block = 2;
	public static final int Material = 3;
	public static final int FluidIs = 4;
	public static final int SolidIs = 5;
	public static final int HeavyIs = 6;
	public static final int ReplaceableIs = 7;

	public static String toString(int index) {

		switch (index) {

		default: {

			return "";
		}

		case BlockState: {

			return "BlockState";
		}

		case BlockStateSubset: {

			return "BlockStateSubset";
		}

		case Block: {

			return "Block";
		}

		case Material: {

			return "Material";
		}

		case FluidIs: {

			return "FluidIs";
		}

		case SolidIs: {

			return "SolidIs";
		}

		case HeavyIs: {

			return "HeavyIs";
		}

		case ReplaceableIs: {

			return "ReplaceableIs";
		}
		}
	}

	public static int fromString(String text) {

		for (int i = 0; i < ListSize; i++) {

			if (toString(i).equalsIgnoreCase(text)) {

				return i;
			}
		}

		return -1;
	}
}
