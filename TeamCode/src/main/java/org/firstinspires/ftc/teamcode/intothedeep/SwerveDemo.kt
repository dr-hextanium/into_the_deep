package org.firstinspires.ftc.teamcode.intothedeep

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlin.math.PI
import kotlin.math.atan2

@TeleOp
class SwerveDemo : OpMode() {
	private val drive = SwerveDrive(hardwareMap)

	override fun init() { drive.initialize() }

	override fun loop() {
		val x = gamepad1.left_stick_x.toDouble()
		val y = gamepad1.left_stick_y.toDouble()

		val target = atan2(y, x) * (180.0 / PI)

		drive.modules.forEach {
			telemetry.addData(
				"${it.steerName} rotation (degrees)",
				it.read()
			)
		}

		drive.modules.forEach { it.update(target) }
		telemetry.addData("target", target)
	}
}