package org.firstinspires.ftc.teamcode.intothedeep

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.intothedeep.swerve.SwerveDrive
import org.firstinspires.ftc.teamcode.intothedeep.util.Vector2D
import kotlin.math.PI
import kotlin.math.atan2

@TeleOp
class SwerveDemo : OpMode() {
	private val drive by lazy { SwerveDrive(hardwareMap) }

	override fun init() { drive.initialize() }

	override fun loop() {
		val x = gamepad1.left_stick_x.toDouble()
		val y = gamepad1.left_stick_y.toDouble()

		drive.modules.forEach {
			telemetry.addData(
				"${it.steerName} rotation (degrees)",
				it.read()
			)
		}

		val omega = (gamepad1.right_trigger - gamepad1.left_trigger).toDouble()

		drive.command(Vector2D(x, y).rotate90(), omega)
	}
}