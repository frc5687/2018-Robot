package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Waypoint;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.commands.MoveCarriageToSetpointPID;
import org.frc5687.powerup.robot.utils.Helpers;

/**
 * Created by Ben Bernard on 2/2/2018.
 */
public class AutoGroup extends CommandGroup {
    public AutoGroup(int mode, int position, int switchSide, int scaleSide, Robot robot) {
        super();
        int switchFactor = switchSide * (position );
        int scaleFactor = scaleSide * (position);

        addSequential(new AutoZeroCarriage(robot.getCarriage()));
        addSequential(new MoveCarriageToSetpointPID(robot.getCarriage(), Constants.Carriage.ENCODER_CLEAR_BUMPERS));

        // Start with the "always" operations
        // addParallel(new CarriageZeroEncoder(robot.getCarriage()));
        addParallel(new MoveCarriageToSetpointPID(robot.getCarriage(), Constants.Carriage.ENCODER_TOP));
        // addParallel(new MoveArmToSetpointPID(robot.getArm(), Constants.Arm.ENCODER_FENCE));
        double distance = 0.0;

        switch (mode) {
            case Constants.AutoChooser.Mode.STAY_PUT:
                // Nothing to do here but look sad
                break;

            case Constants.AutoChooser.Mode.CROSS_AUTOLINE:
                distance = 120;
                if (position>0 && position<5) { distance = 80.0; }

                if (switchSide==Constants.AutoChooser.LEFT) {
                    addParallel(new AutoDrive(robot.getDriveTrain(), distance, 0.75, true, true, 5000, "auto"));
                } else {
                    addParallel(new AutoDrive(robot.getDriveTrain(), distance, 0.75, true, true, 5000, "auto"));
                }

                break;

            case Constants.AutoChooser.Mode.SWITCH_ONLY:
                SmartDashboard.putString("Auto/Mode", "Switch Only");
                switch(switchFactor) {
                    case -Constants.AutoChooser.Position.FAR_LEFT: // Position 1, left side
                        sideSwitch(robot);
                        break;
                    case Constants.AutoChooser.Position.FAR_LEFT:  // Position 1, right side
                        break;
                    case -Constants.AutoChooser.Position.MID_LEFT: // Position 2, left side:
                        straightSwitch(robot);
                        break;
                    case Constants.AutoChooser.Position.MID_LEFT: // Position 2, right side
                        break;
                    case -Constants.AutoChooser.Position.CENTER: // Position 3, left side
                        break;
                    case Constants.AutoChooser.Position.CENTER: // Position 3, right side
                        break;
                    case -Constants.AutoChooser.Position.NEAR_RIGHT: // Position 4, left side
                        break;
                    case Constants.AutoChooser.Position.NEAR_RIGHT: // Position 4, right side
                        straightSwitch(robot);
                        break;
                    case -Constants.AutoChooser.Position.MID_RIGHT: // Position 5, left side
                        break;
                    case Constants.AutoChooser.Position.MID_RIGHT: // Position 5, right side
                        break;
                    case -Constants.AutoChooser.Position.FAR_RIGHT: // Position 6, left side
                        break;
                    case Constants.AutoChooser.Position.FAR_RIGHT: // Position 6, right side
                        break;
                }
                break;

        }
    }

    private void straightSwitch(Robot robot) {
        double distance = 95.0;
        addSequential(new AutoDrive(robot.getDriveTrain(), distance, 0.4, true, true, 5000, "auto"));
        addSequential(new AutoEject(robot.getIntake()));
    }

    private void sideSwitch(Robot robot) {
        DriverStation.reportError("Started sideSwitch", false);
        Waypoint[] points = new Waypoint[] {
                new Waypoint(0, 0, 0),
                new Waypoint(Helpers.i2m(14, 0), Helpers.i2m(0, 0), 0),
                new Waypoint(Helpers.i2m(13, 6), Helpers.i2m(0, -2), -30),
                //new Waypoint(Helpers.i2m(14, 0), Helpers.i2m(0, -6), -90)
        };
        addSequential(new AutoDrivePathfinder(robot.getDriveTrain(), points));
        addSequential(new AutoEject(robot.getIntake()));
    }

}
