package org.firstinspires.ftc.teamcode.intothedeep

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

data class Vector2D(val x: Double, val y: Double) {
	val theta = atan2(y, x) * (180.0 / Math.PI)
	val magnitude = hypot(x, y)

	operator fun plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)
	operator fun minus(other: Vector2D) = Vector2D(x - other.x, y - other.y)
	operator fun times(other: Vector2D) = Vector2D(x * other.x, y * other.y)
	operator fun times(a: Int) = Vector2D(x * a, y * a)
	operator fun times(a: Double) = Vector2D(x * a, y * a)
	operator fun unaryMinus() = Vector2D(-x, -y)
	operator fun unaryPlus() = Vector2D(+x, +y)

	override fun toString() = "Vector2D(x = $x, y = $y, theta = $theta)"

	companion object {
		fun polar(magnitude: Double, theta: Double) =
			Vector2D(magnitude * cos(theta), magnitude * sin(theta))
	}
}

fun main() {
	val v1 = Vector2D(2.0, 3.0)
	val v2 = Vector2D(4.0, 5.0)
	val v3 = Vector2D(2.0, 3.0)

	println(v1 + v2)
	println(v1 - v2)
	println(v1 * v2)
	println(v1 * 2.1)
	println(v1 * 2)
	println(-v1)
	println(v1 == v2)
	println(v1 == v3)
}