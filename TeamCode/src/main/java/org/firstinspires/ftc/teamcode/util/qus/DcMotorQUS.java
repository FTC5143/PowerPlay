package org.firstinspires.ftc.teamcode.util.qus;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class DcMotorQUS extends QUS {
    private double queued_power;

    public DcMotorEx motor;

    public DcMotorQUS(DcMotorEx motor) {
        this.motor = motor;
    }

    public void queue_power(double speed) {
        queued_power = speed;
        needs_write = true;
    }

    @Override
    protected void write() {
        motor.setPower(queued_power);
    }
}
