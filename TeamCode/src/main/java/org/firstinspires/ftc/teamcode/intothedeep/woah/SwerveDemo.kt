package org.firstinspires.ftc.teamcode.intothedeep.woah

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import dev.frozenmilk.dairy.core.util.OpModeLazyCell
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.frozenmilk.dairy.calcified.Calcified
import dev.frozenmilk.dairy.core.FeatureRegistrar
import dev.frozenmilk.util.units.angle.Angle
import dev.frozenmilk.util.units.angle.rad
import dev.frozenmilk.util.units.distance.feet
import dev.frozenmilk.util.units.distance.inches
import dev.frozenmilk.util.units.position.Vector2D
import kotlin.math.atan2
import kotlin.math.pow
import org.firstinspires.ftc.teamcode.intothedeep.woah.SwerveModuleConfiguration.*

/* TODO:
 * - get input from gamepad
 *   - left joystick for translation       [x]
 *   - right joystick x-axis for rotation  [x]
 *   - have a mapping curve on power       [x]
 */

@TeleOp
@Calcified.Attach( // attaches the Calcified feature
	automatedCacheHandling = true, // these are settings for the feature that we can set
)
class SwerveDemo : OpMode() {
    val frontLeft by OpModeLazyCell { SwerveModule(FRONT_LEFT)  }
    val frontRight by OpModeLazyCell { SwerveModule(FRONT_RIGHT) }
	val backLeft by OpModeLazyCell { SwerveModule(BACK_LEFT) }
	val backRight by OpModeLazyCell { SwerveModule(BACK_RIGHT) }

	init { FeatureRegistrar.checkFeatures(this, Calcified) }

	override fun init() {
        frontLeft.initialize()
        frontRight.initialize()
        backLeft.initialize()
        backRight.initialize()
    }

	override fun loop() {
//		val x = MAXIMUM_VELOCITY * map(gamepad1.left_stick_x.toDouble())
//		val y = MAXIMUM_VELOCITY * map(gamepad1.left_stick_y.toDouble())
//		val omega = MAXIMUM_ROTATION * gamepad1.right_stick_x.toDouble()

//	    val translation = Vector2D(x, y)
        telemetry.addData("front left encoder", frontLeft.readEncoder())
        telemetry.addData("front right encoder", frontRight.readEncoder())
        telemetry.addData("back left encoder", backLeft.readEncoder())
        telemetry.addData("back right encoder", backRight.readEncoder())
	}

    fun map(n: Double) = n.pow(3)

	companion object {
		val MAXIMUM_VELOCITY = 5.feet // per second
		val MAXIMUM_ROTATION = 1.rad // per second
	}
}
