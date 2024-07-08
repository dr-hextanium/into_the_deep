package org.firstinspires.ftc.teamcode.intothedeep.swerve

import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.intothedeep.util.Vector2D

class SwerveDrive(hardwareMap: HardwareMap) {
	private val frontLeft by lazy {
		SwerveModule(
			"front left drive",
			"front left steer",
			"front left encoder",
			hardwareMap,
			steerDirection = REVERSE
		)
	}

	private val frontRight by lazy {
		SwerveModule(
			"front right drive",
			"front right steer",
			"front right encoder",
			hardwareMap,
			steerDirection = REVERSE
		)
	}

	private val backLeft by lazy {
		SwerveModule(
			"back left drive",
			"back left steer",
			"back left encoder",
			hardwareMap,
			steerDirection = REVERSE
		)
	}

	private val backRight by lazy {
		SwerveModule(
			"back right drive",
			"back right steer",
			"back right encoder",
			hardwareMap,
			steerDirection = REVERSE
		)
	}

	fun initialize() = modules.forEach { it.initialize() }

	val modules by lazy { listOf(frontLeft, frontRight, backLeft, backRight) }

	fun command(V: Vector2D, omega: Double) {
		val A = V.x - (omega * halved)
		val B = V.x + (omega * halved)
		val C = V.y - (omega * halved)
		val D = V.y + (omega * halved)

		val fR = Vector2D(B, C)
		val fL = Vector2D(B, D)
		val bR = Vector2D(A, C)
		val bL = Vector2D(A, D)

		val commands = listOf(fL, fR, bL, bR)

		modules.zip(commands).forEach { (module, command) -> module.update(command) }
	}

	fun encoders() = modules.map { it.read() }

	companion object {
		const val track_width = 8.386 // inches
		const val halved = track_width / 2.0
	}
}