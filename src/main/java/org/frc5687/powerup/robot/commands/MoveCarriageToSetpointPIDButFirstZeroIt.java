package org.frc5687.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5687.powerup.robot.Robot;
import org.frc5687.powerup.robot.commands.auto.AutoZeroCarriage;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class MoveCarriageToSetpointPIDButFirstZeroIt extends CommandGroup {

    public MoveCarriageToSetpointPIDButFirstZeroIt(Robot robot, int carriageTarget) {
        this(robot.getCarriage(), carriageTarget);
    }

    public MoveCarriageToSetpointPIDButFirstZeroIt(Carriage carriage, int carriageTarget) {
        addSequential(new AutoZeroCarriage(carriage));
        addSequential(new MoveCarriageToSetpointPID(carriage, carriageTarget));
    }
}
