package org.firstinspires.ftc.teamcode.intothedeep.common.hardware

import com.qualcomm.robotcore.hardware.AnalogInput
import org.firstinspires.ftc.teamcode.intothedeep.common.util.normDelta

class AnalogEncoder(val input: AnalogInput) {
    private var inverted = false
    private var offset = 0.0

    private val range = 3.3

    private val voltage: Double
        get() = input.voltage

    fun zero(offset: Double) = run {
        this.offset = offset
        this
    }

    fun invert(inverted: Boolean) = run {
        this.inverted = inverted
        this
    }

    fun direction() = inverted

    fun position(): Double {
        val raw = (if (inverted) 1 - voltage / range else voltage / range)
        val positive = raw * 360.0
        val normalized = normDelta(positive)

        return normalized - offset
    }
}