package com.synchrofield.floaxial.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.synchrofield.floaxial.central.configure.ClientDropletConfigure;
import com.synchrofield.floaxial.central.configure.MaterialConfigureList;
import com.synchrofield.floaxial.central.droplet.MaterialPhysics;
import com.synchrofield.floaxial.central.droplet.MaterialTable;
import com.synchrofield.floaxial.central.network.DropNetwork;
import com.synchrofield.floaxial.central.network.DropPacket;
import com.synchrofield.floaxial.central.registry.CentralRegistry;
import com.synchrofield.floaxial.central.statistics.ClientDropStatistics;
import com.synchrofield.floaxial.central.statistics.ClientMaterialStatistics;
import com.synchrofield.floaxial.client.render.ClientMove;
import com.synchrofield.floaxial.client.render.MovePoolList;
import com.synchrofield.floaxial.client.render.MoveRender;
import com.synchrofield.floaxial.server.droplet.Droplet;
import com.synchrofield.library.list.IndexQueue;
import com.synchrofield.library.terrain.Geometry;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.SectionPos;
import net.minecraftforge.client.event.RenderLevelStageEvent;

public class DropControl {

	public ClientDropletConfigure configure;

	public MovePoolList movePoolList;
	public MoveRender moveRender;

	public MaterialTable materialTable;
	public MaterialPhysics[] materialPhysicsList;

	public ClientDropStatistics statistics;

	protected DropControl(ClientDropletConfigure configure, MaterialTable materialTable,
			MaterialPhysics[] materialPhysicsList, MovePoolList poolList, MoveRender dropRender) {

		this.configure = configure;
		this.materialTable = materialTable;
		this.materialPhysicsList = materialPhysicsList;
		this.movePoolList = poolList;
		this.moveRender = dropRender;

		this.statistics = ClientDropStatistics.of();
	}

	public static DropControl of(ClientDropletConfigure configure,
			MaterialConfigureList materialConfigure, CentralRegistry registry) {

		MovePoolList movePoolList = MovePoolList.of(configure);
		movePoolList.animateListFill();

		MaterialTable materialTable = MaterialTable.of(materialConfigure, registry);

		MaterialPhysics[] materialPhysicsList = new MaterialPhysics[MaterialTable.Size];

		for (int i = 0; i < MaterialTable.Size; i++) {

			materialPhysicsList[i] = MaterialPhysics.of(materialConfigure.list[i]);
		}

		MoveRender dropRender = MoveRender.of();

		return new DropControl(configure, materialTable, materialPhysicsList, movePoolList,
				dropRender);
	}

	public void onRender(ClientLevel level, RenderLevelStageEvent event) {

		// blindly render whole list even though some are dormant
		Camera camera = event.getCamera();
		PoseStack pose = event.getPoseStack();

		for (int materialIndex = 0; materialIndex < MaterialTable.Size; materialIndex++) {

			moveRender.animateListRender(level, camera, pose, movePoolList.list[materialIndex],
					materialIndex, materialTable.list[materialIndex],
					materialPhysicsList[materialIndex]);
		}
	}

	public ClientDropStatistics statisticsDerive() {

		for (int material = 0; material < MaterialTable.Size; material++) {

			statisticsDeriveMaterial(material, statistics.material[material]);
		}

		return statistics;
	}

	public void statisticsDeriveMaterial(int material,
			ClientMaterialStatistics materialStatistics) {

		materialStatistics.animateUse = movePoolList.list[material].useSize();
		materialStatistics.animateAllocate = movePoolList.list[material].allocateSize;
	}

	public void moveAdd(ClientLevel level, DropPacket packet) {

		statistics.networkReceivePacket++;
		statistics.networkReceiveByte += packet.networkSize();

		SectionPos sectionLocation = packet.section.location;

		int material = packet.section.material;

		statistics.material[material].networkReceiveSection++;

		int moveListSize = packet.section.moveListSize;
		for (int moveIndex = 0; moveIndex < moveListSize; moveIndex++) {

			statistics.material[material].networkReceiveDrop++;

			int dropMove = packet.section.moveList[moveIndex];

			// put into render array
			int poolIndex = movePoolList.list[material].consumeTry();
			if (poolIndex == IndexQueue.IndexInvalid) {

				// pool full, discard the rest of the packet
				statistics.material[material].animateFailTotal++;
				break;
			}

			statistics.material[material].animateTotal++;

			ClientMove animate = movePoolList.list[material].animateList[poolIndex];

			animate.existIs = true;

			animate.sourceLocation = DropNetwork.locationUnpack(dropMove, sectionLocation);

			animate.energyStart = DropNetwork.energy(dropMove);
			animate.direction = Geometry.DirectionFromPack[DropNetwork.direction(dropMove)];

			int startTimePack = DropNetwork.time(dropMove);
			float startTimeExact = (float) startTimePack / (float) Droplet.TimeDecimalScale;
			animate.timeStart = (float) (packet.createTime) + startTimeExact;
		}
	}
}
