package org.firstinspires.ftc.teamcode.swerve

import dev.frozenmilk.util.units.angle.Angle
import dev.frozenmilk.util.units.angle.deg
import dev.frozenmilk.util.units.distance.meters
import dev.frozenmilk.util.units.position.Vector2D

class Swerve {
//    val fl = SwerveModule(Robot.Drive.front_left)
    val fr = SwerveModule(Robot.Drive.front_right)
    var output = Vector2D()
//    val bl = SwerveModule(Robot.Drive.back_left)
//    val br = SwerveModule(Robot.Drive.back_right)

//    val modules = listOf(fr, fl, bl, br)
    val modules = listOf(fr)

    val offsets = mapOf(
        fr to Vector2D(offset, 45.deg),
//        fl to Vector2D(offset, 135.deg),
//        bl to Vector2D(offset, 225.deg),
//        br to Vector2D(offset, 315.deg)
    )

    val perps = mapOf(
        fr to offsets[fr]!!.perp(),
//        fl to offsets[fl]!!.perp(),
//        bl to offsets[bl]!!.perp(),
//        br to offsets[br]!!.perp()
    )

    fun update(velocity: Vector2D, omega: Angle) {

        modules.forEach { output = velocity + (perps[it]!! * omega.intoRadians().value); it.update(output); }
    }

    companion object {
        val center = 0.213.meters
        val halved = center / 2.0
        val offset = (halved.pow(2) * 2.0).sqrt()
    }
}