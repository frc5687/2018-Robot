package org.frc5687.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.subsystems.Carriage;

public class ClearBumpersIfNeeded extends Command {
    private Carriage _carriage;
    private int ENCODER_CLEAR_BUMPERS;
    private long endTimeMillis;

    public ClearBumpersIfNeeded(Carriage carriage) {
        _carriage = carriage;
        requires(carriage);
    }

    @Override
    protected void initialize() {
        ENCODER_CLEAR_BUMPERS = _carriage.isCompetitionBot() ? Constants.Carriage.ENCODER_CLEAR_BUMPERS_COMP : Constants.Carriage.ENCODER_CLEAR_BUMPERS_PROTO;
        DriverStation.reportError("Starting ClearBumpersIfNeeded", false);
        _carriage.setSetpoint(ENCODER_CLEAR_BUMPERS);
        _carriage.enable();
        endTimeMillis = System.currentTimeMillis() + 15000;
    }

    @Override
    protected boolean isFinished() {
        if(System.currentTimeMillis() < endTimeMillis){
            DriverStation.reportError("ClearBumpersIfNeeded timed out", false);
            return true;
        }

        return _carriage.getPos() > ENCODER_CLEAR_BUMPERS;
    }

    @Override
    protected void end() {
        _carriage.disable();
        DriverStation.reportError("Ending ClearBumpersIfNeeded", false);
    }
}
