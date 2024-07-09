package org.firstinspires.ftc.teamcode.intothedeep

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.teamcode.intothedeep.swerve.SwerveDrive
import org.firstinspires.ftc.teamcode.intothedeep.util.Vector2D
import kotlin.math.PI
import kotlin.math.atan2

@TeleOp
class SwerveDemo : OpMode() {
	private val drive by lazy { SwerveDrive(hardwareMap) }

	override fun init() { drive.initialize() }

	override fun loop() {
//		val x = gamepad1.left_stick_x.toDouble()
//		val y = gamepad1.left_stick_y.toDouble()

		val x = 0.0
		val y = 0.0

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

		val theta = 0.0 // atan2(y2, x2) * (180/PI)

		val adjustedHeading = drive.adjustHeading(theta)
		drive.command(Vector2D(x, y).rotate90(), 0.1)

		telemetry.addData("heading", drive.heading())
		telemetry.addData("Adjusted Heading", adjustedHeading)
		telemetry.addData("target", theta)
		telemetry.addData("x, y", "${x}, $y")

		telemetry.addLine(drive.headingController.telem)
	}
}