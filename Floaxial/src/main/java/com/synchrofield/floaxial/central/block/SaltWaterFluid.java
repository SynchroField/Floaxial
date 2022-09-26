package com.synchrofield.floaxial.central.block;

import com.synchrofield.floaxial.central.registry.CentralRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class SaltWaterFluid extends ForgeFlowingFluid {

	public static final int LevelMinimum = 1;
	public static final int LevelMaximum = 7;

	public static SaltWaterFluid.Properties propertyListGetDefault(int colorProperty) {

		return new SaltWaterFluid.Properties(
				CentralRegistry.instance().fluid.SaltWaterStillFluidObject,
				CentralRegistry.instance().fluid.SaltWaterFlowingFluidObject,
				FluidAttributes
						.builder(CentralRegistry.instance().fluid.SaltWaterStillTextureName,
								CentralRegistry.instance().fluid.SaltWaterFlowingTextureName)
						.density(1000)
						.viscosity(1000)
						.luminosity(0)
						.color(colorProperty))
				.bucket(CentralRegistry.instance().item.SaltWaterBucketObject)
				.block(CentralRegistry.instance().block.SaltWaterObject);
	}

	public SaltWaterFluid(Properties p_49795_) {

		super(p_49795_);
	}

	@Override
	protected void createFluidStateDefinition(Builder<Fluid, FluidState> builder) {

		super.createFluidStateDefinition(builder);
	}

	@Override
	public int getAmount(FluidState p_164509_) {

		return 0;
	}

	@Override
	public boolean isSource(FluidState p_76140_) {

		return false;
	}

	@Override
	public boolean isSame(Fluid fluidIn) {

		return super.isSame(fluidIn);
	}

	@Override
	protected boolean canConvertToSource() {

		return false;
	}

	public static class SaltWaterFluidFlowing extends SaltWaterFluid {

		public SaltWaterFluidFlowing(Properties properties) {

			super(properties);

			registerDefaultState(getStateDefinition().any()
					.setValue(LEVEL, 7));
		}

		protected void createFluidStateDefinition(
				StateDefinition.Builder<Fluid, FluidState> builder) {

			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		public int getAmount(FluidState state) {

			return state.getValue(LEVEL);
		}

		public boolean isSource(FluidState state) {

			return false;
		}
	}

	public static class SaltWaterFluidSource extends SaltWaterFluid {

		public SaltWaterFluidSource(Properties properties) {

			super(properties);
		}

		public int getAmount(FluidState state) {

			return 8;
		}

		public boolean isSource(FluidState state) {

			return true;
		}
	}

	@Override
	public Vec3 getFlow(BlockGetter p_75987_, BlockPos p_75988_, FluidState p_75989_) {

		return Vec3.ZERO;
	}

	@Override
	public float getHeight(FluidState p_76050_, BlockGetter p_76051_, BlockPos p_76052_) {

		return super.getHeight(p_76050_, p_76051_, p_76052_);
	}

	@Override
	public float getOwnHeight(FluidState p_76048_) {

		return 1.0f;
	}

	@Override
	protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos,
			Fluid fluidIn, Direction direction) {

		return super.canBeReplacedWith(state, level, pos, fluidIn, direction);
	}
}
