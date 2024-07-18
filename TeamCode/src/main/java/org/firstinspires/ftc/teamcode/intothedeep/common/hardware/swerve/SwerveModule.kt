package org.firstinspires.ftc.teamcode.intothedeep.common.hardware.swerve

import com.acmerobotics.dashboard.config.Config
import com.arcrobotics.ftclib.controller.PIDFController
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.intothedeep.common.hardware.AnalogEncoder
import org.firstinspires.ftc.teamcode.intothedeep.common.hardware.swerve.ModuleConstants.kD
import org.firstinspires.ftc.teamcode.intothedeep.common.hardware.swerve.ModuleConstants.kI
import org.firstinspires.ftc.teamcode.intothedeep.common.hardware.swerve.ModuleConstants.kP
import org.firstinspires.ftc.teamcode.intothedeep.common.hardware.swerve.ModuleConstants.kS
import org.firstinspires.ftc.teamcode.intothedeep.common.util.normDelta
import kotlin.math.abs
import kotlin.math.sign

class SwerveModule(hw: HardwareMap, name: String) {
    private val input by lazy { hw["$name input"] as AnalogInput }
    private val motor by lazy { hw["$name motor"] as DcMotor }
    private val servo by lazy { hw["$name servo"] as CRServo }

    private val encoder by lazy { AnalogEncoder(input) }

    private val controller = PIDFController(kP, kI, kD, 0.0)

    private var flipped = false

    private var measured = 0.0
    private var target = 0.0

    fun initialize() {
        motor.power = 0.0
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        servo.power = 0.0
    }

    fun read() { measured = encoder.position() }

    fun update() {
        controller.setPIDF(kP, kI, kD, 0.0)

        val measured = normedMeasured()
        var target = normedTarget()

        val error = normDelta(target - measured)
        val abs = abs(error)

        flipped = abs > 90.0

        if (flipped) target = normDelta(target - 180.0)

        val power = Range.clip(
            controller.calculate(0.0, error),
            -1.0,
            1.0
        ).let { if (it.isNaN()) 0.0 else it }

        servo.power = power + (if (abs > 0.02) kS * power.sign else 0.0)
    }

    fun writeMotor(power: Double) {
        motor.power = power * if (flipped) -1.0 else 1.0
    }

    fun target(target: Double) { this.target = normDelta(target) }

    fun normedTarget() = normDelta(target - 180.0)
    fun normedMeasured() = normDelta(measured - 180.0)
}

@Config
object ModuleConstants {
    @JvmField
    var kP = 0.0

    @JvmField
    var kI = 0.0

    @JvmField
    var kD = 0.0

    @JvmField
    var kS = 0.0
}