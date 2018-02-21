package org.frc5687.powerup.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5687.powerup.robot.Constants;
import org.frc5687.powerup.robot.OI;
import org.frc5687.powerup.robot.RobotMap;
import org.frc5687.powerup.robot.commands.DriveWith2Joysticks;

/**
 * Created by Baxter on 3/22/2017.
 */
public class DriveTrain extends Subsystem implements PIDSource {

    private TalonSRX leftFrontMotor;
    private VictorSPX leftRearMotor;
    private TalonSRX rightFrontMotor;
    private VictorSPX rightRearMotor;

    private AHRS imu;
    private OI oi;

    public double HIGH_POW = 1.0;
    public double LOW_POW = -HIGH_POW;

    public DriveTrain(AHRS imu, OI oi) {
        // Motor Initialization
        leftFrontMotor = new TalonSRX(RobotMap.CAN.TalonSRX.LEFT_FRONT_MOTOR);
        leftRearMotor = new VictorSPX(RobotMap.CAN.VictorSPX.LEFT_BACK_MOTOR);
        rightFrontMotor = new TalonSRX(RobotMap.CAN.TalonSRX.RIGHT_FRONT_MOTOR);
        rightRearMotor = new VictorSPX(RobotMap.CAN.VictorSPX.RIGHT_BACK_MOTOR);

        // Setup slaves to follow their master
        leftRearMotor.follow(leftFrontMotor);
        rightRearMotor.follow(rightFrontMotor);

        // Setup motors
        leftFrontMotor.configPeakOutputForward(HIGH_POW, 0);
        leftRearMotor.configPeakOutputForward(HIGH_POW, 0);
        rightFrontMotor.configPeakOutputForward(HIGH_POW, 0);
        rightRearMotor.configPeakOutputForward(HIGH_POW, 0);

        leftFrontMotor.configPeakOutputReverse(LOW_POW, 0);
        leftRearMotor.configPeakOutputReverse(LOW_POW, 0);
        rightFrontMotor.configPeakOutputReverse(LOW_POW, 0);
        rightRearMotor.configPeakOutputReverse(LOW_POW, 0);

        leftFrontMotor.configNominalOutputForward(0.0, 0);
        leftRearMotor.configNominalOutputForward(0.0, 0);
        rightFrontMotor.configNominalOutputForward(0.0, 0);
        rightRearMotor.configNominalOutputForward(0.0, 0);

        leftFrontMotor.configNominalOutputReverse(0.0, 0);
        leftRearMotor.configNominalOutputReverse(0.0, 0);
        rightFrontMotor.configNominalOutputReverse(0.0, 0);
        rightRearMotor.configNominalOutputReverse(0.0, 0);

        leftFrontMotor.setInverted(Constants.DriveTrain.LEFT_MOTORS_INVERTED);
        leftRearMotor.setInverted(Constants.DriveTrain.LEFT_MOTORS_INVERTED);
        rightFrontMotor.setInverted(Constants.DriveTrain.RIGHT_MOTORS_INVERTED);
        rightRearMotor.setInverted(Constants.DriveTrain.RIGHT_MOTORS_INVERTED);

        // Encoders

        leftFrontMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        rightFrontMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);

