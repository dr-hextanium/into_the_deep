package org.firstinspires.ftc.teamcode.intothedeep.woah

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorSimple
import kotlin.math.PI
import kotlin.math.atan2

@TeleOp
class SwerveDemoTwo : OpMode() {
	private val target = 0.0

	private val frontLeft by lazy {
		SwerveModuleTwo(
			"front left drive",
			DcMotorSimple.Direction.FORWARD,
			"front left steer",
			DcMotorSimple.Direction.FORWARD,
			"front left encoder",
			hardwareMap
		)
	}
	private val frontRight by lazy {
		SwerveModuleTwo(
			"front right drive",
			DcMotorSimple.Direction.FORWARD,
			"front right steer",
			DcMotorSimple.Direction.FORWARD,
			"front right encoder",
			hardwareMap
		)
	}
	private val backLeft by lazy {
		SwerveModuleTwo(
			"back left drive",
			DcMotorSimple.Direction.FORWARD,
			"back left steer",
			DcMotorSimple.Direction.FORWARD,
			"back left encoder",
			hardwareMap
		)
	}
	private val backRight by lazy {
		SwerveModuleTwo(
			"back right drive",
			DcMotorSimple.Direction.FORWARD,
			"back right steer",
			DcMotorSimple.Direction.FORWARD,
			"back right encoder",
			hardwareMap
		)
	}

	private val modules by lazy { listOf(frontLeft, frontRight, backLeft, backRight) }

	override fun init() {
		modules.forEach { it.initialize() }
	}

	override fun loop() {
		val x = gamepad1.left_stick_x.toDouble()
		val y = gamepad1.left_stick_y.toDouble()

		val target = atan2(y, x) * (180 / PI)

		modules.forEach { telemetry.addData("${it.steerName} rotation (degrees)", it.readEncoder()) }
		modules.forEach { it.update(target) }
		modules.forEach { telemetry.addData("controller telemetry", it.telemetry()) }
		telemetry.addData("target", target)
	}
}