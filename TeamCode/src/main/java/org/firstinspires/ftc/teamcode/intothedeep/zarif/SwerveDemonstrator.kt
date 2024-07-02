package org.firstinspires.ftc.teamcode.intothedeep.zarif

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.frozenmilk.dairy.calcified.Calcified
import dev.frozenmilk.dairy.core.FeatureRegistrar
import dev.frozenmilk.dairy.core.util.OpModeLazyCell
import dev.frozenmilk.util.units.angle.rad
import dev.frozenmilk.util.units.distance.feet

import org.firstinspires.ftc.teamcode.intothedeep.zarif.SwerveModule.Location.*
import kotlin.math.atan2
import kotlin.math.hypot


@TeleOp
@Calcified.Attach(automatedCacheHandling = true)
class SwerveDemonstrator : OpMode() {
	val frontLeft by lazy { SwerveModule(FRONT_LEFT)  }
	val frontRight by lazy { SwerveModule(FRONT_RIGHT) }
	val backLeft by lazy { SwerveModule(BACK_LEFT) }
	val backRight by lazy { SwerveModule(BACK_RIGHT) }

	val modules by lazy { listOf(frontLeft, frontRight, backLeft, backRight) }

	init { FeatureRegistrar.checkFeatures(this, Calcified) }

	override fun init() { modules }

	override fun loop() {
		val x = gamepad1.left_stick_x.toDouble()
		val y = gamepad1.left_stick_y.toDouble()

		val theta = atan2(y, x).rad
		val magnitude = hypot(x, y)

		modules.forEach { it.set(5.feet * magnitude, theta) }
		modules.forEach { telemetry.addLine(it.telemetry()) }

		telemetry.update()
	}
}