package com.synchrofield.floaxial.central.configure.store;

import java.util.Optional;

import com.synchrofield.floaxial.central.configure.ClientMaterialConfigure;
import com.synchrofield.floaxial.central.configure.MaterialConfigure;
import com.synchrofield.floaxial.central.configure.ServerMaterialConfigure;
import com.synchrofield.floaxial.central.droplet.MaterialTable;
import com.synchrofield.library.configure.ConfigureStore;
import com.synchrofield.library.math.MathUtility;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

// droplet per material configuration storage
public class MaterialStore extends ConfigureStore {

	// append index to get filename
	public static final String FilenameBase = "material";

	public final int materialIndex;

	public final ConfigValue<Double> timeScale;
	public final ConfigValue<Integer> energyMaximum;
	public final ConfigValue<Integer> ghostProcessPeriod;
	public final ConfigValue<Integer> mobileProcessPeriod;
	public final ConfigValue<Integer> archivePeriod;
	public final ConfigValue<Integer> packetSize;
	public final ConfigValue<Integer> loadSectionSize;
	public final ConfigValue<Integer> loadLocationPerTick;
	public final ConfigValue<Integer> animateSize;
	public final ConfigValue<Integer> ghostLookahead;
	public final ConfigValue<String> mobileState;
	public final ConfigValue<Boolean> mobileStateIsSubset;
	public final ConfigValue<String> renderState;
	public final ConfigValue<Integer> holeRadius;
	public final ConfigValue<Integer> pyramidSizeY;
	public final ConfigValue<Boolean> denseIs;

