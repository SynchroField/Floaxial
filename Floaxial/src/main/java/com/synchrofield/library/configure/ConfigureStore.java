package com.synchrofield.library.configure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import javax.annotation.Nullable;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.base.Predicate;
import com.synchrofield.library.math.MathUtility;
import com.synchrofield.library.text.TextFormat;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.loading.FMLPaths;

// Forge toml configuration file.
//
// Subclass needs to build spec at end of constructor after property list
// is created.
//
// Save-tracking disabled.
public abstract class ConfigureStore {

	public static final String FileExtension = "toml";

	public final Optional<String> folder;

	// include extension
	public final String filename;

	public final ForgeConfigSpec.Builder builder;
	public ForgeConfigSpec spec;
	public CommentedFileConfig model;

	protected ConfigureStore(Optional<String> folder, String filename,
			ForgeConfigSpec.Builder builder) {

		this.folder = folder;
		this.filename = filename;
		this.builder = builder;
	}

	protected static ForgeConfigSpec.Builder builderCreate() {

		return new ForgeConfigSpec.Builder();
	}

	// assume not exist
	public void createDefaultFile(CommentedFileConfig defaultModel, Path file) {

		try {

			Path path = pathDerive();

			if (Files.exists(path)) {

				throw new ConfigureException(
						"Unable to create default file, \"" + path + "\" already exist.");
			}

			Files.createDirectories(file.getParent());
			Files.createFile(file);

			defaultModel.configFormat()
					.initEmptyFile(file);

			// assume default is always in range
			spec.correct(defaultModel, EmptyCorrectionListener.of(), EmptyCorrectionListener.of());

			defaultModel.save();
		}
		catch (IOException e) {

			throw new ConfigureException("Unable to create default file. " + e.getMessage());
		}
	}

	// return first key with bad value
	@Nullable
	public Optional<String> validate(CommentedFileConfig candidateModel) {

		if (spec.isCorrect(candidateModel)) {

			return Optional.empty();
		}
		else {

			FirstCorrectionListener correctionListener = FirstCorrectionListener.of();

			spec.correct(candidateModel, correctionListener, EmptyCorrectionListener.of());

			if (correctionListener.errorKey.isEmpty()) {

				// somehow didn't find the bad key
				return Optional.of("[unknown key]");
			}

			return correctionListener.errorKey;
		}
	}

	public void copyFromFile() {

		try {

			Path path = pathDerive();

			CommentedFileConfig newModel = CommentedFileConfig.builder(path)

					.preserveInsertionOrder()

					// blocking write
					.sync()
					.writingMode(WritingMode.REPLACE)

					// runs during model load
					.onFileNotFound((newfile, configFormat) -> {

						throw new ConfigureException("File \"" + path + "\" not exist.");
					})

					.build();

			if (!Files.exists(path)) {

				createDefaultFile(newModel, path);
			}

			// exception if value has invalid type (e.g. integer instead of boolean)
			newModel.load();

			// exception if value is correct type but out of range
			Optional<String> errorKey = validate(newModel);
			if (!errorKey.isEmpty()) {

				throw new ConfigureException("Value out of range for [" + errorKey.get()
						+ "] in file \"" + filename + "\".");
			}

			// file is good
			this.model = newModel;

			spec.acceptConfig(this.model);
		}
		catch (ParsingException ex) {

			throw new ConfigureException(ex.getMessage() + " in file \"" + filename + "\". ");
		}
	}

	public void copyToFile() {

		model.save();
	}

	public String rangeToString(int minimum, int maximum, int defaultValue) {

		return new String(
				"From [" + minimum + "] to [" + maximum + "] default [" + defaultValue + "].");
	}

	public String rangeToString(float minimum, float maximum, float defaultValue) {

		return new String("From [" + TextFormat.formatFloat(minimum) + "] to ["
				+ TextFormat.formatFloat(maximum) + "] default ["
				+ TextFormat.formatFloat(defaultValue) + "].");
	}

	public ConfigValue<Boolean> propertyCreateBoolean(String name, boolean defaultValue,
			String comment) {

		final Predicate<Object> validate = (o) -> {

			if (o instanceof Boolean) {

				return true;
			}

			return false;
		};

		String fullComment = " " + comment + " Default [" + defaultValue + "].";

		return builder.comment(fullComment)
				.define(name, defaultValue, validate);
	}

	// maximum inclusive 
	public ConfigValue<Integer> propertyCreateInteger(String name, int minimum,
			int maximumInclusive, int defaultValue, String comment) {

		final Predicate<Object> validate = (o) -> {

			if (o instanceof Integer) {

				return MathUtility.rangeCheckInclusive((Integer) o, minimum, maximumInclusive);
			}

			return false;
		};

		String fullComment = " " + comment + " "
				+ rangeToString(minimum, maximumInclusive, defaultValue);

		return builder.comment(fullComment)
				.define(name, defaultValue, validate);
	}

	// stores the float as a double
	// maximum inclusive 
	public ConfigValue<Double> propertyCreateDoubleFromFloat(String name, float minimum,
			float maximumInclusive, float defaultValue, String comment) {

		Double minimumDouble = ((Float) minimum).doubleValue();
		Double maximumDouble = ((Float) maximumInclusive).doubleValue();
		Double defaultDouble = ((Float) defaultValue).doubleValue();

		final Predicate<Object> validate = (o) -> {

			// comes in as a double not as float
			if ((o instanceof Double)) {

				return MathUtility.rangeCheckInclusive((Double) o, minimumDouble, maximumDouble);
			}

			return false;
		};

		String fullComment = " " + comment + " "
				+ rangeToString(minimum, maximumInclusive, defaultValue);

		return builder.comment(fullComment)
				.define(name, defaultDouble, validate);
	}

	public ConfigValue<String> propertyCreateString(String name, String defaultValue,
			String comment) {

		final Predicate<Object> validate = (o) -> {

			if (o instanceof String) {

				return true;
			}

			return false;
		};

		String fullComment = " " + comment + " Default [" + defaultValue + "].";

		return builder.comment(fullComment)
				.define(name, defaultValue, validate);
	}

	public ConfigValue<String> propertyCreateBlockState(String name, String defaultValue,
			String comment) {

		final Predicate<Object> validate = (o) -> {

			if (o instanceof String) {

				// this is called before block registry is created, so proper validate of block states must be done
				// separately after mod initialize
				return true;
			}

			return false;
		};

		String fullComment = " " + comment + " Default [" + defaultValue + "].";

		return builder.comment(fullComment)
				.define(name, defaultValue, validate);
	}

	public Path pathDerive() {

		Path basePath = FMLPaths.CONFIGDIR.get();

		String subPath = "";
		if (folder.isPresent()) {

			subPath += folder.get() + "/";
		}
		subPath += filename;

		return basePath.resolve(subPath);
	}
}