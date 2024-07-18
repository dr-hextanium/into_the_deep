package org.firstinspires.ftc.teamcode.intothedeep.util.controllers

import com.qualcomm.robotcore.util.ElapsedTime
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.sign

class AngularPDController(kP: Double, kD: Double) : Controller(kP, kD) {
	override fun run(current: Double, target: Double): Double {
		val error = (target - current)
			.let { if (abs(it) < 180) it else -(sign(it)) * (360 - abs(it)) }

		telemetry = "error: $error"

		val de = error - lastError

		lastError = error

		val p = (kP * error)
		val d = (kD * de / dt)

		return p + d
	}
}