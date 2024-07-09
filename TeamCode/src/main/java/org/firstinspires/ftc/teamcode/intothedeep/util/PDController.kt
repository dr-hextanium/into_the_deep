package org.firstinspires.ftc.teamcode.intothedeep.util

import com.qualcomm.robotcore.util.ElapsedTime
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.sign

class PDController(private val kP: Double, private val kD: Double) {
	private val timer = ElapsedTime()
	var telem = ""
	private var lastError = 0.0
	private var lastTime = now()

	private fun now() = timer.now(TimeUnit.MILLISECONDS)

	fun run(current: Double, target: Double): Double {
		val error = (target - current)
			.let { if (abs(it) < 180) it else -(sign(it)) * (360 - abs(it)) }

		telem = "error: $error"

		val dt = now() - lastTime
		val de = error - lastError

		lastError = error
		lastTime = now()

		val p = (kP * error)
		val d = (kD * de / dt)

		return p + d
	}
}