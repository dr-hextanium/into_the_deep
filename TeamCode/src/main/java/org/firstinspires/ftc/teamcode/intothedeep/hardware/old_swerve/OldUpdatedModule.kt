package org.firstinspires.ftc.teamcode.intothedeep.hardware.old_swerve

import dev.frozenmilk.dairy.calcified.Calcified
import dev.frozenmilk.dairy.calcified.hardware.controller.ComplexController
import dev.frozenmilk.dairy.calcified.hardware.controller.compiler.Pose2DControllerCompiler
import dev.frozenmilk.dairy.calcified.hardware.controller.compiler.Vector2DControllerCompiler
import dev.frozenmilk.dairy.calcified.hardware.motor.CalcifiedMotor
import dev.frozenmilk.dairy.calcified.hardware.pwm.CalcifiedContinuousServo
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponents
import dev.frozenmilk.dairy.core.util.supplier.numeric.positional.EnhancedVector2DSupplier
import dev.frozenmilk.util.units.angle.Angle
import dev.frozenmilk.util.units.angle.deg
import dev.frozenmilk.util.units.distance.inches
import dev.frozenmilk.util.units.distance.mm
import dev.frozenmilk.util.units.position.Vector2D
import org.firstinspires.ftc.teamcode.intothedeep.hardware.Robot
import org.firstinspires.ftc.teamcode.intothedeep.util.AngularAnalogEncoder

class OldUpdatedModule(location: Location) {
    val encoder: AngularAnalogEncoder
    val servo: CalcifiedContinuousServo
    val motor: CalcifiedMotor

    val targeter: ComplexController<Vector2D>
    var target = Vector2D(0.inches, 0.deg)

    init {
        encoder = AngularAnalogEncoder(location.ports.servoEncoder).zero(location.offset)
        servo = Calcified.controlHub.getContinuousServo(location.ports.servo)
        motor = Calcified.controlHub.getMotor(location.ports.motor)

        Pose2DControllerCompiler()

        targeter = Vector2DControllerCompiler()
            .set({

            })
            .withSupplier(EnhancedVector2DSupplier({ target }))
            .compile(Vector2D(), MotionComponents.POSITION, Vector2D(1.mm, 1.mm))
    }

    fun target() {

    }

    fun update() {

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
}