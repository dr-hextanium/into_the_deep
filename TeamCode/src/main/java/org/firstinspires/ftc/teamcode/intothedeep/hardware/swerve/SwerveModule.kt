package org.firstinspires.ftc.teamcode.intothedeep.hardware.swerve

import dev.frozenmilk.dairy.calcified.Calcified
import dev.frozenmilk.dairy.calcified.hardware.controller.ComplexController
import dev.frozenmilk.dairy.calcified.hardware.controller.calculation.UnitDComponent
import dev.frozenmilk.dairy.calcified.hardware.controller.calculation.UnitPComponent
import dev.frozenmilk.dairy.calcified.hardware.controller.compiler.UnitControllerCompiler
import dev.frozenmilk.dairy.calcified.hardware.motor.CalcifiedMotor
import dev.frozenmilk.dairy.calcified.hardware.motor.Direction
import dev.frozenmilk.dairy.calcified.hardware.pwm.CalcifiedContinuousServo
import dev.frozenmilk.dairy.core.util.supplier.numeric.EnhancedUnitSupplier
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponents
import dev.frozenmilk.util.units.angle.Angle
import dev.frozenmilk.util.units.angle.AngleUnit
import dev.frozenmilk.util.units.angle.deg
import dev.frozenmilk.util.units.distance.Distance
import dev.frozenmilk.util.units.distance.DistanceUnit
import dev.frozenmilk.util.units.distance.DistanceUnits
import dev.frozenmilk.util.units.distance.inches
import dev.frozenmilk.util.units.position.Vector2D
import org.firstinspires.ftc.teamcode.intothedeep.hardware.Robot
import org.firstinspires.ftc.teamcode.intothedeep.util.AngularAnalogEncoder
import org.firstinspires.ftc.teamcode.intothedeep.util.UnitNormalizationComponent
import java.util.function.Supplier
import kotlin.math.sign

class SwerveModule(states: Supplier<List<Distance>>, location: Location) {
    val encoder: AngularAnalogEncoder
    val servo: CalcifiedContinuousServo
    val motor: CalcifiedMotor

    val drive: ComplexController<Distance>
    val turn: ComplexController<Angle>

    var target = Vector2D(0.inches, 0.deg)

    val error
        get() = target.theta.findError(encoder.get())

    init {
        encoder = AngularAnalogEncoder(location.ports.servoEncoder).zero(location.offset)
        servo = Calcified.controlHub.getContinuousServo(location.ports.servo)
        motor = Calcified.controlHub.getMotor(location.ports.motor)

        servo.direction = Direction.REVERSE

        val motorEncoder = Calcified.controlHub.getDistanceEncoder(
            location.ports.motorEncoder,
            DistanceUnits.INCH,
            145.1 / 1.0
        )

        val wrapper = EnhancedUnitSupplier({ encoder.get() })

        val driveCompiler = UnitControllerCompiler<DistanceUnit, Distance>()
            .add(motor)
            .withSupplier(motorEncoder)
            .append(UnitPComponent(DrivePID.P))
            .append(UnitDComponent(DrivePID.D))
            .append(UnitNormalizationComponent(states))

        val turnCompiler = UnitControllerCompiler<AngleUnit, Angle>()
            .add(servo)
            .withSupplier(wrapper)
            .append(UnitPComponent(TurnPID.P))
            .append(UnitDComponent(TurnPID.D))

        drive = driveCompiler.compile(0.inches, MotionComponents.VELOCITY, 0.inches)
        turn = turnCompiler.compile(0.deg, MotionComponents.POSITION, (0.25).deg)
    }

    fun target(x: Distance, y: Distance) { this.target = Vector2D(x, y) }
    fun target(target: Vector2D) { this.target = target }

    fun update() {
        val speed = target.magnitude.intoInches()
        val angle = target.theta.intoDegrees()

        drive.target = speed * angle.value.sign
        turn.target = angle

        drive.update()
        turn.update()
    }

    enum class Location(
        val ports: Robot.Drive.SwerveModuleConfiguration,
        val offset: Angle
    ) {
        FRONT_RIGHT(Robot.Drive.frontRight, 0.deg),
        FRONT_LEFT(Robot.Drive.frontLeft, 0.deg),
        BACK_RIGHT(Robot.Drive.backRight, 0.deg),
        BACK_LEFT(Robot.Drive.backLeft, 0.deg),
    }

    companion object {
        object DrivePID {
            const val P = 0.0005
            const val D = 0.0
            const val S = 0.0
        }

        object TurnPID {
            const val P = 0.4
            const val D = 0.0
            const val S = 0.0
        }
    }
}