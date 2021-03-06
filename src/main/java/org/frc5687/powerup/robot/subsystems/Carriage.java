package org.frc5687.powerup.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveCarriage;
import org.frc5687.powerup.robot.utils.MotorHealthChecker;
import org.frc5687.powerup.robot.utils.PDP;

public class Carriage extends PIDSubsystem {
    private PDP _pdp;
    private Encoder encoder;
    private VictorSP _motor;
    private OI _oi;
    private DigitalInput hallEffectTop;
    private DigitalInput hallEffectBottom;
    private boolean _isCompetitionBot;

    private MotorHealthChecker _healthChecker;

    public static final double kP = 0.5;
    public static final double kI = 0.1;
    public static final double kD = 0.1;
    public static final double kF = 0.5;

    public Carriage(OI oi, PDP pdp, boolean isCompetitionBot) {
        super("Carriage", kP, kI, kD, kF);
        setAbsoluteTolerance(15);
        setInputRange(
                isCompetitionBot ? Constants.Carriage.ENCODER_BOTTOM_COMP : Constants.Carriage.ENCODER_BOTTOM_PROTO,
                isCompetitionBot ? Constants.Carriage.ENCODER_TOP_COMP : Constants.Carriage.ENCODER_TOP_PROTO
        );
        setOutputRange(Constants.Carriage.MINIMUM_SPEED, Constants.Carriage.MAXIMUM_SPEED);
        _oi = oi;
        _pdp = pdp;
        _motor = new VictorSP(RobotMap.Carriage.MOTOR);
        encoder = new Encoder(RobotMap.Carriage.ENCODER_A, RobotMap.Carriage.ENCODER_B);
        hallEffectTop = new DigitalInput(RobotMap.Carriage.HALL_EFFECT_TOP);
        hallEffectBottom = new DigitalInput(RobotMap.Carriage.HALL_EFFECT_BOTTOM);
        _isCompetitionBot = isCompetitionBot;
        _healthChecker = new MotorHealthChecker(Constants.Carriage.HC_MIN_SPEED, Constants.Carriage.HC_MIN_CURRENT, Constants.HEALTH_CHECK_CYCLES, _pdp, RobotMap.PDP.CARRIAGE_SP);
    }

    @Override
    public void disable() {
        super.disable();
        DriverStation.reportError("Carriage PIDSubsystem Disabling", false);
    }

    public double calculateHoldSpeed() {
        double pos = getPos();
        if (pos > 0) {
            return _isCompetitionBot ? Constants.Carriage.HoldSpeeds.PAST_TOP_GRETA : Constants.Carriage.HoldSpeeds.PAST_TOP_PROTO;
        } else if (pos > -20) {
            return _isCompetitionBot ? Constants.Carriage.HoldSpeeds.PAST_NEG_20_GRETA : Constants.Carriage.HoldSpeeds.PAST_NEG_20_PROTO;
        } else if (pos > -50) {
            return _isCompetitionBot ? Constants.Carriage.HoldSpeeds.PAST_NEG_50_GRETA : Constants.Carriage.HoldSpeeds.PAST_NEG_50_PROTO;
        } else if (pos > -100) {
            return _isCompetitionBot ? Constants.Carriage.HoldSpeeds.PAST_NEG_100_GRETA : Constants.Carriage.HoldSpeeds.PAST_NEG_100_PROTO;
        } else if (pos > -200) {
            return _isCompetitionBot ? Constants.Carriage.HoldSpeeds.PAST_NEG_200_GRETA : Constants.Carriage.HoldSpeeds.PAST_NEG_200_PROTO;
        } else if (pos > -300) {
            return _isCompetitionBot ? Constants.Carriage.HoldSpeeds.PAST_NEG_300_GRETA : Constants.Carriage.HoldSpeeds.PAST_NEG_300_PROTO;
        } else if (pos > -400) {
            return _isCompetitionBot ? Constants.Carriage.HoldSpeeds.PAST_NEG_400_GRETA : Constants.Carriage.HoldSpeeds.PAST_NEG_400_PROTO;
        } else if (pos > -500) {
            return _isCompetitionBot ? Constants.Carriage.HoldSpeeds.PAST_NEG_500_GRETA : Constants.Carriage.HoldSpeeds.PAST_NEG_500_PROTO;
        } else if (pos > -600) {
            return _isCompetitionBot ? Constants.Carriage.HoldSpeeds.PAST_NEG_600_GRETA : Constants.Carriage.HoldSpeeds.PAST_NEG_600_PROTO;
        } else if (pos > -700) {
            return _isCompetitionBot ? Constants.Carriage.HoldSpeeds.PAST_NEG_700_GRETA : Constants.Carriage.HoldSpeeds.PAST_NEG_700_PROTO;
        } else if (pos > -800) {
            return _isCompetitionBot ? Constants.Carriage.HoldSpeeds.PAST_NEG_800_GRETA : Constants.Carriage.HoldSpeeds.PAST_NEG_800_PROTO;
        } else if (pos > -900) {
            return _isCompetitionBot ? Constants.Carriage.HoldSpeeds.PAST_NEG_900_GRETA : Constants.Carriage.HoldSpeeds.PAST_NEG_900_PROTO;
        } else {
            return _isCompetitionBot ? Constants.Carriage.HoldSpeeds.PAST_NEG_900_GRETA : Constants.Carriage.HoldSpeeds.PAST_NEG_900_PROTO;
        }
    }

