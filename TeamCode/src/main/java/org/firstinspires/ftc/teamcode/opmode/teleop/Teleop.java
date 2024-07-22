package org.firstinspires.ftc.teamcode.opmode.teleop;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.normalizeRadians;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.drive.drive.swerve.SlewRateLimiter;
import org.firstinspires.ftc.teamcode.common.drive.drive.swerve.SwerveDrivetrain;
import org.firstinspires.ftc.teamcode.common.drive.geometry.Point;
import org.firstinspires.ftc.teamcode.common.drive.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.hardware.Globals;
import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;

@Config
@TeleOp(name = "Swerve Demo")
public class Teleop extends CommandOpMode {
    private ElapsedTime timer;
    private double loopTime = 0;

    private double targetHeading;

    private final RobotHardware robot = RobotHardware.getInstance();
    private SwerveDrivetrain drivetrain;

    private SlewRateLimiter fw;
    private SlewRateLimiter str;

    public static double kP = 0.17;
    public static double kI = 0.18;
    public static double kD = 0.5;
    public static double kF = 0.05;

    private final PIDFController hController = new PIDFController(kP, kI, kD, kF);

    public static double fw_r = 4;
    public static double str_r = 4;
    private boolean lock_robot_heading = false;

    GamepadEx gamepadEx, gamepadEx2;


    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Globals.AUTO = false;
        Globals.USING_IMU = true;
        Globals.USE_WHEEL_FEEDFORWARD = false;

        robot.init(hardwareMap, telemetry);
        drivetrain = new SwerveDrivetrain(robot);
        gamepadEx = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);
        robot.enabled = true;

        robot.hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
    }

    @Override
    public void run() {
        super.run();
        if (timer == null) {
            timer = new ElapsedTime();
            robot.startIMUThread(this);
            fw = new SlewRateLimiter(fw_r);
            str = new SlewRateLimiter(str_r);
        }

        robot.read(drivetrain);

        hController.setPIDF(kP, kI, kD, kF);

        if (gamepad1.right_stick_button && Globals.USING_IMU)
            SwerveDrivetrain.imuOffset = robot.getAngle() + Math.PI;

        if (gamepad1.right_stick_y > 0.25) {
            lock_robot_heading = true;
            targetHeading = Math.PI + SwerveDrivetrain.imuOffset;
        }
        if (gamepad1.right_stick_y < -0.25) {
            lock_robot_heading = true;
            targetHeading = 0 + SwerveDrivetrain.imuOffset;
        }

        double turn = gamepad1.left_trigger - gamepad1.right_trigger;
        if (Math.abs(turn) > 0.002) {
            lock_robot_heading = false;
        }

        double error = normalizeRadians(normalizeRadians(targetHeading) - normalizeRadians(robot.getAngle()));
        double headingCorrection = hController.calculate(0, error) * 12.4 / robot.getVoltage();

        telemetry.addData("error", error);
        telemetry.addData("correction", headingCorrection);

        if (Math.abs(headingCorrection) < 0.01 || Math.abs(error) < 0.05) {
            headingCorrection = 0;
        }

        SwerveDrivetrain.maintainHeading =
                (Math.abs(gamepad1.left_stick_x) < 0.002 &&
                        Math.abs(gamepad1.left_stick_y) < 0.002 &&
                        Math.abs(turn) < 0.002) &&
                        Math.abs(headingCorrection) < 0.02;


        double rotationAmount = (Globals.USING_IMU)
                ? robot.getAngle() - SwerveDrivetrain.imuOffset
                : 0;

        Pose drive = new Pose(
                new Point(joystickScalar(gamepad1.left_stick_y, 0.001),
                        joystickScalar(gamepad1.left_stick_x, 0.001)).rotate(rotationAmount),
                lock_robot_heading ? headingCorrection :
                        joystickScalar(turn, 0.01)
        );

        drive = new Pose(
                fw.calculate(drive.x),
                str.calculate(drive.y),
                drive.heading
        );

        robot.loop(drive, drivetrain);
        robot.write(drivetrain);

        double loop = System.nanoTime();

        telemetry.addData("hz ", 1e9 / (loop - loopTime));
        loopTime = loop;
        telemetry.update();

        robot.clearBulkCache();
    }

    private double joystickScalar(double num, double min) {
        return joystickScalar(num, min, 0.66, 4);
    }

    private double joystickScalar(double n, double m, double l, double a) {
        return Math.signum(n) * m
                + (1 - m) *
                (Math.abs(n) > l ?
                        Math.pow(Math.abs(n), Math.log(l / a) / Math.log(l)) * Math.signum(n) :
                        n / a);
    }
}
