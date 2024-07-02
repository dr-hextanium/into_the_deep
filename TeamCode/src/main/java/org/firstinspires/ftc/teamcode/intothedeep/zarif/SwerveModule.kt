package org.firstinspires.ftc.teamcode.intothedeep.zarif

import dev.frozenmilk.dairy.calcified.Calcified
import dev.frozenmilk.dairy.calcified.hardware.controller.ComplexController
import dev.frozenmilk.dairy.calcified.hardware.controller.calculation.UnitDComponent
import dev.frozenmilk.dairy.calcified.hardware.controller.calculation.UnitPComponent
import dev.frozenmilk.dairy.calcified.hardware.controller.compiler.DoubleControllerCompiler
import dev.frozenmilk.dairy.calcified.hardware.controller.compiler.UnitControllerCompiler
import dev.frozenmilk.dairy.calcified.hardware.controller.implementation.DoubleController
import dev.frozenmilk.dairy.calcified.hardware.motor.CalcifiedMotor
import dev.frozenmilk.dairy.calcified.hardware.pwm.CalcifiedContinuousServo
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponents
import dev.frozenmilk.dairy.core.util.supplier.numeric.unit.EnhancedUnitSupplier
import dev.frozenmilk.util.units.angle.Angle
import dev.frozenmilk.util.units.angle.AngleUnit
import dev.frozenmilk.util.units.angle.deg
import dev.frozenmilk.util.units.distance.Distance
import dev.frozenmilk.util.units.distance.feet
import dev.frozenmilk.util.units.distance.inches
import dev.frozenmilk.util.units.position.Vector2D
import org.firstinspires.ftc.teamcode.intothedeep.old.old_util.AngularAnalogEncoder

class SwerveModule(location: Location) {
	val encoder: AngularAnalogEncoder
	val servo: CalcifiedContinuousServo
	val motor: CalcifiedMotor

	val steer: ComplexController<Angle>

	var target = Vector2D(0.inches, 0.inches)

	var flipped = false

	val error
		get() = target.theta.findError(encoder.get())

	init {
		encoder = AngularAnalogEncoder(location.analog).zero(location.offset)
		servo = Calcified.controlHub.getContinuousServo(location.servo)
		motor = Calcified.controlHub.getMotor(location.motor)

		val wrapper = EnhancedUnitSupplier({ encoder.get() })

		val steerCompiler = UnitControllerCompiler<AngleUnit, Angle>()
			.add(servo)
			.withSupplier(wrapper)
			.append(UnitPComponent(P))
			.append(UnitDComponent(D))

		steer = steerCompiler.compile(location.offset, MotionComponents.POSITION, (0.25).deg)
	}

	fun set(x: Distance, y: Distance) = set(Vector2D(x, y))
	fun set(magnitude: Distance, theta: Angle) = set(Vector2D(magnitude, theta))

	fun set(state: Vector2D) { target = state }

	fun write() {
		steer.update()
		motor.power = target.normalise(5.feet).magnitude.value
	}

	fun telemetry(): String {
		return "target: $target, flipped: $flipped, error: ${error.intoDegrees()}, points: ${encoder.get().intoDegrees()}"
	}

	// TODO find these port values
	enum class Location(val motor: Int, val servo: Int, val analog: Int, val offset: Angle) {
		FRONT_RIGHT(0, 0, 0, 0.deg),
		FRONT_LEFT(2, 2, 2, 0.deg),
		BACK_RIGHT(1, 1, 1, 0.deg),
		BACK_LEFT(3, 3, 3, 0.deg),
	}

	companion object {
		const val P = 0.1
		const val I = 0.00
		const val D = 0.00
	}
}