package org.firstinspires.ftc.teamcode.intothedeep.zarif

import dev.frozenmilk.util.units.angle.Angle
import dev.frozenmilk.util.units.distance.meters
import dev.frozenmilk.util.units.position.Vector2D

class SwerveTrain {
	val fr by lazy { SwerveModule(SwerveModule.Location.FRONT_RIGHT) }
	val fl by lazy { SwerveModule(SwerveModule.Location.FRONT_LEFT) }
	val br by lazy { SwerveModule(SwerveModule.Location.BACK_RIGHT) }
	val bl by lazy { SwerveModule(SwerveModule.Location.BACK_LEFT) }

	val modules by lazy { listOf(fr, fl, br, bl) }

	fun read() {

	}

	fun set(displacement: Vector2D, omega: Angle) {
		val x = displacement.x
		val y = displacement.y

		val rotate = halved * omega.intoRadians().value

		val A = x - rotate
		val B = x + rotate
		val C = y - rotate
		val D = y + rotate

		fl.set(B, D); fr.set(B, C)
		bl.set(A, D); br.set(A, C)
	}

	fun write() {
		modules.forEach { it.write() }
	}

	companion object {
		val centers = 0.213.meters
		val halved = centers / 2.0
		val radius = (halved.pow(2) * 2.0).sqrt()
	}
}