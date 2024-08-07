package org.firstinspires.ftc.teamcode.intothedeep.swerve

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot.*
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.intothedeep.util.controllers.AngularPDController
import org.firstinspires.ftc.teamcode.intothedeep.util.Vector2D
import org.firstinspires.ftc.teamcode.intothedeep.util.controllers.HeadingPDController
import kotlin.math.min
import kotlin.math.sign

class SwerveDrive(hardwareMap: HardwareMap) {
	val headingController = HeadingPDController(kPHeading, kDHeading)

	private val imu by lazy {
		val it = hardwareMap["imu"] as IMU

		val parameters = IMU.Parameters(
			RevHubOrientationOnRobot(
				LogoFacingDirection.DOWN,
				UsbFacingDirection.FORWARD
			)
		)

		it.initialize(parameters)
		it.resetYaw()

		it
	}


	var targetHeading = 0.0

	private val frontLeft by lazy {
		SwerveModule(
			"front left drive",
			"front left steer",
			"front left encoder",
			1.0,
			hardwareMap,
			steerDirection = REVERSE
		)
	}

	private val frontRight by lazy {
		SwerveModule(
			"front right drive",
			"front right steer",
			"front right encoder",
			3.0,
			hardwareMap,
			steerDirection = REVERSE
		)
	}

	private val backLeft by lazy {
		SwerveModule(
			"back left drive",
			"back left steer",
			"back left encoder",
			-4.0,
			hardwareMap,
			steerDirection = REVERSE
		)
	}

	private val backRight by lazy {
		SwerveModule(
			"back right drive",
			"back right steer",
			"back right encoder",
			-20.0,
			hardwareMap,
			steerDirection = REVERSE
		)
	}

	fun initialize() = modules.forEach { it.initialize() }

	fun heading() = -imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES)

	fun updateTargetHeading(targetHeading: Double) { this.targetHeading = targetHeading }

	fun adjustHeading() = headingController.run(heading(), targetHeading)

	fun resetHeading() { imu.resetYaw() }

	val modules by lazy { listOf(frontLeft, frontRight, backLeft, backRight) }

	fun command(V: Vector2D) {
		val omega = adjustHeading().let {
			val sign = it.sign

			min(it, 0.03) * sign
		}

		val u = (omega * halved)

		val A = V.x - u
		val B = V.x + u
		val C = V.y - u
		val D = V.y + u

		// ! TODO: figure out why

//		val fR = Vector2D(B, C)
//		val fL = Vector2D(B, D)
//		val bR = Vector2D(A, C)
//		val bL = Vector2D(A, D)

		val fR = Vector2D(B, C)
		val bR = Vector2D(B, D)
		val fL = Vector2D(A, C)
		val bL = Vector2D(A, D)

		val commands = listOf(fL, fR, bL, bR)

		modules.zip(commands).forEach { (module, command) -> module.update(command) }
	}

	fun encoders() = modules.map { it.read() }

	companion object {
		const val track_width = 8.386 // inches
		const val halved = track_width / 2.0
		const val kPHeading = 0.0016
		const val kDHeading = 0.0
	}
}