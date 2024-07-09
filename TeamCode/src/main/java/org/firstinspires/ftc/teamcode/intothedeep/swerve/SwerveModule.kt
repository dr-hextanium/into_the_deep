package org.firstinspires.ftc.teamcode.intothedeep.swerve

import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.intothedeep.util.PDController
import org.firstinspires.ftc.teamcode.intothedeep.util.Vector2D


class SwerveModule(
	val driveName: String,
	val steerName: String,
	val encoderName: String,
	val offset: Double,

	hardwareMap: HardwareMap,

	private val driveDirection: Direction = Direction.FORWARD,
	private val steerDirection: Direction = Direction.FORWARD
) {
	private val drive by lazy { hardwareMap[driveName] as DcMotor }
	private val steer by lazy { hardwareMap[steerName] as CRServo }
	private val encoder by lazy { hardwareMap[encoderName] as AnalogInput }

	private val moduleController = PDController(kPModule, kDModule)

	fun update(command: Vector2D) {
		steer.power = moduleController.run(read(), command.theta)

		drive.power = command.magnitude
	}

	fun initialize() {
		drive.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
		drive.direction = driveDirection

		steer.direction = steerDirection

		encoder
	}

	// (-180, 180]
	fun read(): Double {
		val raw = (encoder.voltage / 3.3)
		val angle = raw * 360.0
		val offsetted = ((angle + offset) % 360)

		return offsetted - 180.0
	}

	companion object {
		const val kPModule = 0.001
		const val kDModule = 0.3
	}
}