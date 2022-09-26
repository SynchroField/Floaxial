package com.synchrofield.library.configure;

public class GenerateConfigure {

	public static final boolean EnableIsDefault = true;

	public final boolean enableIs;

	protected GenerateConfigure(boolean enableIs) {

		this.enableIs = enableIs;
	}

	public static GenerateConfigure of() {

		return new GenerateConfigure(EnableIsDefault);
	}

	public GenerateConfigure withEnableIs(boolean enableIs) {

		return new GenerateConfigure(enableIs);
	}
}
