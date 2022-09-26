package com.synchrofield.library.math;

public class MathUtility {

	public static double normal(int numerator, int divisor) {

		assert divisor != 0;

		return capNormal((double) numerator / (double) divisor);
	}

	public static double capNormal(double item) {

		return cap(item, 0.0, 1.0);
	}

	public static double cap(double item, double minimum, double maximum) {

		return Math.min(Math.max(item, minimum), maximum);
	}

	// inclusive
	public static int cap(int item, int minimum, int maximum) {

		return Math.min(Math.max(item, minimum), maximum);
	}

	public static double cap(double item, double maximum) {

		return Math.min(Math.max(item, 0.0), maximum);
	}

	public static int cap(int item, int maximum) {

		return Math.min(Math.max(item, 0), maximum);
	}

	public static int positive(int item) {

		return (item >= 0) ? item : 0;
	}

	public static int msb(int item) {

		if (item == 0) {

			return -1;
		}

		return (32 - msbInverse(item)) - 1;
	}

	public static int msbInverse(int item) {

		return Integer.numberOfLeadingZeros(item);
	}

	// next higher power of 2
	public static int power2Above(int item) {

		return 1 << (MathUtility.msb(item) + 1);
	}

		public static boolean rangeCheck(int item, int endExclusive) {

		return (item >= 0) && (item < endExclusive);
	}
		
	// note this is exclusive end value
	public static boolean rangeCheck(int item, int start, int endExclusive) {

		return (item >= start) && (item < endExclusive);
	}

	// exclusive
	public static boolean rangeCheck(double item, double start, double endExclusive) {

		return (item >= start) && (item < endExclusive);
	}

	public static boolean rangeCheckInclusive(double item, double start, double endInclusive) {

		return (item >= start) && (item <= endInclusive);
	}

	// exclusive
	public static boolean rangeCheck(float item, float start, float endExclusive) {

		return (item >= start) && (item < endExclusive);
	}

	public static boolean rangeCheckInclusive(float item, float start, float endInclusive) {

		return (item >= start) && (item <= endInclusive);
	}
}
