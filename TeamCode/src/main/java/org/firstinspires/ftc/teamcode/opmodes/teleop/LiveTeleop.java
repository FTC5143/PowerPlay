package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.opmodes.LiveTeleopBase;

import static android.graphics.Typeface.NORMAL;

@TeleOp(name="Teleop Live", group="driver control")
//@Disabled
public class LiveTeleop extends LiveTeleopBase {

    boolean dpad_up_pressed = false;
    boolean dpad_down_pressed = false;

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

        if(gamepad2.left_bumper) {
            robot.lift.elevate_to(prepared_level);
        }

        if(gamepad2.right_bumper) {
            robot.lift.elevate_to(2);
        }

        if(gamepad2.dpad_up && !dpad_up_pressed) {
            prepared_level = Range.clip(prepared_level + 1, 0, 12);
            dpad_up_pressed = true;
        } else if (!gamepad2.dpad_up) {
            dpad_up_pressed = false;
        }

        if(gamepad2.dpad_down && !dpad_down_pressed) {
            prepared_level = Range.clip(prepared_level - 1, 0, 12);
            dpad_down_pressed = true;
        } else if (!gamepad2.dpad_down) {
            dpad_down_pressed = false;
        }

        if(gamepad2.back) {
            if(gamepad2.dpad_up) {
                robot.lift.max_lift();
            }
            else {
                robot.lift.min_lift();
            }
        }

        /// GAMEPAD TWO BACK HOTKEYS ///

        if (gamepad2.back) {
            if (gamepad2.left_stick_button) {
                robot.phone_camera.start_streaming();
            }
        }

        /// DRIVE CONTROLS ///

        double speed_mod = 1;

        if(gamepad1.left_bumper) {
            speed_mod = 0.25;
        } else if(gamepad1.right_bumper) {
            speed_mod = 0.5;
        }

        robot.drive_train.mechanum_drive((gamepad1.left_stick_x+gamepad2.left_stick_x) * speed_mod, (gamepad1.left_stick_y+gamepad2.left_stick_y) * speed_mod, (gamepad1.right_stick_x+gamepad2.right_stick_x) * speed_mod);

    }

    @Override
    public void on_stop() {

    }
}
