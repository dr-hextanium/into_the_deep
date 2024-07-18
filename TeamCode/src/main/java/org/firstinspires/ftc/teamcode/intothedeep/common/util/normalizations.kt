package org.firstinspires.ftc.teamcode.intothedeep.common.util


// [0, 2π)
fun norm(angle: Double) = angle % 360

// [-π, π)
fun normDelta(angle: Double) = (angle + 180.0) % 360 - 180.0
