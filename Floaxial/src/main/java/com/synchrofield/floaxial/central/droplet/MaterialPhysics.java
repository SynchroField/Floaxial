package com.synchrofield.floaxial.central.droplet;

import com.synchrofield.floaxial.central.configure.MaterialConfigure;
import com.synchrofield.library.math.MathUtility;

public class MaterialPhysics {

	public final MaterialConfigure configure;

	protected MaterialPhysics(MaterialConfigure configure) {

		this.configure = configure;
	}

	public static MaterialPhysics of(MaterialConfigure configure) {

		return new MaterialPhysics(configure);
	}

	public double fallDeriveTime(int energy0, int distance) {

		assert energy0 >= 0;
		assert energy0 <= configure.energyMaximum;

		assert distance > 0;
		assert distance <= 1;

		int distance0 = Math.min(distance, configure.energyMaximum - energy0);
		int distance1 = distance - distance0;

		int energy1 = energyCap(energy0 + distance0);

		// leg1 is at max energy by definition, otherwise there's only 1 leg
		int energy2 = configure.energyMaximum;

		double velocity0 = Math.sqrt((double) energy0);
		double velocity1 = Math.sqrt((double) energy1);
		double velocity2 = Math.sqrt((double) energy2);

		double deltaTime0;
		if (distance0 > 0) {

			double middle = (velocity0 * velocity0) + (2.0 * (double) distance0);

			if (middle < 0.0) {

				// no root
				return 0.0;
			}
			else {

				double high = (-velocity0 + Math.sqrt(middle)) / 1.0;
				double low = (-velocity0 - Math.sqrt(middle)) / 1.0;

				deltaTime0 = Math.max(high, low);
			}
		}
		else {

			deltaTime0 = 0;
		}

		double deltaTime1;
		if (distance1 > 0) {

			// leg1 always constant at terminal velocity
			double velocity1Average = (velocity2 + velocity1) / 2.0;

			deltaTime1 = (double) distance1 / velocity1Average;
		}
		else {

			deltaTime1 = 0;
		}

		return deltaTime0 + deltaTime1;
	}

	// deltaTime not scaled in original
	public double deltaPositionDerive(int energy0, int distance, double deltaTime) {

		assert energy0 >= 0;
		assert energy0 <= configure.energyMaximum;

		assert distance > 0;
		assert distance <= 1;

		int distance0 = Math.min(distance, configure.energyMaximum - energy0);
		int energy1 = energyCap(energy0 + distance0);

		double velocity0 = Math.sqrt((double) energy0);
		double velocity1 = Math.sqrt((double) energy1);
		double deltaTime0;
		if (distance0 > 0) {

			double middle = (velocity0 * velocity0) + (2.0 * (double) distance0);

			if (middle < 0.0) {

				// no root
				return 0.0;
			}
			else {

				double high = (-velocity0 + Math.sqrt(middle)) / 1.0;
				double low = (-velocity0 - Math.sqrt(middle)) / 1.0;

				deltaTime0 = Math.max(high, low);
			}
		}
		else {

			deltaTime0 = 0;
		}

		double deltaPosition;
		if (deltaTime < deltaTime0) {

			// still in leg0 of fall
			//
			// s = ut + 0.5 at^2
			deltaPosition = (velocity0 * deltaTime) + ((deltaTime * deltaTime) / 2.0);
		}
		else {

			double deltaPosition0 = (double) distance0;

			double deltaTimeLeg1 = deltaTime - deltaTime0;

			double deltaPosition1 = (velocity1 * deltaTimeLeg1);

			deltaPosition = deltaPosition0 + deltaPosition1;
		}

		return deltaPosition;
	}

	// no acceleration
	// single block
	public double moveDerivePositionConstant(int energy, double deltaTime) {

		assert MathUtility.rangeCheck(energy, 0, configure.energyMaximum + 1);

		double velocity = Math.sqrt((double) energy);

		return (velocity * deltaTime);
	}

	public double moveDeriveTimeConstant(int energy) {

		assert MathUtility.rangeCheck(energy, 0, configure.energyMaximum + 1);

		double velocity = Math.sqrt((double) energy);

		final double Distance = 1.0;

		return Distance / velocity;
	}

	public int energyCap(int energy) {

		return MathUtility.cap(energy, configure.energyMaximum);
	}
}