    public void drive(double desiredSpeed) {
        drive(desiredSpeed, false);
    }

    public void drive(double desiredSpeed, boolean overrideCaps) {
        double speed = desiredSpeed;
        if (!overrideCaps) {
            if (speed > 0 && isAtTop()) {
                speed = calculateHoldSpeed();
            } else if (speed < 0 && isAtBottom()) {
                speed = calculateHoldSpeed();
            } else if (speed > 0 && isInTopZone()) {
                speed *= Constants.Carriage.ZONE_SPEED_LIMIT;
            } else if (speed < 0 && isInBottomZone()) {
                speed *= Constants.Carriage.ZONE_SPEED_LIMIT;
            }
            if (_pdp.excessiveCurrent(RobotMap.PDP.CARRIAGE_SP, Constants.Carriage.PDP_EXCESSIVE_CURRENT)) {
                speed = 0.0;
            }


            _healthChecker.checkHealth(speed);

            speed = Math.max(speed, Constants.Carriage.MINIMUM_SPEED);
        }

        SmartDashboard.putNumber("Carriage/rawSpeed", desiredSpeed);
        SmartDashboard.putNumber("Carriage/speed", speed);
        _motor.setSpeed(speed * (Constants.Carriage.MOTOR_INVERTED ? -1 : 1));
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveCarriage(this, _oi));
    }

    public boolean isAtTop() {
        return !hallEffectTop.get();
    }

    public boolean isAtBottom() {
        return !hallEffectBottom.get();
    }

    public int getPos() {
        return encoder.get();
    }

    public void zeroEncoder() {
        encoder.reset();
    }

    @Override
    protected double returnPIDInput() {
        return encoder.get();
    }

    @Override
    protected void usePIDOutput(double output) {
        SmartDashboard.putNumber("Carriage/PID output", output);
        drive(output);
    }

    public void updateDashboard() {
        SmartDashboard.putNumber("Carriage/position", getPos());
        SmartDashboard.putBoolean("Carriage/At top", isAtTop());
        SmartDashboard.putBoolean("Carriage/At bottom", isAtBottom());
        SmartDashboard.putBoolean("Carriage/In top zone", isInTopZone());
        SmartDashboard.putBoolean("Carriage/In bottom zone", isInBottomZone());
        SmartDashboard.putNumber("Carriage/theoreticalHoldSpeed", calculateHoldSpeed());
        SmartDashboard.putBoolean("Carriage/Is healthy", _healthChecker.IsHealthy());
    }

    public boolean isCompetitionBot() {
        return _isCompetitionBot;
    }

    public boolean isHealthy() {
        return _healthChecker.IsHealthy();
    }

    public boolean isInTopZone() {
        return getPos() > (_isCompetitionBot ? Constants.Carriage.START_TOP_ZONE_COMP : Constants.Carriage.START_TOP_ZONE_PROTO);
    }

    public boolean isInBottomZone() {
        return getPos() < (_isCompetitionBot ? Constants.Carriage.START_BOTTOM_ZONE_COMP : Constants.Carriage.START_BOTTOM_ZONE_PROTO);
    }

    public double estimateHeight() {
        if (_isCompetitionBot) {
            return Constants.Carriage.TOP_INCHES + ((getPosition() / Constants.Carriage.ENCODER_RANGE_COMP) * Constants.Carriage.RANGE_INCHES);
        } else {
            return Constants.Carriage.TOP_INCHES + ((getPosition() / Constants.Carriage.ENCODER_RANGE_PROTO) * Constants.Carriage.RANGE_INCHES);
        }
    }
}
