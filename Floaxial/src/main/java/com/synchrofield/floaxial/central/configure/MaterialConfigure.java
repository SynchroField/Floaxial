package com.synchrofield.floaxial.central.configure;

import com.synchrofield.floaxial.server.droplet.Droplet;

// common to client and server
public class MaterialConfigure {

	public static final float TimeScaleMinimum = 0.001f;
	public static final float TimeScaleMaximum = 100.0f;
	public static final float TimeScaleDefault = 0.1f;

	public static final int EnergyMinimum = 1;
	public static final int EnergyMaximum = Droplet.EnergyMaximum;
	public static final int EnergyDefault = EnergyMaximum;

	public static final String MobileStateDefault = "minecraft:stone";
	public static final boolean MobileStateIsSubsetDefault = false;
	public static final String RenderStateDefault = "minecraft:stone";

	public static final int HoleRadiusMinimum = 0;
	public static final int HoleRadiusMaximum = 1;
	public static final int HoleRadiusDefault = 1;

	public static final int PyramidSizeYMinimum = 1;
	public static final int PyramidSizeYMaximum = 8;
	public static final int PyramidSizeYDefault = PyramidSizeYMinimum;

	public static final boolean DenseIsDefault = true;

	public static final MaterialConfigure Default0 = MaterialConfigure.of()
			.withTimeScale(0.075f)
			.withMobileState("floaxial:salt_water")
			.withRenderState("floaxial:salt_water_render")
			.withHoleRadius(1)
			.withPyramidSizeY(1)
			.withDenseIs(false);

	public static final MaterialConfigure Default1 = MaterialConfigure.of()
			.withTimeScale(0.1f)
			.withMobileState("minecraft:stone")
			.withRenderState("minecraft:stone")
			.withHoleRadius(0)
			.withPyramidSizeY(1);

	public static final MaterialConfigure Default2 = MaterialConfigure.of()
			.withTimeScale(0.1f)
			.withMobileState("floaxial:vanilla_sand")
			.withRenderState("floaxial:vanilla_sand")
			.withHoleRadius(1)
			.withPyramidSizeY(1);

	public static final MaterialConfigure Default3 = MaterialConfigure.of()
			.withTimeScale(0.1f)
			.withMobileState("minecraft:dirt")
			.withRenderState("minecraft:dirt")
			.withHoleRadius(1)
			.withPyramidSizeY(1);

	public static final MaterialConfigure Default4 = MaterialConfigure.of()
			.withTimeScale(0.1f)
			.withMobileState("minecraft:grass_block")
			.withRenderState("minecraft:grass_block")
			.withHoleRadius(1)
			.withPyramidSizeY(1);

	public static final MaterialConfigure Default5 = MaterialConfigure.of()
			.withTimeScale(0.05f)
			.withMobileState("floaxial:vanilla_jungle_leaf[persistent=true]")
			.withMobileStateIsSubset(true)
			.withRenderState("floaxial:vanilla_jungle_leaf")
			.withHoleRadius(1)
			.withPyramidSizeY(2);

	public static final MaterialConfigure Default6 = MaterialConfigure.of()
			.withTimeScale(0.1f)
			.withMobileState("floaxial:vanilla_gravel")
			.withRenderState("floaxial:vanilla_gravel")
			.withHoleRadius(1)
			.withPyramidSizeY(1);

	public static final MaterialConfigure Default7 = MaterialConfigure.of()
			.withTimeScale(0.1f)
			.withMobileState("minecraft:clay")
			.withRenderState("minecraft:clay")
			.withHoleRadius(1)
			.withPyramidSizeY(2);

	public static final MaterialConfigure DefaultValue[] = {

			Default0, Default1, Default2, Default3, Default4, Default5, Default6, Default7,
	};

	public final float timeScale;
	public final int energyMaximum;
	public final String mobileState;
	public final boolean mobileStateIsSubset;
	public final String renderState;
	public final int holeRadius;
	public final int pyramidSizeY;
	public final boolean denseIs;

	protected MaterialConfigure(float timeScale, int energyMaximum, String mobileState,
			boolean mobileStateIsSubset, String renderState, int holeRadius, int pyramidSizeY,
			boolean denseIs) {

		this.timeScale = timeScale;
		this.energyMaximum = energyMaximum;
		this.mobileState = mobileState;
		this.mobileStateIsSubset = mobileStateIsSubset;
		this.renderState = renderState;
		this.holeRadius = holeRadius;
		this.pyramidSizeY = pyramidSizeY;
		this.denseIs = denseIs;
	}

	public static MaterialConfigure of() {

		return new MaterialConfigure(TimeScaleDefault, EnergyDefault, MobileStateDefault,
				MobileStateIsSubsetDefault, RenderStateDefault, HoleRadiusDefault,
				PyramidSizeYDefault, DenseIsDefault);
	}

	public MaterialConfigure withTimeScale(float timeScale) {

		return new MaterialConfigure(timeScale, energyMaximum, mobileState, mobileStateIsSubset,
				renderState, holeRadius, pyramidSizeY, denseIs);
	}

	public MaterialConfigure withEnergyMaximum(int energyMaximum) {

		return new MaterialConfigure(timeScale, energyMaximum, mobileState, mobileStateIsSubset,
				renderState, holeRadius, pyramidSizeY, denseIs);
	}

	public MaterialConfigure withMobileState(String mobileState) {

		return new MaterialConfigure(timeScale, energyMaximum, mobileState, mobileStateIsSubset,
				renderState, holeRadius, pyramidSizeY, denseIs);
	}

	public MaterialConfigure withMobileStateIsSubset(boolean mobileStateIsSubset) {

		return new MaterialConfigure(timeScale, energyMaximum, mobileState, mobileStateIsSubset,
				renderState, holeRadius, pyramidSizeY, denseIs);
	}

	public MaterialConfigure withRenderState(String renderState) {

		return new MaterialConfigure(timeScale, energyMaximum, mobileState, mobileStateIsSubset,
				renderState, holeRadius, pyramidSizeY, denseIs);
	}

	public MaterialConfigure withHoleRadius(int holeRadius) {

		return new MaterialConfigure(timeScale, energyMaximum, mobileState, mobileStateIsSubset,
				renderState, holeRadius, pyramidSizeY, denseIs);
	}

	public MaterialConfigure withPyramidSizeY(int pyramidSizeY) {

		return new MaterialConfigure(timeScale, energyMaximum, mobileState, mobileStateIsSubset,
				renderState, holeRadius, pyramidSizeY, denseIs);
	}

	public MaterialConfigure withDenseIs(boolean denseIs) {

		return new MaterialConfigure(timeScale, energyMaximum, mobileState, mobileStateIsSubset,
				renderState, holeRadius, pyramidSizeY, denseIs);
	}
}
