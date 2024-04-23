package org.firstinspires.ftc.teamcode.swerve.opmode.demo

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.frozenmilk.dairy.calcified.Calcified
import dev.frozenmilk.dairy.core.FeatureRegistrar
import dev.frozenmilk.util.units.angle.rad
import dev.frozenmilk.util.units.angle.wrappedRad
import dev.frozenmilk.util.units.distance.DistanceUnits
import dev.frozenmilk.util.units.position.Vector2D
import org.firstinspires.ftc.teamcode.swerve.hardware.swerve.SwerveModule.Companion.Hardware.max_vel
import org.firstinspires.ftc.teamcode.swerve.hardware.swerve.Swerve
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.hypot

@TeleOp
@Calcified.Attach(automatedCacheHandling = true)
class Testing : OpMode() {
    init { FeatureRegistrar.checkFeatures(this, Calcified) }

    val swerve by lazy { Swerve() }

    override fun init() {}

    override fun loop() {
        val (x, y) = Pair(
            gamepad1.left_stick_x.toDouble(),
            -gamepad1.left_stick_y.toDouble()
        )

        val ratio = hypot(x, y)
        val velocity = Vector2D(max_vel * ratio, atan2(y, x).rad)
        val angular = (gamepad1.right_stick_x).toDouble().wrappedRad * PI

        telemetry.addData("vel", velocity.into(DistanceUnits.INCH))
        telemetry.addData("ang", angular)
        telemetry.addData("fr output", swerve.output)

        telemetry.addData("motor powers", swerve.modules.map { it.motor.power }.joinToString(", "))
        telemetry.addData("motor targets", swerve.modules.map { it.drive.target }.joinToString(", "))

        telemetry.addData("angle positions", swerve.modules.map { it.encoder.position }.joinToString(", "))
        telemetry.addData("angle analog positions", swerve.modules.map { it.analog.position }.joinToString(", "))

        telemetry.addData("servo powers", swerve.modules.map { it.servo.power }.joinToString(", "))
        telemetry.addData("servo targets", swerve.modules.map { it.turn.target }.joinToString(", "))
        telemetry.addData("servo errors", swerve.modules.map { it.turn.error(it.turn.target) }.joinToString(", "))

        swerve.update(velocity)
    }
}