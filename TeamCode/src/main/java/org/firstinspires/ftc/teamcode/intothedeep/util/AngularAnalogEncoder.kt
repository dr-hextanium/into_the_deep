package org.firstinspires.ftc.teamcode.intothedeep.util

import com.qualcomm.robotcore.hardware.DcMotor
import dev.frozenmilk.dairy.calcified.Calcified
import dev.frozenmilk.dairy.calcified.hardware.sensor.CalcifiedAnalogInput
import dev.frozenmilk.dairy.core.util.supplier.numeric.unit.EnhancedUnitSupplier
import dev.frozenmilk.util.units.angle.Angle
import dev.frozenmilk.util.units.angle.AngleUnit
import dev.frozenmilk.util.units.angle.wrappedDeg
import java.util.function.Supplier
import kotlin.math.abs

class AngularAnalogEncoder(port: Int) : Supplier<Angle> {
    private val encoder: EnhancedUnitSupplier<AngleUnit, Angle>
    private val analog: CalcifiedAnalogInput

    var inverted = false
        private set(value) { field = value }
    var offset = 0.wrappedDeg
        private set(value) { field = value }



    init {
        analog = Calcified.controlHub.getAnalogInput(port)

        encoder = EnhancedUnitSupplier({
            val raw = (analog.supplier.get() / 3330.0)
            val inverted = if (inverted) abs(1.0 - raw) else raw

            (360.0 * inverted).wrappedDeg
        })
    }

    fun invert(value: Boolean): AngularAnalogEncoder {
        inverted = value
        return this
    }

    fun zero(offset: Angle): AngularAnalogEncoder {
        this.offset = offset
        return this
    }

    fun zero() = zero(offset)
    override fun get(): Angle {
        return (encoder.supplier.get() + offset)
            .intoDegrees()
            .intoWrapping()
    }

    fun analog(): Double {
        return analog.supplier.get()
    }
}