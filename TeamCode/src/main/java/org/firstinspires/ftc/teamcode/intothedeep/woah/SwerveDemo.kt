package org.firstinspires.ftc.teamcode.intothedeep.woah

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.frozenmilk.util.units.angle.Angle
import dev.frozenmilk.util.units.angle.rad
import dev.frozenmilk.util.units.distance.feet
import dev.frozenmilk.util.units.position.Vector2D
import kotlin.math.atan2
import kotlin.math.pow

/* TODO:
 * - get input from gamepad
 *   - left joystick for translation       [x]
 *   - right joystick x-axis for rotation  [x]
 *   - have a mapping curve on power       [x]
 */

@TeleOp
class SwerveDemo : OpMode() {
	override fun init() {

	}

	override fun loop() {
		val x = MAXIMUM_VELOCITY * map(gamepad1.left_stick_x.toDouble())
		val y = MAXIMUM_VELOCITY * map(gamepad1.left_stick_y.toDouble())
		val omega = MAXIMUM_ROTATION * gamepad1.right_stick_x.toDouble()

	    val translation = Vector2D(x, y)
	}

    fun map(n: Double) = n.pow(3)

	companion object {
		val MAXIMUM_VELOCITY = 5.feet // per second
		val MAXIMUM_ROTATION = 1.rad // per second
	}
}
