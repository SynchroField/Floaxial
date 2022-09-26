package com.synchrofield.floaxial.central.configure;

public class ClientMaterialConfigure {

	public static final int AnimateSizeMinimum = 1;
	public static final int AnimateSizeMaximum = 1024 * 1024;
	public static final int AnimateSizeDefault = 1024;

	public static final ClientMaterialConfigure Default0 = ClientMaterialConfigure.of()
			.withAnimateSize(1024);
	public static final ClientMaterialConfigure Default1 = ClientMaterialConfigure.of()
			.withAnimateSize(256);
	public static final ClientMaterialConfigure Default2 = ClientMaterialConfigure.of()
			.withAnimateSize(512);
	public static final ClientMaterialConfigure Default3 = ClientMaterialConfigure.of()
			.withAnimateSize(256);
	public static final ClientMaterialConfigure Default4 = ClientMaterialConfigure.of()
			.withAnimateSize(64);
	public static final ClientMaterialConfigure Default5 = ClientMaterialConfigure.of()
			.withAnimateSize(64);
	public static final ClientMaterialConfigure Default6 = ClientMaterialConfigure.of()
			.withAnimateSize(64);
	public static final ClientMaterialConfigure Default7 = ClientMaterialConfigure.of()
			.withAnimateSize(64);

	public static final ClientMaterialConfigure DefaultValue[] = {

			Default0, Default1, Default2, Default3, Default4, Default5, Default6, Default7,
	};

	public final int animateSize;

	protected ClientMaterialConfigure(int animateSize) {

		this.animateSize = animateSize;
	}

	public static ClientMaterialConfigure of() {

		return new ClientMaterialConfigure(AnimateSizeDefault);
	}

	public ClientMaterialConfigure withAnimateSize(int animateSize) {

		return new ClientMaterialConfigure(animateSize);
	}
}
