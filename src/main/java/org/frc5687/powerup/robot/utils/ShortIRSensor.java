package org.frc5687.powerup.robot.utils;

import org.frc5687.powerup.robot.Constants;

/**
 * Created by Ben Bernard on 3/17/2018.
 */
public class ShortIRSensor extends IRSensor {

    public ShortIRSensor(int channel) { super(channel); }

    public double getDistance() {
        return Constants.IRPID.TRANSFORM_COEFFICIENT_SHORT * Math.pow(getRaw(), Constants.IRPID.TRANSFORM_POWER_SHORT) / 2.54;
    }


    public class sensorMedium {
        public double getDistance() {
            return Constants.IRPID.TRANSFORM_COEFFICIENT_MEDIUM * Math.pow(getRaw(), Constants.IRPID.TRANSFORM_POWER_MEDIUM) / 2.54;
        }
    }


    public class sensorLong {
        public double getDistance() {
            return Constants.IRPID.TRANSFORM_COEFFICIENT_LONG * Math.pow(getRaw(), Constants.IRPID.TRANSFORM_POWER_LONG) / 2.54;

        }
    }

}
