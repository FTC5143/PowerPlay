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

        while (!isStarted()) {
            pattern = robot.phone_camera.get_pattern();
        }
    }

    @Override
    public void on_start() {
        robot.shooter.spin();

        robot.drive_train.odo_move(0, 40, 0, 1.0, 1, 0.02, 6);

        if (shoot_preloaded_rings) {
            robot.shooter.aim(HIGH_GOAL);

            robot.drive_train.odo_move(-8, 57, 0, 1.0, 1, 0.02, 6);

            for (int i = 0; i < 4; i++) {
                resetStartTime();

                robot.shooter.shoot();

                while (getRuntime() <= 0.5) {
                }

                robot.shooter.unshoot();

                while (getRuntime() <= 1) {
                }
            }

            robot.shooter.aim(LOW_GOAL);
        }

        if (preloaded_wobble_goal) {
            if (pattern == 1) {
                robot.drive_train.odo_move(4, 56, 0, 1.0, 1, 0.02, 6);
            } else if (pattern == 2) {
                robot.drive_train.odo_move(-14, 79, 0, 1.0, 1, 0.02, 6);
            } else if (pattern == 3) {
                robot.drive_train.odo_move(4, 102, 0, 1.0, 1, 0.02, 6);
            }

            drop_wobble_goal();

            if (pattern == 1) {
                robot.drive_train.odo_move(-14, 52, 0, 1.0, 1, 0.02, 4);
            }
        }

        if (second_wobble_goal) {
            robot.drive_train.odo_move(-36, 0, Math.PI, 1.0, 1, 0.02, 4);

            resetStartTime();
            robot.wobbler.lower();
            robot.wobbler.ungrab();
            while (getRuntime() <= 1) {
            }
            robot.wobbler.grab();
            while (getRuntime() <= 1.5) {
            }
            robot.wobbler.raise();

            if (pattern == 1) {
                robot.drive_train.odo_move(4, 56, 0, 1.0, 1, 0.02, 6);
            } else if (pattern == 2) {
                robot.drive_train.odo_move(-14, 79, 0, 1.0, 1, 0.02, 6);
            } else if (pattern == 3) {
                robot.drive_train.odo_move(4, 102, 0, 1.0, 1, 0.02, 6);
            }

            drop_wobble_goal();
        }

        robot.drive_train.odo_move(-20, 72, 0, 1.0, -1, -1, 8);
    }

    @Override
    public void on_stop() {

    }

    private void drop_wobble_goal() {
        resetStartTime();
        robot.wobbler.lower();
        robot.wobbler.ungrab();
        while (getRuntime() <= 1) {}
        robot.wobbler.raise();
        while (getRuntime() <= 1.5) {}
        robot.wobbler.grab();
    }
}
