package org.firstinspires.ftc.teamcode.swerve

import dev.frozenmilk.dairy.calcified.Calcified
import dev.frozenmilk.dairy.calcified.hardware.controller.ComplexController
import dev.frozenmilk.dairy.calcified.hardware.controller.ControllerCompiler
import dev.frozenmilk.dairy.calcified.hardware.controller.calculation.DoubleDComponent
import dev.frozenmilk.dairy.calcified.hardware.controller.calculation.DoubleIComponent
import dev.frozenmilk.dairy.calcified.hardware.controller.calculation.DoublePComponent
import dev.frozenmilk.dairy.calcified.hardware.controller.calculation.UnitDComponent
import dev.frozenmilk.dairy.calcified.hardware.controller.calculation.UnitPComponent
import dev.frozenmilk.dairy.calcified.hardware.controller.compiler.DoubleControllerCompiler
import dev.frozenmilk.dairy.calcified.hardware.controller.compiler.UnitControllerCompiler
import dev.frozenmilk.dairy.calcified.hardware.controller.implementation.DoubleController
import dev.frozenmilk.dairy.calcified.hardware.motor.CalcifiedMotor
import dev.frozenmilk.dairy.calcified.hardware.motor.ZeroPowerBehaviour
import dev.frozenmilk.dairy.calcified.hardware.pwm.CalcifiedContinuousServo
import dev.frozenmilk.dairy.core.util.supplier.numeric.EnhancedUnitSupplier
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponents
import dev.frozenmilk.util.units.angle.Angle
import dev.frozenmilk.util.units.angle.AngleUnit
import dev.frozenmilk.util.units.angle.deg
import dev.frozenmilk.util.units.angle.wrappedDeg
import dev.frozenmilk.util.units.distance.Distance
import dev.frozenmilk.util.units.distance.DistanceUnit
import dev.frozenmilk.util.units.distance.DistanceUnits
import dev.frozenmilk.util.units.distance.feet
import dev.frozenmilk.util.units.distance.inches
import dev.frozenmilk.util.units.distance.meters
import dev.frozenmilk.util.units.distance.mm
import dev.frozenmilk.util.units.position.Vector2D
import org.firstinspires.ftc.teamcode.swerve.SwerveModule.Companion.Hardware.max_vel
import org.firstinspires.ftc.teamcode.swerve.SwerveModule.Companion.Hardware.ticks_per_inch
import kotlin.math.PI

class SwerveModule(port: Int) {
    val encoder: EnhancedUnitSupplier<AngleUnit, Angle>

    val motor: CalcifiedMotor
    val servo: CalcifiedContinuousServo

    val driveCompiler: ControllerCompiler<Distance>
    val turnCompiler: ControllerCompiler<Angle>

    val drive: ComplexController<Distance>
    val turn: ComplexController<Angle>

    var target = Vector2D()

    init {
        motor = Calcified.controlHub.getMotor(port)

        motor.zeroPowerBehaviour = ZeroPowerBehaviour.BRAKE

        servo = Calcified.controlHub.getContinuousServo(port)

        encoder = Calcified.controlHub.getAnalogInput(port)
                .let { EnhancedUnitSupplier({ (360.0 * (it.position / 3.3)).wrappedDeg }) }

        driveCompiler = UnitControllerCompiler<DistanceUnit, Distance>()
                .add(motor)
                .withSupplier(Calcified.controlHub.getDistanceEncoder(port, DistanceUnits.INCH, 145.1 / 1.0))
                .append(UnitPComponent(DrivePID.P))
                .append(UnitDComponent(DrivePID.D))

        turnCompiler = UnitControllerCompiler<AngleUnit, Angle>()
                .add(servo)
                .withSupplier(encoder)
                .append(UnitPComponent(TurnPID.P))
                .append(UnitDComponent(TurnPID.D))

        drive = driveCompiler.compile({ this.target.magnitude.intoInches() }, MotionComponents.VELOCITY, 0.mm)
        turn = turnCompiler.compile({ this.target.theta }, MotionComponents.VELOCITY, 0.deg)
    }

    fun update(target: Vector2D) {
        this.target = target

        drive.update()
        turn.update()
    }

    fun write() { }

    companion object {
        object Hardware {
            object Drive {
                const val input = 6000.0 / 60.0

                const val pulley = 1.0 / 2.0
                const val spur = 20.0 / 30.0
                const val bevel = 18.0 / 52.0

                const val ratio = pulley * spur * bevel

                const val output = input * ratio
            }

            object Wheel {
                val diameter = 64.mm
                val radius = diameter / 2.0
                val circumference = diameter * PI
            }

            val max_vel = (Wheel.circumference * Drive.output).intoInches()

            val ticks_per_inch = Wheel.circumference.intoInches().pow(-1) * (27.0 * Drive.ratio)
        }

        object DrivePID {
            const val P = 0.0004
            const val D = 0.0
            const val S = 0.0
        }

        object TurnPID {
            const val P = 0.001
            const val D = 0.0
            const val S = 0.0
        }
    }
}