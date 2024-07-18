package org.firstinspires.ftc.teamcode.intothedeep.hardware.swerve

import dev.frozenmilk.util.units.angle.deg
import dev.frozenmilk.util.units.distance.inches
import dev.frozenmilk.util.units.position.Pose2D
import dev.frozenmilk.util.units.position.Vector2D


class Swerve {
    val modules by lazy { listOf(fr, fl, br, bl) }

    val fr = SwerveModule()
    val fl = SwerveModule()
    val br = SwerveModule()
    val bl = SwerveModule()

    val offsets = mapOf(
        fr to 0.deg,
        fl to 0.deg,
        br to 0.deg,
        bl to 0.deg
    )

    val powers = mapOf(
        fr to 0.deg,
        fl to 0.deg,
        br to 0.deg,
        bl to 0.deg
    )

    var locked = false

    fun read() {

    }

    fun write() {

    }

    fun set(pose: Pose2D) {
        val (x, y, head) = Triple(pose.vector2D.x, pose.vector2D.y, pose.heading.intoRadians().value)

        val n = (WHEEL_BASE / R) * head
        val m = (TRACK_WIDTH / R) * head

        val a = x - n
        val b = x + n
        val c = y - m
        val d = y + m

        if (locked) {

        } else {

        }
    }

    fun update() {

    }

    companion object {
        val TRACK_WIDTH = 8.385.inches
        val WHEEL_BASE = 8.385.inches

        val measurements = Vector2D(TRACK_WIDTH, WHEEL_BASE)

        val R = measurements.magnitude
    }
}