	protected MaterialStore(Optional<String> folder, String filename,
			ForgeConfigSpec.Builder builder, int materialIndex) {

		super(folder, filename, builder);

		assert MathUtility.rangeCheck(materialIndex, MaterialTable.Size);

		this.materialIndex = materialIndex;

		// drop
		this.builder.push("Drop");

		this.timeScale = propertyCreateDoubleFromFloat("TimeScale",
				MaterialConfigure.TimeScaleMinimum, MaterialConfigure.TimeScaleMaximum,
				MaterialConfigure.DefaultValue[materialIndex].timeScale,
				"General movement speed of the material.");

		this.energyMaximum = propertyCreateInteger("EnergyMaximum", MaterialConfigure.EnergyMinimum,
				MaterialConfigure.EnergyMaximum,
				MaterialConfigure.DefaultValue[materialIndex].energyMaximum,
				"Maximum energy.  As droplets fall they gain 1 energy unit for each block distance.");

		this.ghostProcessPeriod = propertyCreateInteger("GhostProcessPeriod",
				ServerMaterialConfigure.GhostProcessPeriodMinimum,
				ServerMaterialConfigure.GhostProcessPeriodMaximum,
				ServerMaterialConfigure.DefaultValue[materialIndex].ghostProcessPeriod,
				"Process ghost droplet every N tick."
						+ " Ghosts need to be serviced more often than regular mobile drops."
						+ " If this value is too high the falling animation will stutter because the client doesn't receive it in time.");

		this.mobileProcessPeriod = propertyCreateInteger("MobileProcessPeriod",
				ServerMaterialConfigure.MobileProcessPeriodMinimum,
				ServerMaterialConfigure.MobileProcessPeriodMaximum,
				ServerMaterialConfigure.DefaultValue[materialIndex].mobileProcessPeriod,
				"Process regular mobile drop every N tick.  High values are recommended but droplets will decide their movements more slowly.");

		this.archivePeriod = propertyCreateInteger("ArchivePeriod",
				ServerMaterialConfigure.ArchivePeriodMinimum,
				ServerMaterialConfigure.ArchivePeriodMaximum,
				ServerMaterialConfigure.DefaultValue[materialIndex].archivePeriod,
				"Time before declaring a mobile droplet as stationary.  It is then treated as a regular block with no physics applied.");

		this.packetSize = propertyCreateInteger("PacketSize",
				ServerMaterialConfigure.PacketSizeMinimum,
				ServerMaterialConfigure.PacketSizeMaximum,
				ServerMaterialConfigure.PacketSizeDefault, "Maximum size of each packet in byte.");

		this.loadSectionSize = propertyCreateInteger("LoadSectionSize",
				ServerMaterialConfigure.LoadSectionSizeMinimum,
				ServerMaterialConfigure.LoadSectionSizeMaximum,
				ServerMaterialConfigure.DefaultValue[materialIndex].loadSectionSize,
				"Chunk sections (16 blocks cubed) are recorded during load if they contain droplets."
						+ " Then later scanned in the background. The queue holds section locations which are 8 byte.");

		this.loadLocationPerTick = propertyCreateInteger("LoadLocationPerTick",
				ServerMaterialConfigure.LoadPerTickMinimum,
				ServerMaterialConfigure.LoadPerTickMaximum,
				ServerMaterialConfigure.DefaultValue[materialIndex].loadLocationPerTick,
				"Sections are scanned for droplets which are then added to the physics engine."
						+ " Energy is reset to zero for all droplets upon level load.");

		this.ghostLookahead = propertyCreateInteger("GhostLookahead",
				ServerMaterialConfigure.GhostLookaheadMinimum,
				ServerMaterialConfigure.GhostLookaheadMaximum,
				ServerMaterialConfigure.DefaultValue[materialIndex].ghostLookahead,
				"The distance a ghost may travel ahead of the animated block."
						+ " A value of 10 - 30 tick is recommend to ensure smooth client animation."
						+ " A high value will cause the droplet to run ahead on server and affect collision physics.");

		this.animateSize = propertyCreateInteger("AnimateSize",
				ClientMaterialConfigure.AnimateSizeMinimum,
				ClientMaterialConfigure.AnimateSizeMaximum,
				ClientMaterialConfigure.DefaultValue[materialIndex].animateSize,
				"Limit how many moves can be displayed on client for this droplet material.  All moves still happen on server regardless.");

		this.mobileState = propertyCreateBlockState("MobileState",
				MaterialConfigure.DefaultValue[materialIndex].mobileState,
				"Block state for this material in the form \"mod:block[property=value]\".");

		this.mobileStateIsSubset = propertyCreateBoolean("MobileStateIsSubset",
				MaterialConfigure.DefaultValue[materialIndex].mobileStateIsSubset,
				"Ignore any block state properties that are not specified."
						+ " For example the 'distance' property of leaves is not compared if it's not in criteria.");

		this.renderState = propertyCreateBlockState("RenderState",
				MaterialConfigure.DefaultValue[materialIndex].renderState,
				"Block state used for the droplet client animation.");

		this.holeRadius = propertyCreateInteger("HoleRadius", MaterialConfigure.HoleRadiusMinimum,
				MaterialConfigure.HoleRadiusMaximum,
				MaterialConfigure.DefaultValue[materialIndex].holeRadius,
				"Hole search radius.  0 means it never tries to go sideways."
						+ " High values cause server load due to path searching.");

		this.pyramidSizeY = propertyCreateInteger("PyramidSizeY",
				MaterialConfigure.PyramidSizeYMinimum, MaterialConfigure.PyramidSizeYMaximum,
				MaterialConfigure.DefaultValue[materialIndex].pyramidSizeY,
				"Height the block will stack before falling off the column.");

		this.denseIs = propertyCreateBoolean("DenseIs",
				MaterialConfigure.DefaultValue[materialIndex].denseIs,
				"Dense droplets are able to sink in non-dense (e.g. stone falling in water).");

		this.builder.pop();

		this.spec = this.builder.build();
	}

	public static MaterialStore of(Optional<String> folder, String filename, int materialIndex) {

		return new MaterialStore(folder, filename, builderCreate(), materialIndex);
	}
}
