package com.synchrofield.floaxial.central.generate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.synchrofield.floaxial.central.block.VanillaJungleLeafBlock;
import com.synchrofield.floaxial.central.registry.CentralRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;

//public class IslandChunkGenerator extends ChunkGenerator {
public class IslandChunkGenerator extends NoiseBasedChunkGenerator {

	public static final Codec<IslandChunkGenerator> CODEC = RecordCodecBuilder
			.create((p_188643_) -> {

				return commonCodec(p_188643_)
						.and(p_188643_.group(RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY)
								.forGetter((p_188716_) -> {

									return p_188716_.noises;
								}), BiomeSource.CODEC.fieldOf("biome_source")
										.forGetter((p_188711_) -> {

											return p_188711_.biomeSource;
										}),
								Codec.LONG.fieldOf("seed")
										.stable()
										.forGetter((p_188690_) -> {

											return p_188690_.seed;
										}),
								NoiseGeneratorSettings.CODEC.fieldOf("settings")
										.forGetter((p_204585_) -> {

											return p_204585_.settings;
										})))
						.apply(p_188643_, p_188643_.stable(IslandChunkGenerator::new));
			});

	// inclusive
	public int BedrockY = 0;

	public int OceanBottom = BedrockY + 1;
	public int OceanTop = 7;

	// island
	public int IslandCenterX = 0;
	public int IslandCenterZ = 0;

	public int IslandFloor = OceanTop;

	public int IslandBoxRadius = 80;

	public int IslandBoxX0 = -IslandBoxRadius;
	public int IslandBoxX1 = IslandBoxRadius;
	public int IslandBoxZ0 = -IslandBoxRadius;
	public int IslandBoxZ1 = IslandBoxRadius;
	public int IslandBoxY0 = OceanBottom;
	public int IslandBoxY1 = 100;

	public int IslandSectionX0 = (IslandBoxX0 >> 4) - 1;
	public int IslandSectionX1 = (IslandBoxX1 >> 4) + 1;
	public int IslandSectionZ0 = (IslandBoxZ0 >> 4) - 1;
	public int IslandSectionZ1 = (IslandBoxZ1 >> 4) + 1;
	public int IslandSectionY0 = (IslandBoxY0 >> 4) - 1;
	public int IslandSectionY1 = (IslandBoxY1 >> 4) + 1;

	// palm
	public int PalmX = 0;
	public int PalmY = IslandFloor;
	public int PalmZ = 0;

	public int PalmTrunkSize = 7;
	public int PalmTrunk0 = PalmY;
	public int PalmTrunk1 = PalmY + PalmTrunkSize;

	public int PalmCanopyRadius = 2;

	public int PalmCanopyX0 = PalmX - PalmCanopyRadius;
	public int PalmCanopyX1 = PalmX + PalmCanopyRadius;
	public int PalmCanopyZ0 = PalmZ - PalmCanopyRadius;
	public int PalmCanopyZ1 = PalmZ + PalmCanopyRadius;
	public int PalmCanopyY0 = PalmTrunk1 - 1;
	public int PalmCanopyY1 = PalmTrunk1 + 1;

	public int PalmBoxX0 = PalmCanopyX0;
	public int PalmBoxX1 = PalmCanopyX1;
	public int PalmBoxZ0 = PalmCanopyZ0;
	public int PalmBoxZ1 = PalmCanopyZ1;
	public int PalmBoxY0 = PalmY;
	public int PalmBoxY1 = PalmCanopyY1;

	public BlockState PalmDirtState = Blocks.DIRT.defaultBlockState();
	public BlockState PalmLogState = Blocks.JUNGLE_LOG.defaultBlockState();
	public BlockState PalmLeafState = CentralRegistry.instance().block.JungleLeafMobileObject.get()
			.defaultBlockState()
			.setValue(VanillaJungleLeafBlock.PERSISTENT, false);
	public BlockState GrassState = Blocks.GRASS_BLOCK.defaultBlockState();
	public BlockState WaterState = CentralRegistry.instance().block.SaltWaterObject.get()
			.defaultBlockState();
	public BlockState SandState = CentralRegistry.instance().block.VanillaSandBlockObject.get()
			.defaultBlockState();

	public final PerlinNoise perlinNoise;

	public IslandChunkGenerator(Registry<StructureSet> p_209112_,
			Registry<NormalNoise.NoiseParameters> p_209113_, BiomeSource p_209114_, long p_209116_,
			Holder<NoiseGeneratorSettings> p_209117_) {

		super(p_209112_, p_209113_, p_209114_, p_209116_, p_209117_);

		ArrayList<Integer> noiseParameter = new ArrayList<>();

		noiseParameter.add(1);

		this.perlinNoise = PerlinNoise.create(new XoroshiroRandomSource(this.seed), noiseParameter);
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {

		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long newSeed) {

		return new IslandChunkGenerator(this.structureSets, this.noises, this.biomeSource, newSeed,
				this.settings);
	}

	@Override
	public Climate.Sampler climateSampler() {

		return Climate.empty();
	}

	@Override
	public int getGenDepth() {

		return 512;
	}

	@Override
	public int getSeaLevel() {

		return -63;
	}

	@Override
	public int getMinY() {

		return 0;
	}

	@Override
	public int getBaseHeight(int x, int y, Heightmap.Types heightmapType,
			LevelHeightAccessor levelHeightAccessor) {

		return 0;
	}

	@Override
	public NoiseColumn getBaseColumn(int p_156150_, int p_156151_,
			LevelHeightAccessor levelHeightAccessor) {

		return new NoiseColumn(0, new BlockState[] {
				Blocks.AIR.defaultBlockState()
		});
	}

	@Override
	public void addDebugScreenInfo(List<String> lines, BlockPos pos) {

	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {

	}

	@Override
	public void applyCarvers(WorldGenRegion worldGenRegion, long seed, BiomeManager biomeManager,
			StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess,
			GenerationStep.Carving carving) {

	}

	@Override
	public void createReferences(WorldGenLevel world, StructureFeatureManager structureManager,
			ChunkAccess chunk) {

	}

	@Override
	public void applyBiomeDecoration(WorldGenLevel p_187712_, ChunkAccess p_187713_,
			StructureFeatureManager p_187714_) {

	}

	@Override
	public void createStructures(RegistryAccess p_62200_, StructureFeatureManager p_62201_,
			ChunkAccess p_62202_, StructureManager p_62203_, long p_62204_) {

	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender,
			StructureFeatureManager structureManager, ChunkAccess chunk) {

		return CompletableFuture.completedFuture(chunk);
	}

	@SuppressWarnings("deprecation")
	public void sectionFill(LevelChunkSection section, BlockState state) {

		section.states = new PalettedContainer<>(Block.BLOCK_STATE_REGISTRY, state,
				PalettedContainer.Strategy.SECTION_STATES);
	}

	public void sectionFillSliceY(LevelChunkSection section, BlockState state, int sliceLocation) {

		for (int x = 0; x < 16; x++) {

			for (int z = 0; z < 16; z++) {

				section.setBlockState(x, sliceLocation, z, state, false);
			}
		}
	}

	// fill down from given y location inclusive
	public void sectionFillYn(LevelChunkSection section, BlockState state, int sliceLocationEnd) {

		for (int sliceLocation = 0; sliceLocation <= sliceLocationEnd; sliceLocation++) {

			for (int x = 0; x < 16; x++) {

				for (int z = 0; z < 16; z++) {

					section.setBlockState(x, sliceLocation, z, state, false);
				}
			}
		}
	}

	@Override
	public void buildSurface(WorldGenRegion worldGenRegion,
			StructureFeatureManager structureFeatureManager, ChunkAccess chunk) {

		for (LevelChunkSection section : chunk.getSections()) {

			if (section == null) {

				continue;
			}

			SectionPos sectionLocation = SectionPos.of(chunk.getPos(), section.bottomBlockY() >> 4);

			sectionGenerate(worldGenRegion, structureFeatureManager, chunk, sectionLocation,
					section);
		}
	}

	public void sectionGenerate(WorldGenRegion worldGenRegion,
			StructureFeatureManager structureFeatureManager, ChunkAccess chunk,
			SectionPos sectionLocation, LevelChunkSection section) {

		assert section != null;

		if (section.bottomBlockY() < 0) {

			// below bedrock
			return;
		}

		// single material section
		if (section.bottomBlockY() <= OceanTop) {

			// ocean
			if (section.bottomBlockY() + 16 <= OceanTop) {

				// full section
				sectionFill(section, WaterState);
			}
			else {

				// only up to certain y in section
				for (int layerY = 0; layerY <= (OceanTop - section.bottomBlockY()); layerY++) {

					sectionFillSliceY(section, WaterState, layerY);
				}
			}
		}

		if (section.bottomBlockY() == BedrockY) {

			// bedrock
			sectionFillSliceY(section, Blocks.BEDROCK.defaultBlockState(), 0);
		}

		// individual block generation
		int sectionOriginX = sectionLocation.origin()
				.getX();
		int sectionOriginY = sectionLocation.origin()
				.getY();
		int sectionOriginZ = sectionLocation.origin()
				.getZ();

		int x;
		int y;
		int z;

		// island
		if (islandIsSection(sectionLocation)) {

			for (int dY = 0; dY < 16; dY++) {

				for (int dX = 0; dX < 16; dX++) {

					for (int dZ = 0; dZ < 16; dZ++) {

						x = sectionOriginX + dX;
						y = sectionOriginY + dY;
						z = sectionOriginZ + dZ;

						if (palmIsBox(x, y, z)) {

							if (palmIsTrunk(x, y, z)) {

								section.setBlockState(dX, dY, dZ, PalmLogState);
								continue;
							}

							if (palmIsCanopy(x, y, z)) {

								section.setBlockState(dX, dY, dZ, PalmLeafState);
								continue;
							}
						}

						if (islandIsBox(x, y, z)) {

							if (islandIs(x, y, z)) {

								// grass area in center
								if (Math.abs(PalmX - x) < 2) {

									if (Math.abs(PalmZ - z) < 2) {

										section.setBlockState(dX, dY, dZ, GrassState);
										continue;
									}
								}
								//								}

								section.setBlockState(dX, dY, dZ, SandState);
								continue;
							}
						}
					}
				}
			}
		}

		section.recalcBlockCounts();
	}

	public boolean islandIsSection(SectionPos location) {

		return ((location.x() >= IslandSectionX0) && (location.x() <= IslandSectionX1))
				&& ((location.y() >= IslandSectionY0) && (location.y() <= IslandSectionY1))
				&& ((location.z() >= IslandSectionZ0) && (location.z() <= IslandSectionZ1));
	}

	public boolean islandIsBox(int x, int y, int z) {

		return ((x >= IslandBoxX0) && (x <= IslandBoxX1))
				&& ((y >= IslandBoxY0) && (y <= IslandBoxY1))
				&& ((z >= IslandBoxZ0) && (z <= IslandBoxZ1));
	}

	public boolean islandIs(int x, int y, int z) {

		if (y > IslandFloor + 10) {

			return false;
		}

		float InputScale = 0.02f;
		float OutputPreTranslate = 0.0f;
		float OutputScale = 4.0f;
		float OutputPostTranslate = 0.0f;

		float xInput = (float) x * InputScale;
		float zInput = (float) z * InputScale;

		float noise = (float) perlinNoise.getValue((double) xInput, (double) 0, (double) zInput);

		noise += OutputPreTranslate;
		noise *= OutputScale;
		noise += OutputPostTranslate;

		// sand pyramid
		float slope = 0.2f;

		float originDistance = (float) Math.sqrt((double) (x * x) + (double) (z * z));

		originDistance *= slope;
		originDistance *= 1.0;

		float pyramidY = (float) (OceanTop + 2) - originDistance;

		pyramidY *= 1.0;

		float islandY = pyramidY + noise;

		if (y <= (int) islandY) {

			return true;
		}

		return false;
	}

	public boolean palmIsBox(int x, int y, int z) {

		return ((x >= PalmBoxX0) && (x <= PalmBoxX1)) && ((y >= PalmBoxY0) && (y <= PalmBoxY1))
				&& ((z >= PalmBoxZ0) && (z <= PalmBoxZ1));
	}

	public boolean palmIsTrunk(int x, int y, int z) {

		return (x == PalmX) && (z == PalmZ) && (y >= PalmTrunk0) && (y <= PalmTrunk1);
	}

	// assume in box
	public boolean palmIsCanopy(int x, int y, int z) {

		if (y < PalmCanopyY0) {

			return false;
		}

		if (y > PalmCanopyY1) {

			return false;
		}

		int radius = PalmCanopyY1 - y;

		int originDistance = ((Math.abs(x) * Math.abs(x)) + (Math.abs(z) * Math.abs(z))) / 2;

		if (originDistance > radius) {

			return false;
		}

		return true;
	}
}