package org.firstinspires.ftc.teamcode.util.qus;

import com.qualcomm.robotcore.hardware.Servo;

public class ServoQUS extends QUS {
    private double queued_position;

    private Servo servo;

    public ServoQUS(Servo servo) {
        this.servo = servo;
    }

    public void queue_position(double speed) {
        queued_position = speed;
        needs_write = true;
    }

    @Override
    protected void write() {
        servo.setPosition(queued_position);
    }
}
