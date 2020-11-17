package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.opmodes.LiveTeleopBase;

@TeleOp(name="Teleop Live", group="driver control")
//@Disabled
public class LiveTeleop extends LiveTeleopBase {

    @Override
    public void on_init() {

    }

    @Override
    public void on_start() {
        this.getRuntime();
    }

    @Override
    public void on_loop() {

        /// GAMEPAD 2 CONTROLS ///

        if (gamepad2.back) { // Hotkeys of a sort, less used things only accessed by holding back
            if (gamepad2.x) {
                robot.phone_camera.start_streaming();
            } else if (gamepad2.y) {
                robot.phone_camera.stop_streaming();
            }

            if (gamepad2.dpad_up) {
                robot.shooter.update_pid_coeffs();
            }

        } else {
            if (gamepad2.a) {
                robot.shooter.spin();
            } else if (gamepad2.b) {
                robot.shooter.stop();
            }

            if (gamepad2.x) {
                robot.intake.spin();
            } else if (gamepad2.y) {
                robot.intake.stop();
            }

            if (gamepad2.right_bumper) {
                robot.shooter.shoot();
            } else if (gamepad2.left_bumper) {
                robot.shooter.unshoot();
            }
        }

        /// GAMEPAD 1 CONTROLS ///

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
