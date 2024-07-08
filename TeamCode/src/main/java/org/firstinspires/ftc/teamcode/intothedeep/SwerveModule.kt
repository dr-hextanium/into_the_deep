package org.firstinspires.ftc.teamcode.intothedeep

import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.HardwareMap
import kotlin.math.atan2
import kotlin.math.floor
import kotlin.math.hypot


class SwerveModule(
	val driveName: String,
	val steerName: String,
	val encoderName: String,

	hardwareMap: HardwareMap,

	private val driveDirection: Direction = Direction.FORWARD,
	private val steerDirection: Direction = Direction.FORWARD
) {
	private val drive by lazy { hardwareMap[driveName] as DcMotor }
	private val steer by lazy { hardwareMap[steerName] as CRServo }
	private val encoder by lazy { hardwareMap[encoderName] as AnalogInput }

	private val controller = PDController(kP, kD)
	private var target = 0.0


	fun update(newTarget: Double) {
		target = newTarget
		controller.updateTarget(target)

		val power = controller.run(read())
		steer.power = power
	}

	fun initialize() {
		drive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		drive.direction = driveDirection

		steer.direction = steerDirection

		encoder
	}

	fun read(): Double {
		return ((encoder.voltage / 3.3) * 360.0) - 180.0
	}

	fun command(V: Vector2D, omega: Double) {
		val A = V.x - (omega * SwerveDrive.halved)
		val B = V.x + (omega * SwerveDrive.halved)
		val C = V.y - (omega * SwerveDrive.halved)
		val D = V.y + (omega * SwerveDrive.halved)

		val fR = Vector2D(B, C)
		val fL = Vector2D(B, D)
		val bR = Vector2D(A, C)
		val bL = Vector2D(A, D)
	}

	companion object {
		const val kP = 0.006
		const val kD = 0.3
	}
}