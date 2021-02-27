package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.opmodes.LiveTeleopBase;

import static android.graphics.Typeface.NORMAL;

@TeleOp(name="Teleop Live", group="driver control")
//@Disabled
public class LiveTeleop extends LiveTeleopBase {

    enum TeleopStates {
        NORMAL,
        UNJAM,
        RESET
    }

    TeleopStates state = TeleopStates.NORMAL;

    @Override
    public void on_init() {
        robot.drive_train.odo_reset(-18, 72, 0);
    }

    @Override
    public void on_start() {
        this.getRuntime();
    }

    @Override
    public void on_loop() {

        //// GAMEPAD ONE ////

        // Wobbler control
        if (gamepad1.a) {
            robot.wobbler.grab();
        } else if (gamepad1.b) {
            robot.wobbler.ungrab();
        }

        if (gamepad1.x) {
            robot.wobbler.raise();
        } else if (gamepad1.y) {
            robot.wobbler.lower();
        }

        // Popout intake
        if (gamepad1.right_trigger > 0.5) {
            robot.intake.popout();
        } else {
            robot.intake.popin();
        }

        /// GAMEPAD TWO BACK HOTKEYS ///

        if (gamepad2.back) {
            if (gamepad2.left_stick_button) {
                robot.phone_camera.start_streaming();
            }
            if (gamepad2.right_stick_button) {
                robot.shooter.update_pid_coeffs();
            }

            if (gamepad2.x && state == TeleopStates.NORMAL) {
                state = TeleopStates.UNJAM;
                resetStartTime();
            }

            if (gamepad2.dpad_up) {
                robot.shooter.stop_encoder_reset();
            }

            if (gamepad2.dpad_down) {
                robot.shooter.encoder_reset();
            }
        } else {
            //// GAMEPAD TWO ////

            // Flywheel control
            if (gamepad2.a) {
                robot.shooter.spin();
            } else if (gamepad2.b) {
                robot.shooter.stop();
            }

            if (state == TeleopStates.NORMAL) {
                // Normal intake control
                if (gamepad2.x) {
                    robot.intake.spin(1);
                } else if (gamepad2.y) {
                    robot.intake.stop();
                }
            } else if (state == TeleopStates.UNJAM) {
                // Intake unjam
                if (getRuntime() < 0.75 /*seconds*/) {
                    robot.intake.spin(-1);
                } else {
                    robot.intake.spin(1);
                    state = TeleopStates.NORMAL;
                }

                if (gamepad2.y) {
                    robot.intake.stop();
                    state = TeleopStates.NORMAL;
                }
            }

            // Shunter control
            if (gamepad2.right_bumper) {
                robot.shooter.shoot();
            } else {
                robot.shooter.unshoot();
            }

            robot.intake.forced_chop = gamepad2.left_bumper;

            // Angler control
            if (gamepad2.dpad_up) {
                robot.shooter.aim(3);
            } else if (gamepad2.dpad_down) {
                robot.shooter.aim(0);
            } else if (gamepad2.dpad_left) {
                robot.shooter.aim(1);
            } else if (gamepad2.dpad_right) {
                robot.shooter.aim(2);
            }
        }

        /// DRIVE CONTROLS ///

        double speed_mod = 1;

        if(gamepad1.left_bumper) {
            speed_mod = 0.25;
        } else if(gamepad1.right_bumper) {
            speed_mod = 0.5;
        }

        if (gamepad1.back) {
            robot.drive_train.odo_drive_towards(
                    -10, 62,
                    ((double)Math.round(robot.drive_train.lcs.a/(Math.PI * 2))) * (Math.PI * 2),
                    1
            );
        } else {
            robot.drive_train.mechanum_drive(gamepad1.left_stick_x * speed_mod, gamepad1.left_stick_y * speed_mod, gamepad1.right_stick_x * speed_mod);
        }
    }

    @Override
    public void on_stop() {

    }
}
