package org.firstinspires.ftc.teamcode.intothedeep.hardware.swerve

import dev.frozenmilk.util.units.angle.Angle
import dev.frozenmilk.util.units.distance.Distance
import dev.frozenmilk.util.units.distance.feet
import dev.frozenmilk.util.units.distance.meters
import dev.frozenmilk.util.units.position.Vector2D
import org.firstinspires.ftc.teamcode.intothedeep.hardware.swerve.SwerveModule.Location
import java.util.function.Supplier

class Swerve {
    val states: Supplier<List<Distance>> = Supplier { modules.map { it.raw.output } }

    val fr = SwerveModule(states, Location.FRONT_RIGHT)
    val fl = SwerveModule(states, Location.BACK_RIGHT)
    val br = SwerveModule(states, Location.FRONT_LEFT)
    val bl = SwerveModule(states, Location.BACK_LEFT)

    val modules = listOf(fr, fl, br, bl)

    fun update(velocity: Vector2D, rotation: Angle) {
        val omega = rotation.intoRadians().value

        val L = (WHEEL_BASE * omega / 2.0)
        val W = (TRACK_WIDTH * omega / 2.0)

        val a = velocity.x - L
        val b = velocity.x + L
        val c = velocity.y - W
        val d = velocity.y + W

        fr.target(b, c)
        fl.target(b, d)
        bl.target(a, d)
        br.target(a, c)

        modules.forEach { it.update() }
    }

    fun telemetry(): String {
        return modules.joinToString("\n") { it.telemetry() }
    }

    companion object {
        val maxVel = 7.feet

        val WHEEL_BASE = 0.213.meters // L
        val TRACK_WIDTH = 0.213.meters // W

        val R = (WHEEL_BASE.pow(2) + TRACK_WIDTH.pow(2)).sqrt() / 2.0
    }
}