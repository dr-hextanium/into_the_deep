package org.firstinspires.ftc.teamcode.intothedeep.opmode.demo

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.frozenmilk.dairy.calcified.Calcified
import dev.frozenmilk.util.units.angle.deg
import dev.frozenmilk.util.units.angle.linearDeg
import dev.frozenmilk.util.units.position.Vector2D
import org.firstinspires.ftc.teamcode.intothedeep.hardware.swerve.Swerve
import org.firstinspires.ftc.teamcode.intothedeep.hardware.swerve.Swerve.Companion.maxVel
import org.firstinspires.ftc.teamcode.intothedeep.hardware.swerve.SwerveModule

@TeleOp(group = "Testing")
@Calcified.Attach
class SwerveDrive : OpMode() {
    val swerve by lazy { Swerve() }

    override fun init() { swerve }

    override fun loop() {
        val (x, y) = Pair(-gamepad1.left_stick_y.toDouble(), gamepad1.left_stick_x.toDouble())
        val (v, w) = Pair(355.linearDeg * gamepad1.left_trigger.toDouble(), 355.linearDeg * gamepad1.right_trigger.toDouble())

        val velocity = Vector2D(maxVel * x, maxVel * y)
        val rotation = (v - w).intoLinear()

        telemetry.addData("velocity", velocity)
        telemetry.addData("rotation", rotation)

        swerve.update(velocity, rotation)
    }
}