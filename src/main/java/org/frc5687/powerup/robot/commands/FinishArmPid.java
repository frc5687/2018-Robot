package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.subsystems.Arm;

public class FinishArmPid extends Command {
    private Arm _arm;
    private boolean _isFinished;

    public FinishArmPid(Arm arm) {
        _arm = arm;
    }

    @Override
    protected void initialize() {
        DriverStation.reportError("You should not be using this Command!!! (FinishArmPid)", false);
        _arm.disable();
        _isFinished = true;
    }

    @Override
    protected boolean isFinished() {
        return _isFinished;
    }
}
