package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.commands.DriveClimber;
import org.frc5687.powerup.robot.utils.PDP;

public class Climber extends Subsystem {

    private VictorSP motor;

    private OI oi;
    private PDP _pdp;
    private double _lastSpeed = 0;

    public Climber(OI oi, PDP pdp) {
        motor = new VictorSP(RobotMap.Climber.MOTOR);
        motor.setName("Climber");
        this.oi = oi;
        _pdp = pdp;
        _lastSpeed = 0;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveClimber(this, oi));
    }

    public void drive(double speed) {
        speed *= (Constants.Climber.MOTOR_INVERT ? -1 : 1);
        if (_pdp.excessiveCurrent(RobotMap.PDP.CLIMBER_SP, Constants.Climber.PDP_EXCESSIVE_CURRENT)) {
            speed = 0.0;
        }
        SmartDashboard.putNumber("Climber/speed", speed);
        _lastSpeed = speed;
        motor.set(speed);
    }

    public double getDirection() {
        return _lastSpeed;
    }
}