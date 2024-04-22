package org.firstinspires.ftc.teamcode.swerve

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.frozenmilk.dairy.calcified.Calcified
import dev.frozenmilk.dairy.core.FeatureRegistrar
import dev.frozenmilk.util.units.angle.rad
import dev.frozenmilk.util.units.distance.DistanceUnits
import dev.frozenmilk.util.units.position.Vector2D
import org.firstinspires.ftc.teamcode.swerve.SwerveModule.Companion.Hardware.max_vel
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
        val angular = (gamepad1.right_stick_x).toDouble().rad

        telemetry.addData("vel", velocity.into(DistanceUnits.INCH))
        telemetry.addData("ang", angular)
        telemetry.addData("max vel", max_vel)

        telemetry.addData("angle position", swerve.fr.encoder.position)

        telemetry.addData("motor power", swerve.fr.motor.power)

        telemetry.addData("motor target", swerve.fr.drive.target)

        telemetry.addData("fr output", swerve.output)

        telemetry.addData("motor out ", swerve.fr.drive.output)
        telemetry.addData("motor out power", swerve.fr.drive.outputPower)

        swerve.update(velocity, angular)
    }
}