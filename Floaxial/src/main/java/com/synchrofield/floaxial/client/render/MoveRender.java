package com.synchrofield.floaxial.client.render;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.synchrofield.floaxial.central.droplet.DropletMaterial;
import com.synchrofield.floaxial.central.droplet.MaterialPhysics;
import com.synchrofield.library.math.MathUtility;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.EmptyModelData;

public class MoveRender {

	public final BlockRenderDispatcher blockRenderDispatcher;
	public final BufferSource bufferSource;

	public final Random random;

	protected MoveRender(BlockRenderDispatcher blockRenderDispatcher, BufferSource bufferSource,
			Random random) {

		this.blockRenderDispatcher = blockRenderDispatcher;
		this.bufferSource = bufferSource;
		this.random = random;
	}

	public static MoveRender of() {

		Minecraft minecraft = Minecraft.getInstance();

		BlockRenderDispatcher blockRenderDispatcher = minecraft.getBlockRenderer();

		MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers()
				.bufferSource();

		Random random = new Random();

		return new MoveRender(blockRenderDispatcher, bufferSource, random);
	}

	// expect camera pose
	public void animateListRender(ClientLevel level, Camera camera, PoseStack pose, MovePool pool,
			int materialIndex, DropletMaterial material, MaterialPhysics materialPhysics) {

		//		ForgeHooksClient.setRenderType(RenderType.translucent());

		BlockState blockState = material.renderState;

		BakedModel model = blockRenderDispatcher.getBlockModel(blockState);

		VertexConsumer vertexConsumer;
		if (material.solidIs) {

			vertexConsumer = bufferSource.getBuffer(RenderType.solid());
		}
		else {

			vertexConsumer = bufferSource.getBuffer(RenderType.translucent());
		}

		// world view
		pose.pushPose();

		Vec3 worldTranslate = camera.getPosition()
				.reverse();

		pose.translate(worldTranslate.x, worldTranslate.y, worldTranslate.z);

		// hard timeout 
		double DeltaTickMaximumExact = 100.0;

		long tick = level.getGameTime();

		double tickExact = (double) tick + (double) Minecraft.getInstance()
				.getFrameTime();

		// render all active animate
		for (int poolIndex = 0; poolIndex < pool.allocateSize; poolIndex++) {

			ClientMove animate = pool.animateList[poolIndex];

			if (!animate.existIs) {

				continue;
			}

			if (tickExact + 1.0 < animate.timeStart) {

				// waiting to start
				continue;
			}

			double deltaTickExact = tickExact - animate.timeStart;

			if (deltaTickExact > DeltaTickMaximumExact) {

				// hard timeout
				animate.existIs = false;
				pool.release(poolIndex);
				continue;
			}

			double deltaTime = deltaTickExact * materialPhysics.configure.timeScale;

			if (!animateRender(level, materialIndex, material, materialPhysics, animate,
					vertexConsumer, pose, model, deltaTime)) {

				// move complete
				animate.existIs = false;
				pool.release(poolIndex);
				continue;
			}
		}

		pose.popPose();
	}

	// expect pose to be player camera view
	// return false if timeout
	public boolean animateRender(ClientLevel level, int materialIndex, DropletMaterial material,
			MaterialPhysics materialPhysics, ClientMove animate, VertexConsumer vertexConsumer,
			PoseStack pose, BakedModel model, double deltaTime) {

		Vec3 sourcePosition = Vec3.atLowerCornerOf(animate.sourceLocation);

		// already unpack
		int totalDistance = 1;

		int energy0 = animate.energyStart;

		int acceleration = (animate.direction == Direction.DOWN) ? 1 : 0;

		double deltaPosition;
		if (acceleration == 1) {

			deltaPosition = materialPhysics.deltaPositionDerive(energy0, totalDistance, deltaTime);
		}
		else {

			deltaPosition = materialPhysics.moveDerivePositionConstant(energy0, deltaTime);
		}

		if (deltaPosition < 0.0) {

			// early
			return true;
		}

		if (deltaPosition > (double) totalDistance) {

			// complete
			return false;
		}

		Vec3 deltaPositionVector = new Vec3(animate.direction.step()).scale(deltaPosition);

		Vec3 position = sourcePosition.add(deltaPositionVector);

		// light
		int ambientLightSize = level.getSkyDarken();

		int lightSize = level.getLightEngine()
				.getRawBrightness(animate.sourceLocation.above(), ambientLightSize);

		lightSize <<= 4;
		lightSize = MathUtility.cap(lightSize, 255);

		pose.pushPose();
		pose.translate(position.x, position.y, position.z);

		blockRenderDispatcher.renderSingleBlock(material.renderState, pose, bufferSource, lightSize,
				OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);

		pose.popPose();

		return true;
	}
}
