package org.firstinspires.ftc.teamcode.opmodes.autonomous;


import android.media.MediaPlayer;

import com.qualcomm.ftccommon.SoundPlayer;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.HIGH_GOAL;
import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.LOW_GOAL;

public class UltimateGoalAuto extends LiveAutoBase {

    int pattern = 1;

    boolean preloaded_wobble_goal = false;
    boolean second_wobble_goal = false;
    boolean shoot_preloaded_rings = false;

    @Override
    public void on_init() {
        robot.phone_camera.start_streaming();

        if (robot.expansion_hub_2.getBulkInputData().getDigitalInputState(1)) { // shooter is up; put it down
            robot.addWarning("Put the shooter down!");
            int ptsd_sound_id = hardwareMap.appContext.getResources().getIdentifier("ptsd","raw", hardwareMap.appContext.getPackageName());
            SoundPlayer.getInstance().preload(hardwareMap.appContext, ptsd_sound_id);
            SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, ptsd_sound_id);
        }

        while (!isStarted() && !isStopRequested()) {
            pattern = robot.phone_camera.get_pattern();
        }
    }

    @Override
    public void on_start() {
        robot.shooter.aim(HIGH_GOAL);

        robot.drive_train.odo_move(0, 40, 0, 1.0, 1, 0.02, 4);

        if (shoot_preloaded_rings) {
            robot.shooter.spin();

            robot.drive_train.read_from_imu();

            robot.drive_train.odo_move(-8, 62, 0, 1.0, 1, 0.02, 4, 0.3);


            int shots = pattern == 3 ? 3 : 4;

            for (int i = 0; i < shots; i++) {
                robot.shooter.shoot();

                halt(0.6);

                robot.shooter.unshoot();

                if(i != (shots-1)) { // Don't wait for it to go back on the last shot
                    halt(0.4);
                }
            }

            robot.shooter.stop();
        }

        if (preloaded_wobble_goal) {
            if (pattern == 1) {
                robot.drive_train.odo_move(4, 64, 0, 1.0, 1, 0.02, 6);
            } else if (pattern == 2) {
                robot.drive_train.odo_move(-19, 87, 0, 1.0, 1, 0.02, 6);
            } else if (pattern == 3) {
                robot.drive_train.odo_move(6, 110, 0, 1.0, 1, 0.02, 6);
            }

            drop_wobble_goal();

            if (pattern == 1) {
                robot.drive_train.odo_move(4, 54, 0, 1.0, 1, 0.02, 4);
            }
        }

        if (second_wobble_goal) {

            if (pattern == 3) { // more stupid exceptions due to bad odo
                robot.intake.spin(0.7);
                robot.drive_train.odo_move(-17, 62, 0, 1.0, 1, 0.02, 4); // Pick up two rings
                robot.drive_train.odo_move(-17, 33, 0, 0.3, 1, 0.02, 4); // Pick up two rings

                robot.shooter.spin();

                robot.drive_train.odo_move(-8, 62, 0, 1.0, 1, 0.02, 4, 0.3);

                robot.shooter.shoot();
                halt(0.6);
                robot.shooter.unshoot();
                halt(0.4);
                robot.shooter.shoot();
                halt(0.6);
                robot.shooter.unshoot();
                robot.shooter.stop();
            }
            else if (pattern == 2) {
                robot.intake.spin(0.7);
                robot.drive_train.odo_move(-17, 32, Math.PI, 1.0, 1, 0.02, 4);
            } else if (pattern == 1) {
            }

            /// AUTO WOBBLE ALIGNMENT
            robot.phone_camera.set_pipeline(robot.phone_camera.wobble_pipeline);
            robot.phone_camera.start_streaming();

            if (pattern == 3) {
                robot.drive_train.odo_move(-8, 12, 3*Math.PI/2, 1.0, 1, 0.02, 4, 0.3);
            } else {
                robot.drive_train.odo_move(-34, 33, Math.PI, 1.0, 1, 0.02, 4, 0.3);
            }

            robot.drive_train.read_from_imu();
            int wobble_goal_offset = robot.phone_camera.get_wobble_goal_pos() - 9;
            robot.phone_camera.stop_streaming();
            double offset = (((double) wobble_goal_offset) * (12.5 / 18));

            if (pattern == 3) {
                robot.drive_train.odo_move(-12, 17 + offset, -Math.PI/2, 2.0, 1, 0.02, 4, 0.3);
                robot.drive_train.odo_move(-22, 17 + offset, -Math.PI/2, 2.0, 1, 0.02, 4, 0.3);
            }
            else {
                robot.drive_train.odo_move(-34 - offset, 33, Math.PI, 2.0, 1, 0.02, 2, 0.3);
                robot.drive_train.odo_move(-34 - offset, 23, Math.PI, 0.33, 1, 0.02, 2);
            }

            robot.wobbler.grab();
            halt(0.5);
            robot.wobbler.raise();

            if (pattern == 1) {
                robot.drive_train.odo_move(4, 56, 0, 1.0, 1, 0.02, 6);
            } else if (pattern == 2) {
                // SHOOT ON THE WAY THERE
                robot.shooter.spin();
                robot.drive_train.odo_move(-8, 62, 0, 1.0, 1, 0.02, 6, 0.5);

                robot.shooter.shoot();
                halt(0.6);
                robot.shooter.unshoot();
                robot.shooter.stop();
                // After shooting go to the wobble goal drop spot
                robot.drive_train.odo_move(-12, 83, 0, 1.0, 1, 0.02, 6);
            } else if (pattern == 3) {
                // SHOOT TWICE ON THE WAY THERE
                robot.shooter.spin();
                robot.drive_train.odo_move(-8, 62, 0, 1.0, 1, 0.02, 6, 0.5);

                robot.shooter.shoot();
                halt(0.6);
                robot.shooter.unshoot();
                halt(0.4);
                robot.shooter.shoot();
                halt(0.6);
                robot.shooter.unshoot();

                robot.shooter.stop();
                robot.drive_train.odo_move(-5, 104, Math.PI/4, 1.0, 1, 0.02, 6);
            }

            drop_wobble_goal();
        }
//ha
        robot.intake.stop(); // In case we were intaking rings from any of the earlier patterns

        robot.wobbler.raise();
        halt(0.5);

        robot.shooter.aim(LOW_GOAL);
        robot.wobbler.grab();

        if (pattern == 1) {
            robot.drive_train.odo_move(-18, 54, 0, 1.0, -1, -1, 3);
        }

        robot.drive_train.odo_move(-18, 72, 0, 1.0, -1, -1, 8);
    }

    @Override
    public void on_stop() {

    }

    private void drop_wobble_goal() {
        robot.wobbler.lower();
        halt(1);
        robot.wobbler.ungrab();
        halt(0.5);
    }

    public void shoot_until_empty() {
        while (robot.intake.loaded) {
            robot.shooter.shoot();
            halt(0.6);
            robot.shooter.unshoot();
            if (robot.intake.loaded) {
                halt(0.4);
            }
        }
    }
}
