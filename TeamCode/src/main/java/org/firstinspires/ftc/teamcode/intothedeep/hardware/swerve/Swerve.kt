package org.firstinspires.ftc.teamcode.intothedeep.hardware.swerve

import dev.frozenmilk.util.units.distance.feet
import org.firstinspires.ftc.teamcode.intothedeep.hardware.swerve.SwerveModule.Location

class Swerve {
    val fr = SwerveModule(Location.FRONT_RIGHT)
    val fl = SwerveModule(Location.FRONT_LEFT)
    val br = SwerveModule(Location.BACK_RIGHT)
    val bl = SwerveModule(Location.BACK_LEFT)

    val modules = listOf(fr, fl, br, bl)

    companion object {
        val maxVel = 7.feet
    }
}