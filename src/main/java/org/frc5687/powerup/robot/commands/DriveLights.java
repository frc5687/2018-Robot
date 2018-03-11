package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.subsystems.Lights;

public class DriveLights extends Command {
    private Lights _lights;

    public DriveLights(Robot robot) {
        _lights = robot.getLights();
        requires(_lights);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void initialize() {
        _lights.initialize();
    }

    @Override
    protected void execute() {
        _lights.setColors();
    }
}
