package org.firstinspires.ftc.teamcode.util.qus;

import com.qualcomm.robotcore.hardware.Servo;

public class ServoQUS extends QUS {
    private boolean first_cache = false; // Have we cached a value yet

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
        if (do_cache && speed == queued_position && first_cache) {
            return;
        }

        queued_position = speed;
        needs_write = true;
        first_cache = true;
    }

    @Override
    protected void write() {
        servo.setPosition(queued_position);
    }
}
