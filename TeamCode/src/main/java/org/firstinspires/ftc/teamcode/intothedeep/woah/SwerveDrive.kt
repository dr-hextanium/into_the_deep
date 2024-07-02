package org.firstinspires.ftc.teamcode.intothedeep.woah

import dev.frozenmilk.util.units.angle.Angle
import dev.frozenmilk.util.units.distance.inches
import dev.frozenmilk.util.units.position.Vector2D

/* TODO:
 * - get input from gamepad
 *   - left joystick for translation         [x]
 *   - right joystick x-axis for rotation    [x]
 *   - have a mapping curve on power         [x]
 *
 * - implement wrappers for
 * 	 - analog encoders (for the servos)      [x]
 *   - good wrappers for continuous servos   [ ]
 *
 * - implement kinematics for swerve
 *   - measure distance from origin          [x]
 *   - M_n_x = V_x + (ω * L/2)
 *   - M_n_y = V_y - (ω * W/2)
 */

class SwerveDrive {
	fun update(vector: Vector2D, omega: Angle) {

	}

	companion object {
		val track_width = 8.386.inches
		val halved = track_width / 2.0
	}
}