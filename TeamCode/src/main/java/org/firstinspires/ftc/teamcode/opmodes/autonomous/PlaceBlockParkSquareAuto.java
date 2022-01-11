package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.constants.AutonomousConst;
import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

public class PlaceBlockParkSquareAuto extends LiveAutoBase {

    int color = AutonomousConst.RED;
    int side = AutonomousConst.LEFT;

    int pattern = 1;

    @Override
    public void on_init() {
        if (side == AutonomousConst.LEFT) {
            robot.drive_train.odo_reset(24, 0, 0);
        } else if (side == AutonomousConst.RIGHT) {
            robot.drive_train.odo_reset(-24, 0, 0);
        }

        robot.phone_camera.start_streaming();

        while (!isStarted() && !isStopRequested()) {
            pattern = robot.phone_camera.get_randomization_pattern();
        }
    }

    @Override
    public void on_start() {
        robot.intake.cradle_intake();

        robot.drive_train.odo_move(0, -20, 0, 0.5, -1, -1, 4);

        robot.intake.cradle_lift();

        robot.lift.elevate_to(pattern);

        halt(2);

        robot.intake.cradle_dump();

        halt(2);

        robot.intake.cradle_intake();
        robot.lift.min_lift();

        if (color == AutonomousConst.RED) {
            robot.drive_train.odo_move(24 * 2, -24, 0, 1, -1, -1, 5);
        } else if (color == AutonomousConst.BLUE) {
            robot.drive_train.odo_move(-24 * 2, -24, 0, 1, -1, -1, 5);
        }
    }

    @Override
    public void on_stop() {
        robot.sound_player.vineboom();
    }
}
