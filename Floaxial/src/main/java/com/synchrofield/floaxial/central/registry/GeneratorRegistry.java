package com.synchrofield.floaxial.central.registry;

import com.synchrofield.floaxial.central.configure.ProductConfigure;
import com.synchrofield.floaxial.central.generate.IslandBiomeSource;
import com.synchrofield.floaxial.central.generate.IslandChunkGenerator;

import net.minecraft.client.gui.screens.worldselection.WorldPreset;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class GeneratorRegistry {

	protected static IslandChunkGenerator islandChunkGenerator;

	protected GeneratorRegistry() {

	}

	public static GeneratorRegistry of() {

		return new GeneratorRegistry();
	}

	public void register() {

		Registry.register(Registry.CHUNK_GENERATOR,
				new ResourceLocation(ProductConfigure.Name, ProductConfigure.Name),
				IslandChunkGenerator.CODEC);
	}

	public static ChunkGenerator islandChunkGeneratorGet(long seedMaybe) {

		if (islandChunkGenerator == null) {

			// from DimensionType
			RegistryAccess registryAccess = RegistryAccess.BUILTIN.get();

			Registry<Biome> registry1 = registryAccess.registryOrThrow(Registry.BIOME_REGISTRY);

			Registry<StructureSet> registry2 = registryAccess
					.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);

			Holder<Biome> biomeHolder = registry1.getOrCreateHolder(Biomes.PLAINS);

			BiomeSource biomeSource = new IslandBiomeSource(biomeHolder);

			Registry<NormalNoise.NoiseParameters> registry3 = registryAccess
					.registryOrThrow(Registry.NOISE_REGISTRY);
			Registry<NoiseGeneratorSettings> registry4 = registryAccess
					.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);

			islandChunkGenerator = new IslandChunkGenerator(registry2, registry3, biomeSource,
					seedMaybe, registry4.getOrCreateHolder(NoiseGeneratorSettings.OVERWORLD));
		}

		return islandChunkGenerator;
	}

	public void islandChunkGeneratorInstall() {

		WorldPreset.NORMAL = new WorldPreset(new TextComponent("default")) {

			protected ChunkGenerator generator(RegistryAccess p_194096_, long p_194097_) {

				return islandChunkGeneratorGet(p_194097_);
			}
		};
	}
}
