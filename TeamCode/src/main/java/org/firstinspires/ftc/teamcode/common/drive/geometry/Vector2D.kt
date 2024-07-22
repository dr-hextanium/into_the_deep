package org.firstinspires.ftc.teamcode.common.drive.geometry


class Vector2D(public var x: Double, public var y: Double) {
	fun mult(scalar: Double): Vector2D {
		return Vector2D(x * scalar, y * scalar)
	}

	fun divide(scalar: Double): Vector2D {
		return Vector2D(x / scalar, y / scalar)
	}

	fun subt(other: Vector2D): Vector2D {
		return Vector2D(x - other.x, y - other.y)
	}

	fun dot(other: Vector2D): Double {
		return x * other.x + y * other.y
	}

	fun magnitude(): Double {
		return Math.hypot(x, y)
	}

	fun unit(): Vector2D {
		return divide(magnitude())
	}

	fun rotate(angle: Double): Vector2D {
		return Vector2D(
			x * Math.cos(angle) - y * Math.sin(angle),
			x * Math.sin(angle) + y * Math.cos(angle))
	}

	fun cross(other: Vector2D): Double {
		return x * other.y - y * other.x
	}

	override fun toString(): String {
		return String.format("{%.2f, %.2f}", x, y)
	}

	companion object {
		fun fromHeadingAndMagnitude(h: Double, m: Double): Vector2D {
			return Vector2D(Math.cos(h) * m, Math.sin(h) * m)
		}
	}
}