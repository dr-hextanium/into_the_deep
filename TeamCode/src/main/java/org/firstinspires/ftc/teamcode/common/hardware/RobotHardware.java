package org.firstinspires.ftc.teamcode.common.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.common.drive.drive.swerve.SwerveDrivetrain;
import org.firstinspires.ftc.teamcode.common.drive.geometry.Pose;

import javax.annotation.concurrent.GuardedBy;

public class RobotHardware {
    public DcMotorEx frontLeftMotor;
    public DcMotorEx frontRightMotor;
    public DcMotorEx backLeftMotor;
    public DcMotorEx backRightMotor;

    public CRServo frontLeftServo;
    public CRServo frontRightServo;
    public CRServo backLeftServo;
    public CRServo backRightServo;

    public AnalogInput frontLeftEncoder;
    public AnalogInput frontRightEncoder;
    public AnalogInput backLeftEncoder;
    public AnalogInput backRightEncoder;


    private final Object imuLock = new Object();
    @GuardedBy("imuLock")
    public BNO055IMU imu;
    private Thread imuThread;
    private double imuAngle = 0;
    private double imuOffset = 0;
    private double voltage = 0.0;
    private ElapsedTime voltageTimer;

    private static RobotHardware instance = null;

    public boolean enabled;

    private HardwareMap hardwareMap;

    private final double startingIMUOffset = Math.PI / 2;

    public LynxModule hub;

    public static RobotHardware getInstance() {
        if (instance == null) {
            instance = new RobotHardware();
        }
        instance.enabled = true;
        return instance;
    }

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        if (Globals.USING_IMU) {
            synchronized (imuLock) {
                imu = hardwareMap.get(BNO055IMU.class, "imu");
                BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
                parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
                imu.initialize(parameters);
            }
        }

        hub = hardwareMap.getAll(LynxModule.class).get(0);

        voltageTimer = new ElapsedTime();

        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "front left drive");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "front right drive");
        backLeftMotor = hardwareMap.get(DcMotorEx.class, "back left drive");
        backRightMotor = hardwareMap.get(DcMotorEx.class, "back right drive");

        frontLeftServo = hardwareMap.get(CRServo.class, "front left steer");
        frontRightServo = hardwareMap.get(CRServo.class, "front right steer");
        backLeftServo = hardwareMap.get(CRServo.class, "back left steer");
        backRightServo = hardwareMap.get(CRServo.class, "back right steer");

        frontLeftServo.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightServo.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftServo.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightServo.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftEncoder = hardwareMap.get(AnalogInput.class, "front left encoder");
        frontRightEncoder = hardwareMap.get(AnalogInput.class, "front right encoder");
        backLeftEncoder = hardwareMap.get(AnalogInput.class, "back left encoder");
        backRightEncoder = hardwareMap.get(AnalogInput.class, "back right encoder");

        voltage = hardwareMap.voltageSensor.iterator().next().getVoltage();
    }

    public void loop(Pose drive, SwerveDrivetrain drivetrain) {
        if (drive != null) {
            drivetrain.set(drive);
        }

        drivetrain.updateModules();

        if (voltageTimer.seconds() > 5) {
            voltageTimer.reset();
            voltage = hardwareMap.voltageSensor.iterator().next().getVoltage();
        }
    }

    public void read(SwerveDrivetrain drivetrain) {
        drivetrain.read();
    }

    public void write(SwerveDrivetrain drivetrain) {
        drivetrain.write();
    }

    public void reset() {
        imuOffset = imu.getAngularOrientation().firstAngle;
    }

    public void clearBulkCache() { hub.clearBulkCache(); }

    public double getAngle() {
        return imuAngle - imuOffset;
    }

    public void startIMUThread(LinearOpMode opMode) {
        if (Globals.USING_IMU) {
            imuThread = new Thread(() -> {
                while (!opMode.isStopRequested() && opMode.opModeIsActive()) {
                    synchronized (imuLock) {
                        imuAngle = imu.getAngularOrientation().firstAngle + startingIMUOffset;
                    }
                }
            });
            imuThread.start();
        }
    }

    public double getVoltage() {
        return voltage;
    }
}
