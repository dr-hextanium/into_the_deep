package org.firstinspires.ftc.teamcode.intothedeep.woah

import java.util.function.Supplier


fun f(x: Supplier<Int>) { 2 * x.get() }

class Box(val value: Int) : Supplier<Int> {
	override fun get() = value
}

fun main() {
	val doubled = f(Box(1))
	
	var n = 7
	
	val other = f { n + 3 }
}