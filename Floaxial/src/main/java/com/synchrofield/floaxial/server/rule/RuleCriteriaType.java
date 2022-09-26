package com.synchrofield.floaxial.server.rule;

import com.synchrofield.library.math.MathUtility;

public class RuleCriteriaType {

	public static final int BitSize = 2;
	public static final int Size = (1 << BitSize);
	public static final int Maximum = Size - 1;

	public static final int Center = 0;
	public static final int Touch = 1;
	public static final int Row = 2;
	public static final int Sample = 3;

	public static String toString(int index) {

		assert BitSize > 0;
		assert indexCheck(index);

		switch (index) {

		default:
		case 0: {

			return "center";
		}

		case 1: {

			return "touch";
		}

		case 2: {

			return "row";
		}

		case 3: {

			return "sample";
		}
		}
	}

	// return -1 if not found
	public static int fromString(String rawText) {

		String text = rawText.trim();

		for (int i = 0; i < Size; i++) {

			if (toString(i).equalsIgnoreCase(text)) {

				return i;
			}
		}

		return -1;
	}

	public static boolean indexCheck(int index) {

		return MathUtility.rangeCheck(index, Size);
	}
}
