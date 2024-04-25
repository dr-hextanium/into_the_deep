package org.firstinspires.ftc.teamcode.intothedeep.util

import dev.frozenmilk.dairy.calcified.hardware.controller.calculation.CalculationComponent
import dev.frozenmilk.util.units.ReifiedUnit
import dev.frozenmilk.util.units.Unit
import java.util.function.Supplier

/**
 * Receives the state of all motors through `states` and normalizes itself accordingly.
 * This component is partially destructive; it does not preserve `accumulation`.
 * By partial destruction, all earlier components are affected; not later ones by consequence.
 */
class DoubleNormalizationComponent(val states: Supplier<List<Double>>) : CalculationComponent<Double> {
    override fun calculate(
        accumulation: Double,
        currentState: Double,
        target: Double,
        error: Double,
        deltaTime: Double
    ): Double {
        val max = states.get().max()

        return if (max > 1) accumulation / max else accumulation
    }
}

/**
 * Receives the state of all motors through `states` and normalizes itself accordingly.
 * This component is partially destructive; it does not preserve `accumulation`.
 * By partial destruction, all earlier components are affected; not later ones by consequence.
 */
class UnitNormalizationComponent<U: Unit<U>, RU: ReifiedUnit<U, RU>>(val states: Supplier<List<RU>>) : CalculationComponent<RU> {
    override fun calculate(
        accumulation: RU,
        currentState: RU,
        target: RU,
        error: RU,
        deltaTime: Double
    ): RU {
        val max = states.get()
            .map { it.intoCommon().value }
            .maxOf { it }

        return if (max > 1.0) accumulation / max else accumulation
    }
}