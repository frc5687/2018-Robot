package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.subsystems.Arm;

public class DriveArm extends Command {
    private Arm arm;
    private OI oi;

    public DriveArm(Arm arm, OI oi){
        requires(arm);
        this.arm = arm;
        this.oi = oi;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void execute() {
        // TODO: Add protobot value
        double speed = DriverStation.getInstance().isAutonomous()
                ? Constants.Arm.HOLD_SPEED_COMP :
                oi.getArmSpeed();
        SmartDashboard.putNumber("Arm/Speed", speed);
        arm.drive(speed);
    }
}
