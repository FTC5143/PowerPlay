package org.firstinspires.ftc.teamcode.opmodes.autonomous;


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

            robot.drive_train.odo_move(-8, 62, 0, 1.0, 1, 0.02, 4, 0.5);

            for (int i = 0; i < 4; i++) {
                robot.shooter.shoot();

                halt(0.6);

                robot.shooter.unshoot();

                halt(0.7);
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
                robot.drive_train.odo_move(-34, 56, Math.PI, 1.0, 1, 0.02, 6, 0.5);
                robot.drive_train.read_from_imu();
                robot.drive_train.odo_move(-34, 25, Math.PI, 0.33, 1, 0.02, 4);
            }
            else if (pattern == 2) {
                robot.intake.spin(1);
                robot.drive_train.odo_move(-17, 32, Math.PI, 1.0, 1, 0.02, 4);
                robot.drive_train.odo_move(-34, 32, Math.PI, 1.0, 1, 0.02, 4, 0.5);
                robot.drive_train.read_from_imu();
                robot.drive_train.odo_move(-34, 23, Math.PI, 0.33, 1, 0.02, 2);
            } else if (pattern == 1) {
                robot.drive_train.odo_move(-34, 33, Math.PI, 1.0, 1, 0.02, 6, 0.5);
                robot.drive_train.read_from_imu();
                robot.drive_train.odo_move(-34, 23, Math.PI, 0.33, 1, 0.02, 2);
            }


            halt(1);
            robot.wobbler.grab();
            halt(0.5);
            robot.wobbler.raise();

            robot.intake.stop(); // In case we were intaking rings from any of the earlier patterns

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
                robot.drive_train.odo_move(-9, 106, Math.PI/4, 1.0, 1, 0.02, 6);
            }

            drop_wobble_goal();
        }

        robot.wobbler.raise();
        halt(0.5);

        robot.shooter.aim(LOW_GOAL);

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
}
