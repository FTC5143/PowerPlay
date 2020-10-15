package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.opmodes.LiveTeleopBase;

@TeleOp(name="Teleop Live", group="driver control")
//@Disabled
public class DebugOpmodeLive extends LiveTeleopBase {

    boolean dpad_up_pressed = false;
    boolean dpad_down_pressed = false;

    boolean dpad_left_pressed = false;
    boolean dpad_right_pressed = false;

    boolean a2_pressed = false;

    long slow_accel_starttime = System.currentTimeMillis();

    int prepared_level = 1;

    @Override
    public void on_init() {

    }

    @Override
    public void on_start() {
        this.getRuntime();
    }

    @Override
    public void on_loop() {
        double speed_mod = 1;

        if(gamepad1.left_bumper) {
            speed_mod = 0.25;
        } else if(gamepad1.right_bumper) {
            speed_mod = 0.5;
        }

        robot.drive_train.mechanum_drive(gamepad1.left_stick_x * speed_mod, gamepad1.left_stick_y * speed_mod, gamepad1.right_stick_x * speed_mod);
    }

    @Override
    public void on_stop() {

    }
}