        this.imu = imu;
        this.oi = oi;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveWith2Joysticks(this, oi));
    }

    public void setPower(double leftSpeed, double rightSpeed) {
        leftFrontMotor.set(ControlMode.PercentOutput, leftSpeed);
        rightFrontMotor.set(ControlMode.PercentOutput, rightSpeed);
        SmartDashboard.putNumber("DriveTrain/Speed/Right", rightSpeed);
        SmartDashboard.putNumber("DriveTrain/Speed/Left", leftSpeed);
    }


    private Encoder initializeEncoder(int channelA, int channelB, boolean reversed, double distancePerPulse) {
        Encoder encoder = new Encoder(channelA, channelB, reversed, Encoder.EncodingType.k4X);
        encoder.setDistancePerPulse(distancePerPulse);
        encoder.setMaxPeriod(Constants.Encoders.Defaults.MAX_PERIOD);
        encoder.setSamplesToAverage(Constants.Encoders.Defaults.SAMPLES_TO_AVERAGE);
        encoder.reset();
        return encoder;
    }

    public void resetDriveEncoders() {
        leftFrontMotor.setSelectedSensorPosition(0,0,0);
        rightFrontMotor.setSelectedSensorPosition(0, 0, 0);
    }

    public float getYaw() {
        return imu.getYaw();
    }

    public float getCheesyYaw() {
        return -getYaw();
    }

    /**
     * Get the number of ticks since the last reset
     * @return
     */
    public long getLeftTicks() {
        return leftFrontMotor.getSelectedSensorPosition(0);
    }

    /**
     * The left distance in Inches since the last reset.
     * @return
     */
    public double getLeftDistance() {
        return leftFrontMotor.getSelectedSensorPosition(0) * Constants.Encoders.LeftDrive.INCHES_PER_PULSE;
    }

    /**
     * Get the number of ticks in the past 100ms
     * @return
     */
    public double getLeftRate() {
        return leftFrontMotor.getSelectedSensorVelocity(0);
    }

    /**
     * Get the number of inches in the past 100ms
     * @return
     */
    public double getLeftSpeed() {
        return getLeftRate() * Constants.Encoders.LeftDrive.INCHES_PER_PULSE;
    }

    public double getLeftVelocityIPS() {
        return getLeftSpeed() * 10;
    }

    public double getLeftRPS() {
        return getLeftRate() * 10 / Constants.Encoders.Defaults.PULSES_PER_ROTATION;
    }

    /**
     * Get the number of pulses since the last reset
     * @return
     */
    public long getRightTicks() {
        return rightFrontMotor.getSelectedSensorPosition(0);
    }

    /**
     *
     * @return The distance traveled in inches.
     */
    public double getRightDistance() {
        return getRightTicks() * Constants.Encoders.RightDrive.INCHES_PER_PULSE;
    }

    /**
     * Get the number of pulses in the last 100ms.
     * @return
     */
    public double getRightRate() {
        return rightFrontMotor.getSelectedSensorVelocity(0);
    }

    /**
     * Get the number of inches traveled in the last 100ms
     * @return
     */
    public double getRightSpeed() {
        return getRightRate() * Constants.Encoders.RightDrive.INCHES_PER_PULSE;
    }

    public double getRightVelocityIPS() {
        return getRightSpeed() * 10;
    }

    public double getRightRPS() {
        return getRightRate() * 10 / Constants.Encoders.Defaults.PULSES_PER_ROTATION;
    }


    /**
     * @return average of leftDistance and rightDistance
     */
    public double getDistance() {
        if (Math.abs(getRightTicks())<10) {
            return getLeftDistance();
        }
        if (Math.abs(getLeftTicks())<10) {
            return getRightDistance();
        }
        return (getLeftDistance() + getRightDistance()) / 2;
    }

    @Override
    public double pidGet() {
        return getDistance();
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
    }

    protected class EncoderPIDSource implements PIDSource {
        private DriveTrain _driveTrain;

        protected EncoderPIDSource(DriveTrain driveTrain) {
            _driveTrain = driveTrain;
        }

        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {

        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return null;
        }

        @Override
        public double pidGet() {
            return 0;
        }
    }

    protected class UltrasonicPIDSource implements PIDSource {

        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {

        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return null;
        }

        @Override
        public double pidGet() {
            return 0;
        }
    }

    public void updateDashboard() {
        SmartDashboard.putNumber("DriveTrain/observedHeadingCheesy", -getYaw());
        SmartDashboard.putNumber("DriveTrain/Distance/Right", getRightDistance());
        SmartDashboard.putNumber("DriveTrain/Distance/Left", getLeftDistance());

        SmartDashboard.putNumber("DriveTrain/Ticks/Right", getRightTicks());
        SmartDashboard.putNumber("DriveTrain/Ticks/Left", getLeftTicks());

        SmartDashboard.putNumber("DriveTrain/Rate/Right", getRightRate());
        SmartDashboard.putNumber("DriveTrain/Rate/Left", getLeftRate());

        SmartDashboard.putNumber("DriveTrain/RPS/Right", getRightRPS());
        SmartDashboard.putNumber("DriveTrain/RPS/Left", getLeftRPS());

        SmartDashboard.putBoolean("DriveTrain/Inverted/Right", Constants.DriveTrain.RIGHT_MOTORS_INVERTED);
        SmartDashboard.putBoolean("DriveTrain/Inverted/Left", Constants.DriveTrain.LEFT_MOTORS_INVERTED);

        SmartDashboard.putNumber("IMU/yaw", imu.getYaw());
        SmartDashboard.putData("IMU", imu);
    }

    @Override
    public void periodic() {

    }
}
