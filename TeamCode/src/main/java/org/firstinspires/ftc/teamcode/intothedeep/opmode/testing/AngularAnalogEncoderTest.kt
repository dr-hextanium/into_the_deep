package org.firstinspires.ftc.teamcode.intothedeep.opmode.testing

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.AnalogInput
import dev.frozenmilk.dairy.calcified.Calcified
import org.firstinspires.ftc.teamcode.intothedeep.util.AngularAnalogEncoder

@TeleOp(group = "Testing")
@Calcified.Attach
class AngularAnalogEncoderTest : OpMode() {
    private val encoder by lazy { AngularAnalogEncoder(PORT) }
    private val real by lazy { hardwareMap["encoder"] as AnalogInput }

    override fun init() {
        encoder
    }

    override fun loop() {
        if (gamepad1.a) { encoder.zero() }
        if (gamepad1.b) { encoder.invert(true) }

        telemetry.addData("reference", real.voltage)
        telemetry.addData("angle", encoder.get())
        telemetry.addData("raw", encoder.analog())
        telemetry.addData("offset", encoder.offset)
        telemetry.addData("inverted", encoder.inverted)
    }

    companion object {
        const val PORT = 2
    }
}