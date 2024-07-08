package org.firstinspires.ftc.teamcode.intothedeep.util

import com.qualcomm.robotcore.util.ElapsedTime
import java.util.concurrent.TimeUnit

class PDController(private val kP: Double, private val kD: Double) {
	private var lastError = 0.0
	private var lastTime = now()

	private val timer = ElapsedTime()

	private fun now() = timer.now(TimeUnit.MILLISECONDS)

	fun run(current: Double, target: Double): Double {
		val error = (target - current)

		val dt = now() - lastTime
		val de = error - lastError

		lastError = error
		lastTime = now()

		val p = (kP * error)
		val d = (kD * de / dt)

		return p + d
	}
}