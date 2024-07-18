package org.firstinspires.ftc.teamcode.intothedeep.common.hardware.swerve

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.intothedeep.common.Globals.R
import org.firstinspires.ftc.teamcode.intothedeep.common.Globals.USE_FEEDFORWARD
import org.firstinspires.ftc.teamcode.intothedeep.common.Globals.WHEEL_BASE
import org.firstinspires.ftc.teamcode.intothedeep.common.util.norm
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.sign

class SwerveDrive(hw: HardwareMap) {
    private val frontLeft = SwerveModule(hw, "frontLeft", Offsets.frontLeft)
    private val frontRight = SwerveModule(hw, "frontRight", Offsets.frontRight)
    private val backLeft = SwerveModule(hw, "backLeft", Offsets.backLeft)
    private val backRight = SwerveModule(hw, "backRight", Offsets.backRight)

    private val modules = listOf(frontLeft, frontRight, backRight, backLeft)

    private var ws = listOf<Double>()
    private var wa = listOf<Double>()

    private val minimum = 0.1
    private var offset = 0.0

    var locked = false

    fun initialize() = modules.forEach { it.initialize() }

    fun read() = modules.forEach { it.read() }

    fun set(x: Double, y: Double, omega: Double) {
        val u = (WHEEL_BASE / R)
        val wu = omega * u

        val a = x - omega * wu
        val b = x + omega * wu
        val c = y - omega * wu
        val d = y + omega * wu

        if (locked) {
            ws = listOf(0.0, 0.0, 0.0, 0.0)
            wa = listOf(Math.PI / 4, -Math.PI / 4, Math.PI / 4, -Math.PI / 4).map { it * 180.0 }
        } else {
            ws = listOf(hypot(b, c), hypot(b, d), hypot(a, d), hypot(a, c))
            wa = listOf(atan2(b, c), atan2(b, d), atan2(a, d), atan2(a, c)).map { it * 180.0 }
        }
    }

    fun write() {
        val max = ws.max()

        if (max > 1.0) ws = ws.map { it / max }

        modules.forEachIndexed { i, module -> module.run {
            writeMotor(abs(ws[i]) + if (USE_FEEDFORWARD) minimum * ws[i].sign else 0.0)
            target(norm(wa[i]))
        } }
    }

    fun updateModules() = modules.forEach { it.update() }
}

@Config
object Offsets {
    @JvmField
    var frontLeft = 0.0

    @JvmField
    var frontRight = 0.0

    @JvmField
    var backLeft = 0.0

    @JvmField
    var backRight = 0.0
}