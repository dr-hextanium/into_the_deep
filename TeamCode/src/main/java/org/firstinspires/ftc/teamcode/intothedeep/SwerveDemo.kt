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

		if (gamepad1.a) {
			drive.resetHeading()
		}

		drive.modules.forEach {
			telemetry.addData(
				"${it.steerName} rotation (degrees)",
				it.read()
			)
		}

		val x2 = gamepad1.right_stick_x.toDouble()
		val y2 = gamepad1.right_stick_y.toDouble()

		val theta = atan2(y2, x2) * (180.0 / PI)

		if(x2 != 0.0 && y2 != 0.0) drive.updateTargetHeading(theta)

		drive.command(Vector2D(x, y).rotate90())

		telemetry.addData("heading", drive.heading())
		telemetry.addData("target", drive.targetHeading)
		telemetry.addData("error", drive.headingController.telemetry)
		telemetry.addData("x, y", "$x, $y")

	}
}