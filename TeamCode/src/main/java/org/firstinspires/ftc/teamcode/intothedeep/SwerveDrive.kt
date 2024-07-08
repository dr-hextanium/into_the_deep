package org.firstinspires.ftc.teamcode.intothedeep

import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

class SwerveDrive(private val hardwareMap: HardwareMap) {
	private val frontLeft by lazy {
		SwerveModule(
			"front left drive",
			DcMotorSimple.Direction.FORWARD,
			"front left steer",
			DcMotorSimple.Direction.FORWARD,
			"front left encoder",
			hardwareMap
		)
	}
	private val frontRight by lazy {
		SwerveModule(
			"front right drive",
			DcMotorSimple.Direction.FORWARD,
			"front right steer",
			DcMotorSimple.Direction.FORWARD,
			"front right encoder",
			hardwareMap
		)
	}
	private val backLeft by lazy {
		SwerveModule(
			"back left drive",
			DcMotorSimple.Direction.FORWARD,
			"back left steer",
			DcMotorSimple.Direction.FORWARD,
			"back left encoder",
			hardwareMap
		)
	}
	private val backRight by lazy {
		SwerveModule(
			"back right drive",
			DcMotorSimple.Direction.FORWARD,
			"back right steer",
			DcMotorSimple.Direction.FORWARD,
			"back right encoder",
			hardwareMap
		)
	}

	fun readEncoders(): List<Double> = listOf(
		frontLeft.readEncoder(),
		frontRight.readEncoder(),
		backLeft.readEncoder(),
		backRight.readEncoder()
	)

	companion object {
		const val track_width = 8.386
		const val halved = track_width / 2.0
	}
}