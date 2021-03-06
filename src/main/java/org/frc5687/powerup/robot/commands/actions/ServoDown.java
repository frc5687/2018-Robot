package org.frc5687.powerup.robot.commands.actions;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Intake;

public class ServoDown extends Command {
    private Intake _intake;
    private boolean _isFinished;

    public ServoDown(Intake intake) {
        _intake = intake;
    }

    @Override
    protected void initialize() {
        DriverStation.reportError("ServoDown initialized", false);
        _intake.driveServo(Constants.Intake.SERVO_BOTTOM);
        SmartDashboard.putNumber("Intake/Servo", Constants.Intake.SERVO_BOTTOM);
        _isFinished = true;
    }

    @Override
    protected void end() {
        DriverStation.reportError("ServoDown.end(): ServoDown finished", false);
    }

    @Override
    protected boolean isFinished() {
        return _isFinished;
    }
}
