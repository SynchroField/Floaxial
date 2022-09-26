package com.synchrofield.floaxial.central.configure.store;

import java.util.Optional;

import com.synchrofield.floaxial.central.configure.ClientDropletConfigure;
import com.synchrofield.floaxial.central.configure.ProductConfigure;
import com.synchrofield.floaxial.central.configure.ServerDropletConfigure;
import com.synchrofield.library.configure.GenerateConfigure;
import com.synchrofield.library.configure.ConfigureStore;

import net.minecraftforge.common.ForgeConfigSpec;

// general configuration storage
public class GeneralStore extends ConfigureStore {

	public static final String Filename = ProductConfigure.Name + "." + FileExtension;

	public final ForgeConfigSpec.ConfigValue<Boolean> generateIsEnable;
	public final ForgeConfigSpec.ConfigValue<Integer> gapSize;
	public final ForgeConfigSpec.ConfigValue<Integer> gapPerTick;
	public final ForgeConfigSpec.ConfigValue<Boolean> ghostIsVisible;

	protected GeneralStore(Optional<String> folder, String filename,
			ForgeConfigSpec.Builder builder) {

		super(folder, filename, builder);

		this.builder.push("General");

		this.generateIsEnable = propertyCreateBoolean("GenerateIsEnable",
				GenerateConfigure.EnableIsDefault, "Enable island terrain generation.");

		this.gapSize = propertyCreateInteger("GapSize", ServerDropletConfigure.GapSizeMinimum,
				ServerDropletConfigure.GapPerTickMaximum, ServerDropletConfigure.GapSizeDefault,
				"When a block moves it leaves behind a hole that other blocks might fall into."
						+ " The hole locations get recorded and processed.  Each entry in the queue 8 byte.");

		this.gapPerTick = propertyCreateInteger("GapPerTick",
				ServerDropletConfigure.GapPerTickMinimum, ServerDropletConfigure.GapPerTickMaximum,
				ServerDropletConfigure.GapPerTickDefault,
				"Limit the hole filling rate.  1 second = 20 tick.  High values cause server load.");

		this.ghostIsVisible = propertyCreateBoolean("GhostIsVisible",
				ClientDropletConfigure.GhostIsVisibleDefault,
				"Display block at destination instantly before it actually gets there.  For debugging.");

		this.builder.pop();

		this.spec = this.builder.build();
	}

	public static GeneralStore of(Optional<String> folder) {

		return new GeneralStore(folder, Filename, builderCreate());
	}
}
