package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Carriage;

/**
 * Created by Ben Bernard on 2/3/2018.
 */
public class AutoZeroCarriage extends Command {

    private Carriage _carriage;
    private long endMillis;
    private long _timeout = 15000;

    public AutoZeroCarriage(Carriage carriage) {
        requires(carriage);
        _carriage = carriage;
    }

    @Override
    protected void initialize() {
        super.initialize();
        DriverStation.reportError("Starting AutoZeroCarriage", false);
        endMillis = System.currentTimeMillis() + _timeout;
    }

    @Override
    protected void execute() {
        _carriage.drive(Constants.Carriage.ZERO_SPEED, true);
        super.execute();
    }

    @Override
    protected void end() {
        _carriage.drive(0, true);
        super.end();
    }

    @Override
    protected boolean isFinished() {
        if(System.currentTimeMillis()>endMillis){
            DriverStation.reportError("AutoZeroCarriage timed out after " + _timeout + "ms" , false);
            return true;
        }
        if (_carriage.isAtTop()) {
            DriverStation.reportError("AutoZeroCarriage completed ", false);
            _carriage.zeroEncoder();
            return true;
        }
        return false;
    }
}
