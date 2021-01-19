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

        while (!isStarted() && opModeIsActive()) {
            pattern = robot.phone_camera.get_pattern();
        }
    }

    @Override
    public void on_start() {
        robot.shooter.aim(HIGH_GOAL);

        robot.drive_train.odo_move(0, 40, 0, 1.0, 1, 0.02, 6);

        if (shoot_preloaded_rings) {
            robot.shooter.spin();

            robot.drive_train.odo_move(-8, 57, 0, 1.0, 1, 0.02, 6);

            for (int i = 0; i < 4; i++) {
                robot.shooter.shoot();

                halt(0.5);

                robot.shooter.unshoot();

                halt(0.5);
            }

            robot.shooter.stop();

            robot.shooter.aim(LOW_GOAL);
        }

        if (preloaded_wobble_goal) {
            if (pattern == 1) {
                robot.drive_train.odo_move(4, 64, 0, 1.0, 1, 0.02, 6);
            } else if (pattern == 2) {
                robot.drive_train.odo_move(-14, 87, 0, 1.0, 1, 0.02, 6);
            } else if (pattern == 3) {
                robot.drive_train.odo_move(4, 110, 0, 1.0, 1, 0.02, 6);
            }

            drop_wobble_goal();

            if (pattern == 1) {
                robot.drive_train.odo_move(4, 54, 0, 1.0, 1, 0.02, 4);
            }
        }

        if (second_wobble_goal) {
            robot.drive_train.odo_move(-34, 30, Math.PI, 1.0, 1, 0.02, 8);
            robot.drive_train.odo_move(-34, 24, Math.PI, 1.0, 1, 0.02, 4);

            halt(1);
            robot.wobbler.grab();
            halt(0.5);
            robot.wobbler.raise();

            if (pattern == 1) {
                robot.drive_train.odo_move(4, 56, 0, 1.0, 1, 0.02, 6);
            } else if (pattern == 2) {
                robot.drive_train.odo_move(-18, 83, 0, 1.0, 1, 0.02, 6);
            } else if (pattern == 3) {
                robot.drive_train.odo_move(4, 102, 0, 1.0, 1, 0.02, 6);
            }

            drop_wobble_goal();
        }


        robot.wobbler.raise();
        halt(0.5);

        if (pattern == 1) {
            robot.drive_train.odo_move(-18, 54, 0, 1.0, -1, -1, 3);
        }

        robot.drive_train.odo_move(-18, 72, 0, 1.0, -1, -1, 8);
    }

    @Override
    public void on_stop() {

    }

    private void drop_wobble_goal() {
        resetStartTime();
        robot.wobbler.lower();
        while (getRuntime() <= 1) {}
        robot.wobbler.ungrab();
        while (getRuntime() <= 1.5) {}
    }
}
