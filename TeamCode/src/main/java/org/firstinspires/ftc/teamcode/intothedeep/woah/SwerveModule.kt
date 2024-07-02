package org.firstinspires.ftc.teamcode.intothedeep.woah

import dev.frozenmilk.dairy.calcified.Calcified
import dev.frozenmilk.dairy.calcified.hardware.motor.Direction
import dev.frozenmilk.dairy.calcified.hardware.motor.ZeroPowerBehaviour
import dev.frozenmilk.dairy.core.util.OpModeLazyCell
import dev.frozenmilk.util.units.angle.Angle
import org.firstinspires.ftc.teamcode.intothedeep.old.old_util.AngularAnalogEncoder

enum class SwerveModuleConfiguration(val motorPort: Int, val servoPort: Int, val encoderPort: Int) {
	FRONT_LEFT(2, 2, 2),
	FRONT_RIGHT(0, 0, 0),
	BACK_LEFT(3, 3, 3),
	BACK_RIGHT(1, 1, 1)
}

class SwerveModule(
	private val config: SwerveModuleConfiguration,
	private val motorDirection: Direction = Direction.FORWARD,
	private val servoDirection: Direction = Direction.FORWARD
) {
    var motorPower = 0.0
    var servoPower = 0.0

    private val drivePort = config.motorPort
    private val steerPort = config.servoPort
    private val servoEncoderPort = config.encoderPort

    val steer by lazy { Calcified.controlHub.getContinuousServo(steerPort) }
    val drive by lazy { Calcified.controlHub.getMotor(drivePort) }
    val encoder by lazy { AngularAnalogEncoder(servoEncoderPort) }

    fun initialize() {
        drive.zeroPowerBehaviour = ZeroPowerBehaviour.BRAKE
        drive.direction = motorDirection
        drive.enabled = true

		encoder

		steer.direction = servoDirection
		steer.cachingTolerance = 0.02
    }

    fun readEncoder(): Angle {
		steer.power = 0.01

        return encoder.get()
    }
}