package org.firstinspires.ftc.teamcode.intothedeep.woah

import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.HardwareMap
import kotlin.math.atan2
import kotlin.math.floor
import kotlin.math.hypot
import kotlin.math.pow
import kotlin.math.sqrt


class SwerveModuleTwo(
	val driveName: String,
	private val driveDirection: Direction,
	val steerName: String,
	private val steerDirection: Direction,
	val encoderName: String,
	private val hardwareMap: HardwareMap
) {
	private val drive by lazy { hardwareMap[driveName] as DcMotor }
	private val steer by lazy { hardwareMap[steerName] as CRServo }
	private val encoder by lazy { hardwareMap[encoderName] as AnalogInput }

	private var target = 0.0
	private val controller = PDController(kP, kD)

	fun telemetry() = controller.telemetry

	fun update(newTarget: Double) {
		target = newTarget
		controller.updateTarget(target)

		val power = controller.run(readEncoder())
		steer.power = power
	}

	fun initialize() {
		drive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		drive.direction = driveDirection
		drive.power = 0.0

		steer.power = 0.1
		steer.direction = Direction.REVERSE

		encoder
	}

	fun readEncoder(): Double {
		return floor((encoder.voltage / 3.3) * 360) - 180
	}

	fun command(V: Vector2D, omega: Double) {
		val A = V.x - (omega * SwerveDriveTwo.halved)
		val B = V.x + (omega * SwerveDriveTwo.halved)
		val C = V.y - (omega * SwerveDriveTwo.halved)
		val D = V.y + (omega * SwerveDriveTwo.halved)

		val fR = calculateModule(B, C)
		val fL = calculateModule(B, D)
		val bR = calculateModule(A, C)
		val bL = calculateModule(A, D)
	}

	private fun calculateModule(x: Double, y: Double): Command {
		val speed = hypot(x, y)
		val angle = atan2(x, y) * (180.0 / Math.PI)

		return Command(speed, angle)
	}

	companion object {
		const val kP = 0.006
		const val kD = 0.3
	}
}