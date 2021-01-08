package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.HIGH_GOAL;
import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.LOW_GOAL;

@Autonomous(name = "ParkAuto", group = "autonomous")
public class ParkAuto extends LiveAutoBase {

    int pattern = 1;

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

        robot.drive_train.odo_move(0, 40, 0, 1.0);

        robot.shooter.aim(HIGH_GOAL);

        robot.drive_train.odo_move(-10, 62, 0, 1.0);

        for (int i = 0; i < 4; i++) {
            resetStartTime();

            robot.shooter.shoot();

            while (getRuntime() <= 0.5) {}

            robot.shooter.unshoot();

            while (getRuntime() <= 1) {}
        }

        robot.shooter.aim(LOW_GOAL);

        if (pattern == 1) {
            robot.drive_train.odo_move(4, 56, 0, 1.0);
        } else if (pattern == 2) {
            robot.drive_train.odo_move(-19, 79, 0, 1.0);
        } else if (pattern == 3) {
            robot.drive_train.odo_move(4, 102, 0, 1.0);
        }

        resetStartTime();
        robot.wobbler.lower();
        while (getRuntime() <= 1) {}
        robot.wobbler.ungrab();
        while (getRuntime() <= 1.5) {}

        robot.drive_train.odo_move(-10, 72, 0, 1.0, -1, -1);
    }

    @Override
    public void on_stop() {

    }
}
