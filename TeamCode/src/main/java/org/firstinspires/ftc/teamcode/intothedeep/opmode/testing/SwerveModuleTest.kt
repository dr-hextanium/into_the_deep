package org.firstinspires.ftc.teamcode.intothedeep.opmode.testing

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.frozenmilk.dairy.calcified.Calcified
import dev.frozenmilk.util.units.angle.wrappedRad
import dev.frozenmilk.util.units.distance.inches
import dev.frozenmilk.util.units.distance.mm
import dev.frozenmilk.util.units.position.Vector2D
import org.firstinspires.ftc.teamcode.intothedeep.hardware.old_swerve.OldSwerveModule
import kotlin.math.atan2

@TeleOp(group = "Testing")
@Calcified.Attach
class SwerveModuleTest : OpMode() {
    val module by lazy { OldSwerveModule({ listOf(0.inches) }, location) }

    override fun init() { module }

    override fun loop() {
        val theta = atan2(gamepad1.left_stick_x, -gamepad1.left_stick_y).toDouble()

        module.target(Vector2D(0.003.mm, theta.wrappedRad))
        module.update()
    }

    companion object {
        val location = OldSwerveModule.Location.BACK_RIGHT
    }
}