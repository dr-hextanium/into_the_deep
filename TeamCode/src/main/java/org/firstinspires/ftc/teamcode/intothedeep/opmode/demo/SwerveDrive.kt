package org.firstinspires.ftc.teamcode.intothedeep.opmode.demo

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.IMU
import dev.frozenmilk.dairy.calcified.Calcified
import dev.frozenmilk.util.units.angle.Angle
import dev.frozenmilk.util.units.angle.linearDeg
import dev.frozenmilk.util.units.angle.wrappedDeg
import dev.frozenmilk.util.units.angle.wrappedRad
import dev.frozenmilk.util.units.position.Vector2D
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.intothedeep.hardware.swerve.Swerve
import org.firstinspires.ftc.teamcode.intothedeep.hardware.swerve.Swerve.Companion.maxVel

@TeleOp(group = "Testing")
@Calcified.Attach
class SwerveDrive : OpMode() {
    val swerve by lazy { Swerve() }
    val imu by lazy { hardwareMap["imu"] as IMU }

    val heading: Angle
        get() = imu.robotYawPitchRollAngles
            .getYaw(AngleUnit.DEGREES)
            .wrappedDeg

    override fun init() {
        swerve

        imu.initialize(IMU.Parameters(orientation))
    }

    override fun loop() {
        val (x, y) = Pair(-gamepad1.left_stick_y.toDouble(), gamepad1.left_stick_x.toDouble())
        val (v, w) = Pair(540.linearDeg * gamepad1.left_trigger.toDouble(), 540.linearDeg * gamepad1.right_trigger.toDouble())

        val velocity = Vector2D(maxVel * x, maxVel * y).rotate(-heading)
        val rotation = (v - w).intoLinear()

        if (gamepad1.left_stick_button) imu.resetYaw()

        telemetry.addData("velocity", velocity)
        telemetry.addData("rotation", rotation)
        telemetry.addData("heading", heading)

        swerve.update(velocity, rotation)
    }

    companion object {
        val orientation = RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
            RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        )
    }
}