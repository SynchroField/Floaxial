package com.synchrofield.floaxial.central.configure;

public class ProductConfigure {

	public static final String Name = "floaxial";
	public static final String NameDisplay = "Floaxial";

	public static final String VersionType = "alpha";
	public static final int VersionMajor = 0;
	public static final int VersionMinor = 3;
	public static final String VersionDisplay = VersionMajor + "." + VersionMinor + "."
			+ VersionType;
	public static final String VersionDate = "2022Sep26";

	public static final String VanillaVersionDisplay = "1.18.2";
	public static final String VanillaName = "minecraft";

	protected ProductConfigure() {

	}

	public static ProductConfigure of() {

		return new ProductConfigure();
	}
}
