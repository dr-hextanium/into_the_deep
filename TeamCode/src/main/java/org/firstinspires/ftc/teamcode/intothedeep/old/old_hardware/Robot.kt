package org.firstinspires.ftc.teamcode.intothedeep.old.old_hardware

object Robot {
    object Drive {
        open class SwerveModuleConfiguration(
            val motor: Int,
            val servo: Int,
            val motorEncoder: Int,
            val servoEncoder: Int
        )

        val frontRight = SwerveModuleConfiguration(0, 0, 0, 0)
        val backRight = SwerveModuleConfiguration(1, 1, 1, 1)
        val frontLeft = SwerveModuleConfiguration(2, 2, 2, 2)
        val backLeft = SwerveModuleConfiguration(3, 3, 3, 3)
    }
}