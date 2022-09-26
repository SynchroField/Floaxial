package com.synchrofield.floaxial.central.configure;

import java.util.Optional;

import com.synchrofield.floaxial.central.configure.store.GeneralStore;
import com.synchrofield.floaxial.central.configure.store.MaterialStoreTable;
import com.synchrofield.floaxial.central.configure.store.RuleStoreTable;
import com.synchrofield.floaxial.central.droplet.MaterialTable;
import com.synchrofield.floaxial.server.rule.BlockCriteria;
import com.synchrofield.floaxial.server.rule.BlockCriteriaType;
import com.synchrofield.floaxial.server.rule.RuleCriteriaType;
import com.synchrofield.floaxial.server.rule.RuleTable;
import com.synchrofield.library.configure.GenerateConfigure;
import com.synchrofield.library.configure.ConfigureException;
import com.synchrofield.library.terrain.Terrain;
import com.synchrofield.library.terrain.TerrainException;

// Configuration for both client and server.
//
// File tracking is disabled and changes need to be manually reloaded with
// cacheCopyFromStore() .
public class CentralConfigure {

	public static final Optional<String> ConfigureFolder = Optional
			.of(ProductConfigure.Name);

	// file storage
	public final GeneralStore generalStore;
	public final MaterialStoreTable materialStore;
	public final RuleStoreTable ruleStore;

	// cache
	public ProductConfigure product = ProductConfigure.of();
	public RegistryConfigure registry = RegistryConfigure.of();
	public MaterialConfigureList material = MaterialConfigureList.of();
	public GenerateConfigure generate = GenerateConfigure.of();
	public ServerConfigure server = ServerConfigure.of();
	public ClientConfigure client = ClientConfigure.of();

	protected CentralConfigure(GeneralStore commonStore, MaterialStoreTable materialStore,
			RuleStoreTable ruleStore) {

		this.generalStore = commonStore;
		this.materialStore = materialStore;
		this.ruleStore = ruleStore;
	}

	public static CentralConfigure of() {

		GeneralStore generalStore = GeneralStore.of(ConfigureFolder);
		MaterialStoreTable materialStore = MaterialStoreTable.of(ConfigureFolder);
		RuleStoreTable ruleStore = RuleStoreTable.of(ConfigureFolder);

		return new CentralConfigure(generalStore, materialStore, ruleStore);
	}

	// only call after block registry is created
	public void validateBlock() throws ConfigureException {

		try {

			for (int materialIndex = 0; materialIndex < MaterialTable.Size; materialIndex++) {

				Terrain.blockStateValidate(material.list[materialIndex].mobileState);
				Terrain.blockStateValidate(material.list[materialIndex].renderState);
			}

			for (int ruleIndex = 0; ruleIndex < RuleTable.ListSize; ruleIndex++) {

				if (!server.level.rule.list[ruleIndex].enableIs) {

					continue;
				}

				BlockCriteria.validate(server.level.rule.list[ruleIndex].centerCriteriaType,
						server.level.rule.list[ruleIndex].centerName);

				Terrain.blockStateValidate(server.level.rule.list[ruleIndex].destinationState0);

				if (server.level.rule.list[ruleIndex].destinationState1.trim()
						.length() > 0) {

					// destination1 in use
					Terrain.blockStateValidate(server.level.rule.list[ruleIndex].destinationState1);
				}
			}
		}
		catch (TerrainException e) {

			throw ConfigureException.of(e.getMessage());
		}
	}

	// copy from file to cache
	public void cacheCopyFromStore() throws ConfigureException {

		// intermediate copy to property list from file
		propertyCopyFromStore();

		// then copy to cache
		cacheCopyFromProperty();
	}

	// copy from file to store object
	public void propertyCopyFromStore() {

		generalStore.copyFromFile();
		materialStore.copyFromFile();
		ruleStore.copyFromFile();
	}

