package org.firstinspires.ftc.teamcode.util.qus;

import com.qualcomm.robotcore.hardware.CRServo;

public class CRServoQUS extends QUS {
    private double queued_power;

    private CRServo servo;

    public CRServoQUS(CRServo servo) {
        this.servo = servo;
    }

    public void queue_power(double speed) {
        queued_power = speed;
        needs_write = true;
    }

    @Override
    protected void write() {
        servo.setPower(queued_power);
    }
}
