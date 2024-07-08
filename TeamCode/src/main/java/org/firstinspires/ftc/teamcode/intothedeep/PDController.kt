package org.firstinspires.ftc.teamcode.intothedeep

import com.qualcomm.robotcore.util.ElapsedTime
import java.util.concurrent.TimeUnit

class PDController(private val kP: Double, private val kD: Double) {
	private var target = 0.0
	private var lastError = 0.0

	private val timer = ElapsedTime()
	private var lastTime = now()

	var telemetry = ""

	fun updateTarget(newTarget: Double) {
		target = newTarget
	}

	private fun now() = timer.now(TimeUnit.MILLISECONDS)

	fun run(current: Double): Double {
		val error = (target - current)

		val dt = now() - lastTime
		val de = error - lastError

		lastError = error
		lastTime = now()

		val p = (kP * error)
		val d = (kD * de / dt)

		telemetry = "error: $error, de: $de, dt: $dt, p term: $p, d term: $d"

		println(telemetry)

		return p + d
	}
}