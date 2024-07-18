package org.firstinspires.ftc.teamcode.intothedeep.common.util

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.normalizeDegrees


// [0, 2π)
fun norm(angle: Double) = normDelta(angle) + 180.0

// [-π, π)
fun normDelta(angle: Double) = normalizeDegrees(angle)
