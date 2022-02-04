package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.constants.AutonomousConst;
import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

public class PlaceBlockParkSquareAuto extends LiveAutoBase {

    int color = AutonomousConst.RED;
    int side = AutonomousConst.LEFT;
    boolean duck = false;

    int pattern = 1;

    @Override
    public void on_init() {
        if (side == AutonomousConst.LEFT) {
            robot.drive_train.odo_reset(0, 22, 0);
        } else if (side == AutonomousConst.RIGHT) {
            robot.drive_train.odo_reset(0, -26, 0);
        }

        robot.phone_camera.start_streaming();

        while (!isStarted() && !isStopRequested()) {
            pattern = robot.phone_camera.get_randomization_pattern();
        }

        robot.phone_camera.stop_streaming();
    }

    @Override
    public void on_start() {
        robot.intake.cradle_intake();

        if (side == AutonomousConst.LEFT && color == AutonomousConst.RED) {
            robot.drive_train.odo_move(40, 19, 0, 0.5, -1, -1, 5);
        } else {
            robot.drive_train.odo_move(32, 8, -Math.PI/2, 0.3, -1, -1, 5);
        }

        robot.intake.cradle_lift();

        robot.lift.elevate_to(pattern);

        halt(2);

        if (robot.lift.level == 2) {
            robot.intake.cradle_half_dump();
        } else {
            robot.intake.cradle_dump();
        }

        halt(2);

        robot.intake.cradle_intake();
        robot.lift.min_lift();

        robot.intake.spin(-1);

        if (duck) {
            if (color == AutonomousConst.RED) {
                if (side == AutonomousConst.LEFT) {
                    robot.drive_train.odo_move(16, 54, -Math.PI / 4, 0.4, -1, -1, 5);
                } else if (side == AutonomousConst.RIGHT) {
                    robot.drive_train.odo_move(16, 54, -Math.PI / 4, 0.4, -1, -1, 5);
                }
                robot.wheeler.spin(-1);
            }

            else if (color == AutonomousConst.BLUE) {
                // Todo, correctly position
                robot.drive_train.odo_move(-4, -70, -Math.PI/4, 0.4, -1, -1, 5);
                robot.wheeler.spin(1);
            }

            halt(5);

            robot.wheeler.spin(0);
        }

        robot.intake.spin(0);

        if (color == AutonomousConst.RED) {
            robot.drive_train.odo_move(38, 58, -Math.PI/2, 0.5, -1, -1, 5);
        } else if (color == AutonomousConst.BLUE) {
            robot.drive_train.odo_move(34, -40, -Math.PI/2, 0.5, -1, -1, 5);
        }
    }

    @Override
    public void on_stop() {
    }
}
