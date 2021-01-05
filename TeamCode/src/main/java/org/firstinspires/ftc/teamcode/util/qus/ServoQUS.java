package org.firstinspires.ftc.teamcode.util.qus;

import com.qualcomm.robotcore.hardware.Servo;

public class ServoQUS extends QUS {
    private double queued_position;

    private boolean do_cache;

    public Servo servo;

    public ServoQUS(Servo servo, boolean do_cache) {
        this.servo = servo;
        this.do_cache = do_cache;
    }


    public ServoQUS(Servo servo) {
        this(servo, true);
    }

    public void queue_position(double speed) {
        if (do_cache && speed == queued_position) {
            return;
        }

        queued_position = speed;
        needs_write = true;
    }

    @Override
    protected void write() {
        servo.setPosition(queued_position);
    }
}
