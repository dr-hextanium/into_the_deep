package org.firstinspires.ftc.teamcode.swerve.hardware.swerve

import dev.frozenmilk.util.units.angle.deg
import dev.frozenmilk.util.units.angle.wrappedRad
import dev.frozenmilk.util.units.distance.inches
import dev.frozenmilk.util.units.distance.meters
import dev.frozenmilk.util.units.position.Vector2D
import org.firstinspires.ftc.teamcode.swerve.hardware.Robot
import org.firstinspires.ftc.teamcode.swerve.util.perp
import kotlin.math.atan2
import kotlin.math.hypot

class Swerve {
    val fl = SwerveModule(Robot.Drive.front_left)
    val fr = SwerveModule(Robot.Drive.front_right)
//    val bl = SwerveModule(Robot.Drive.back_left)
//    val br = SwerveModule(Robot.Drive.back_right)

    var output = Vector2D()

//    val modules = listOf(fr, fl, bl, br)
    val modules = listOf(fl)

    val offsets = mapOf(
        fr to Vector2D(offset, 45.deg),
        fl to Vector2D(offset, 135.deg),
//        bl to Vector2D(offset, 225.deg),
//        br to Vector2D(offset, 315.deg)
    )

    val perps = mapOf(
        fr to offsets[fr]!!.perp(),
        fl to offsets[fl]!!.perp(),
//        bl to offsets[bl]!!.perp(),
//        br to offsets[br]!!.perp()
    )

    fun update(velocity: Vector2D) {
        val angle = velocity.theta

        val omega = angle.intoRadians().value

        val u = halved * omega

        val a = (velocity.x - u).intoInches().value
        val b = (velocity.x + u).intoInches().value
        val c = (velocity.y - u).intoInches().value
        val d = (velocity.y + u).intoInches().value

//        fr.update(hypot(b, c).inches, atan2(b, c).wrappedRad)
        fl.update(hypot(b, d).inches, atan2(b, d).wrappedRad)
//        bl.update(hypot(a, d).inches, atan2(a, d).wrappedRad)
//        br.update(hypot(a, c).inches, atan2(a, c).wrappedRad)
    }

    companion object {
        val center = 0.213.meters
        val halved = center / 2.0
        val offset = (halved.pow(2) * 2.0).sqrt()
    }
}