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

	hardwareMap: HardwareMap,

	private val driveDirection: Direction = Direction.FORWARD,
	private val steerDirection: Direction = Direction.FORWARD
) {
	private val drive by lazy { hardwareMap[driveName] as DcMotor }
	private val steer by lazy { hardwareMap[steerName] as CRServo }
	private val encoder by lazy { hardwareMap[encoderName] as AnalogInput }

	private val controller = PDController(kP, kD)

	fun update(command: Vector2D) {
		drive.power = command.magnitude

		steer.power = controller.run(read(), command.theta)
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

	companion object {
		const val kP = 0.006
		const val kD = 0.3
	}
}