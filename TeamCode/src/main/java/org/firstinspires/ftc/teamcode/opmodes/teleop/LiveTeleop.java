package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.opmodes.LiveTeleopBase;

import static android.graphics.Typeface.NORMAL;

@TeleOp(name="Teleop Live", group="driver control")
//@Disabled
public class LiveTeleop extends LiveTeleopBase {

    boolean dpad_up_pressed = false;
    boolean dpad_down_pressed = false;

    boolean gp1_a_pressed = false;
    boolean gp1_b_pressed = false;
    boolean gp1_y_pressed = false;

    int prepared_level = 1;

    int drive_mul = -1;


    @Override
    public void on_init() {
    }

    @Override
    public void on_start() {
        this.getRuntime();
    }

    @Override
    public void on_loop() {

        /// GAMEPAD TWO BACK HOTKEYS ///
        if(gamepad2.back) {
            if(gamepad2.dpad_up) {
                robot.lift.max_lift();
            }
            else {
                robot.lift.min_lift();
            }


        } else {
            if(gamepad2.left_bumper) {
                robot.lift.elevate_to(prepared_level);
            }

            if(gamepad2.dpad_up && !dpad_up_pressed) {
                prepared_level = Range.clip(prepared_level + 1, 0, robot.lift.max_level);
                dpad_up_pressed = true;
            } else if (!gamepad2.dpad_up) {
                dpad_up_pressed = false;
            }

            if(gamepad2.dpad_down && !dpad_down_pressed) {
                prepared_level = Range.clip(prepared_level - 1, 0, robot.lift.max_level);
                dpad_down_pressed = true;
            } else if (!gamepad2.dpad_down) {
                dpad_down_pressed = false;
            }


            robot.lift.tweak(- gamepad2.left_trigger);
        }



        // Nothing to see here
        if ((gamepad1.back && gamepad1.a) && !gp1_a_pressed) {
            robot.sound_player.vineboom();
            gp1_a_pressed = true;
        } else if (!gamepad1.a) {
            gp1_a_pressed = false;
        }

        // Nothing to see here
        if ((gamepad1.back && gamepad1.b) && !gp1_b_pressed) {
            robot.sound_player.skypecall();
            gp1_b_pressed = true;
        } else if (!gamepad1.b) {
            gp1_b_pressed = false;
        }

        if (gamepad2.a) {
            robot.lift.open_claw();
        } else if (gamepad2.b) {
            robot.lift.close_claw();
        }


        /// DRIVE CONTROLS ///
        double speed_mod = 1;

        if(gamepad1.left_bumper) {
            speed_mod = 0.25;
        } else if(gamepad1.right_bumper) {
            speed_mod = 0.5;
        }

        robot.drive_train.omni_drive(
            (gamepad1.left_stick_x+gamepad2.left_stick_x) * speed_mod * drive_mul,
            (gamepad1.left_stick_y+gamepad2.left_stick_y) * speed_mod * drive_mul,
            (gamepad1.right_stick_x+gamepad2.right_stick_x) * speed_mod
        );
    }

    @Override
    public void on_stop() {

    }
}
