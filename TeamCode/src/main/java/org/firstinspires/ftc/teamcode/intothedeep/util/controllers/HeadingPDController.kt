package org.firstinspires.ftc.teamcode.intothedeep.util.controllers

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit

class HeadingPDController(kP: Double, kD: Double) : Controller(kP, kD) {
	// current : [-180, 180], target : [-180, 180]
	// 60 - (-180) = 240
	override fun run(current: Double, target: Double): Double {
		val error = target - current


		val de = error - lastError
		lastError = error

		val p = (kP * error)
		val d = (kD * de / dt)

		telemetry = "value: ${p + d}, error: $error"


		return p + d
	}
}