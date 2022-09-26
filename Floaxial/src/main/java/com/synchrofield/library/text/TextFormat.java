package com.synchrofield.library.text;

public class TextFormat {

	public static String FormatDoubleDefault = "%,.2f";
	public static String FormatFloatDefault = "%,.2f";

	public static String formatDouble(double item) {

		return String.format(FormatDoubleDefault, item);
	}
	
		public static String formatFloat(float item) {

		return String.format(FormatFloatDefault, item);
	}
}
