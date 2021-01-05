package org.firstinspires.ftc.teamcode.util.qus;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class DcMotorQUS extends QUS {
    private double queued_power;

    private boolean do_cache;

    public DcMotorEx motor;

    public DcMotorQUS(DcMotorEx motor) {
        this(motor, true);
    }

    public DcMotorQUS(DcMotorEx motor, boolean do_cache) {
        this.motor = motor;
        this.do_cache = do_cache;
    }

    public void queue_power(double speed) {
        if (do_cache && speed == queued_power) {
            return;
        }

        queued_power = speed;
        needs_write = true;
    }

    @Override
    protected void write() {
        motor.setPower(queued_power);
    }
}
