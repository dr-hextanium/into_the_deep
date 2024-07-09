package org.firstinspires.ftc.teamcode.intothedeep.util.controllers

import com.qualcomm.robotcore.util.ElapsedTime
import java.util.concurrent.TimeUnit.MILLISECONDS

abstract class Controller(protected val kP: Double, protected val kD: Double) {
	protected val timer = ElapsedTime()

	protected val dt: Long
		get() = (now() - timer.time(MILLISECONDS)).also { timer.reset() }

	protected var lastError = 0.0

	var telemetry = ""

	private fun now() = timer.now(MILLISECONDS)

	abstract fun run(current: Double, target: Double): Double
}