	// copy from store object to cache
	public void cacheCopyFromProperty() {

		// material
		MaterialConfigure[] materialConfigureList = new MaterialConfigure[MaterialTable.Size];
		ServerMaterialConfigure[] serverMaterialConfigureList = new ServerMaterialConfigure[MaterialTable.Size];
		ClientMaterialConfigure[] clientMaterialConfigureList = new ClientMaterialConfigure[MaterialTable.Size];

		for (int materialIndex = 0; materialIndex < MaterialTable.Size; materialIndex++) {

			materialConfigureList[materialIndex] = MaterialConfigure.of()
					.withTimeScale(materialStore.list[materialIndex].timeScale.get()
							.floatValue())
					.withEnergyMaximum(materialStore.list[materialIndex].energyMaximum.get())
					.withMobileState(materialStore.list[materialIndex].mobileState.get())
					.withMobileStateIsSubset(
							materialStore.list[materialIndex].mobileStateIsSubset.get())
					.withRenderState(materialStore.list[materialIndex].renderState.get())
					.withHoleRadius(materialStore.list[materialIndex].holeRadius.get())
					.withPyramidSizeY(materialStore.list[materialIndex].pyramidSizeY.get())
					.withDenseIs(materialStore.list[materialIndex].denseIs.get());

			serverMaterialConfigureList[materialIndex] = ServerMaterialConfigure.of()
					.withGhostProcessPeriod(
							materialStore.list[materialIndex].ghostProcessPeriod.get())
					.withMobileProcessPeriod(
							materialStore.list[materialIndex].mobileProcessPeriod.get())
					.withPacketSize(materialStore.list[materialIndex].packetSize.get())
					.withArchivePeriod(materialStore.list[materialIndex].archivePeriod.get())
					.withLoadSectionSize(materialStore.list[materialIndex].loadSectionSize.get())
					.withLoadQueueLocationPerTick(
							materialStore.list[materialIndex].loadLocationPerTick.get())
					.withGhostLookahead(materialStore.list[materialIndex].ghostLookahead.get());

			clientMaterialConfigureList[materialIndex] = ClientMaterialConfigure.of()
					.withAnimateSize(materialStore.list[materialIndex].animateSize.get());
		}

		// droplet
		ServerDropletConfigure serverDropletConfigure = ServerDropletConfigure.of()
				.withGapSize(generalStore.gapSize.get())
				.withGapPerTick(generalStore.gapPerTick.get())
				.withMaterial(serverMaterialConfigureList);

		ClientDropletConfigure clientDropletConfigure = ClientDropletConfigure.of()
				.withGhostIsVisible(generalStore.ghostIsVisible.get())
				.withMaterial(clientMaterialConfigureList);

		// rule
		RuleConfigure[] ruleConfigureList = new RuleConfigure[RuleTable.ListSize];

		for (int ruleIndex = 0; ruleIndex < RuleTable.ListSize; ruleIndex++) {

			int criteriaType = RuleCriteriaType
					.fromString(ruleStore.list[ruleIndex].criteriaType.get());
			if (criteriaType == -1) {

				// bad type, for now just use default
				criteriaType = RuleConfigure.CriteriaTypeDefault;
			}

			int centerType = BlockCriteriaType
					.fromString(ruleStore.list[ruleIndex].centerType.get());
			if (centerType == -1) {

				centerType = RuleConfigure.CenterCriteriaTypeDefault;
			}

			int touchType = BlockCriteriaType.fromString(ruleStore.list[ruleIndex].touchType.get());
			if (touchType == -1) {

				touchType = RuleConfigure.TouchTypeDefault;
			}

			ruleConfigureList[ruleIndex] = RuleConfigure.of()
					.withEnableIs(ruleStore.list[ruleIndex].enableIs.get())
					.withSectionPerTick(ruleStore.list[ruleIndex].sectionPerTick.get())
					.withTickPerLevel(ruleStore.list[ruleIndex].tickPerLevel.get())
					.withLocationPerSection(ruleStore.list[ruleIndex].locationPerSection.get())
					.withCriteriaType(criteriaType)
					.withCenterCriteriaType(centerType)
					.withCenterName(ruleStore.list[ruleIndex].centerName.get())
					.withTouchType(touchType)
					.withTouchName(ruleStore.list[ruleIndex].touchName.get())
					.withTouchIsAny(ruleStore.list[ruleIndex].touchIsAny.get())
					.withTouchDirectionList(ruleStore.list[ruleIndex].touchDirectionList.get())
					.withRowDistance(ruleStore.list[ruleIndex].rowDistance.get())
					.withSampleRadius(ruleStore.list[ruleIndex].sampleRadius.get())
					.withDestinationState0(ruleStore.list[ruleIndex].destinationState0.get())
					.withDestinationWeight0(ruleStore.list[ruleIndex].destinationWeight0.get())
					.withDestinationState1(ruleStore.list[ruleIndex].destinationState1.get())
					.withDestinationWeight1(ruleStore.list[ruleIndex].destinationWeight1.get())
					.withDestinationOffset(ruleStore.list[ruleIndex].destinationOffset.get());
		}

		// cache
		this.product = ProductConfigure.of();

		this.registry = RegistryConfigure.of();

		this.generate = GenerateConfigure.of()
				.withEnableIs(generalStore.generateIsEnable.get());

		this.material = MaterialConfigureList.of()
				.withList(materialConfigureList);

		this.server = ServerConfigure.of()
				.withLevel(ServerLevelConfigure.of()
						.withDrop(serverDropletConfigure)
						.withRule(RuleConfigureList.of()
								.withList(ruleConfigureList)));

		this.client = ClientConfigure.of()
				.withLevel(ClientLevelConfigure.of()
						.withDrop(clientDropletConfigure));
	}
}
