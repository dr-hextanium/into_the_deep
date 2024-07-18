package org.firstinspires.ftc.teamcode.intothedeep.common

import com.acmerobotics.dashboard.config.Config
import kotlin.math.hypot
import kotlin.math.sqrt

@Config
object Globals {
    @JvmField
    var USE_FEEDFORWARD = false

    @JvmField
    var USE_HEADING_PID = false

    const val WHEEL_BASE = 8.386 // inches

    val R = hypot(WHEEL_BASE, WHEEL_BASE)
}