package org.firstinspires.ftc.teamcode.common.drive.drive.swerve;

import static org.firstinspires.ftc.teamcode.common.hardware.Globals.USE_WHEEL_FEEDFORWARD;
import static java.lang.Math.atan2;
import static java.lang.Math.hypot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.drive.drive.Drivetrain;
import org.firstinspires.ftc.teamcode.common.drive.geometry.MathUtils;
import org.firstinspires.ftc.teamcode.common.drive.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.hardware.AbsoluteAnalogEncoder;
import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;

@Config
public class SwerveDrivetrain implements Drivetrain {
    public SwerveModule frontLeftModule, backLeftModule, backRightModule, frontRightModule;
    public SwerveModule[] modules;

    public static double TRACK_WIDTH = 8.386, WHEEL_BASE = 8.386;
    private final double R;
    public static double
            frontLeftOffset = -0.1571,
            frontRightOffset = 0.1,
            backLeftOffset = 0.05,
            backRightOffset = -0.195;

    public static boolean maintainHeading = false;

    double[] ws = new double[4];
    double[] wa = new double[4];
    double max = 0.0;

    public final double minPow = 0.1;
    public static double imuOffset = 0.0;

    private boolean locked = false;

    public SwerveDrivetrain(RobotHardware robot) {
        frontLeftModule = new SwerveModule(robot.frontLeftMotor, robot.frontLeftServo, new AbsoluteAnalogEncoder(robot.frontLeftEncoder, 3.3).zero(frontLeftOffset).setInverted(false));
        backLeftModule = new SwerveModule(robot.backLeftMotor, robot.backLeftServo, new AbsoluteAnalogEncoder(robot.backLeftEncoder, 3.3).zero(backLeftOffset).setInverted(false));
        backRightModule = new SwerveModule(robot.backRightMotor, robot.backRightServo, new AbsoluteAnalogEncoder(robot.backRightEncoder, 3.3).zero(backRightOffset).setInverted(false));
        frontRightModule = new SwerveModule(robot.frontRightMotor, robot.frontRightServo, new AbsoluteAnalogEncoder(robot.frontRightEncoder, 3.3).zero(frontRightOffset).setInverted(false));

        modules = new SwerveModule[]{frontLeftModule, frontRightModule, backRightModule, backLeftModule};
        for (SwerveModule m : modules) m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        R = hypot(TRACK_WIDTH, WHEEL_BASE);
    }

    public void read() {
        for (SwerveModule module : modules) module.read();
    }


    @Override
    public void set(Pose pose) {
        frontLeftModule.encoder.zero(frontLeftOffset);
        backLeftModule.encoder.zero(backLeftOffset);
        backRightModule.encoder.zero(backRightOffset);
        frontRightModule.encoder.zero(frontRightOffset);

        double x = pose.x, y = pose.y, head = pose.heading;

        double a = x - head * (WHEEL_BASE / R),
                b = x + head * (WHEEL_BASE / R),
                c = y - head * (TRACK_WIDTH / R),
                d = y + head * (TRACK_WIDTH / R);

        if (locked) {
            ws = new double[]{0, 0, 0, 0};
            wa = new double[]{0, 0, 0, 0};
//            wa = new double[]{Math.PI / 4, -Math.PI / 4, Math.PI / 4, -Math.PI / 4};
        } else {
            ws = new double[]{hypot(b, c), hypot(b, d), hypot(a, d), hypot(a, c)};
            if (!maintainHeading)
                wa = new double[]{atan2(b, c), atan2(b, d), atan2(a, d), atan2(a, c)};
        }

        max = MathUtils.max(ws);
    }

    public void write() {
        for (int i = 0; i < 4; i++) {
            SwerveModule m = modules[i];
            if (Math.abs(max) > 1) ws[i] /= max;
            m.setMotorPower(Math.abs(ws[i]) + ((USE_WHEEL_FEEDFORWARD) ? minPow * Math.signum(ws[i]) : 0));
            m.setTargetRotation(MathUtils.norm(wa[i]));
        }
    }

    public void updateModules() {
        for (SwerveModule m : modules) m.update();
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public String getTelemetry() {
        return frontLeftModule.getTelemetry("leftFrontModule") + "\n" +
                backLeftModule.getTelemetry("leftRearModule") + "\n" +
                frontRightModule.getTelemetry("rightFrontModule") + "\n" +
                backRightModule.getTelemetry("rightRearModule") + "\n";
    }
}