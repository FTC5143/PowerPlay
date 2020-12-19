package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.HIGH_GOAL;
import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.LOW_GOAL;

@Autonomous(name = "ParkAuto", group = "autonomous")
public class ParkAuto extends LiveAutoBase {

    @Override
    public void on_init() {

    }

    @Override
    public void on_start() {
        robot.shooter.spin();

        robot.drive_train.odo_move(0, 40, 0, 1.0);

        robot.shooter.aim(HIGH_GOAL);

        robot.drive_train.odo_move(-12, 62, 0, 1.0);

        for (int i = 0; i < 6; i++) {
            resetStartTime();

            robot.shooter.shoot();

            while (getRuntime() <= 1) {}

            robot.shooter.unshoot();

            while (getRuntime() <= 2) {}
        }

        robot.shooter.aim(LOW_GOAL);

        robot.drive_train.odo_move(-12, 72, 0, 1.0);
    }

    @Override
    public void on_stop() {

    }
}
