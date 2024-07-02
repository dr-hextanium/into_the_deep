package org.firstinspires.ftc.teamcode.intothedeep.woah

import dev.frozenmilk.util.units.angle.Angle
import dev.frozenmilk.util.units.angle.deg
import dev.frozenmilk.util.units.angle.rad
import dev.frozenmilk.util.units.distance.inches
import dev.frozenmilk.util.units.distance.meters
import dev.frozenmilk.util.units.position.Vector2D
import kotlin.math.PI

/* TODO:
 * - get input from gamepad
 *   - left joystick for translation
 *   - right joystick x-axis for rotation
 *   - have a mapping curve on power
 *
 * - implement wrappers for
 * 	 - analog encoders (for the servos)
 *   - good wrappers for continuous servos
 *
 * - implement kinematics for swerve
 *   - measure distance from origin [x]
 *   - M_n_x = V_x + (ω * L/2)
 *   - M_n_y = V_y - (ω * W/2)
 */

val center_to_center = 8.386.inches
val other_center_to_center = (0.213).meters

val n = 180.deg
val x = PI.rad

class SwerveDrive {
	fun update(vector: Vector2D, omega: Angle) {

	}
}