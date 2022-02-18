package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.constants.AutonomousConst;
import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

public abstract class StateBaseAuto extends LiveAutoBase {

    int color = AutonomousConst.RED;
    int pattern = 1;
    boolean park = true;

    @Override
    public void on_init() {
        robot.phone_camera.start_streaming();

        while (!isStarted() && !isStopRequested()) {
            pattern = robot.phone_camera.get_randomization_pattern();
        }

        robot.phone_camera.stop_streaming();
    }

    public void deposit_freight(int level) {
        robot.intake.cradle_lift();

        robot.lift.elevate_to(level);

        halt(1);

        dump_freight(level);

        robot.lift.min_lift();
    }

    public void dump_freight(int level) {
        if (robot.lift.level == 2) {
            robot.intake.cradle_half_dump();
        } else {
            robot.intake.cradle_dump();
        }

        if (level == 2) {
            halt(2);
        } else {
            halt(1);
        }

        robot.intake.cradle_move();
    }

    public void goto_shipping_hub(double speed, double timeout) {
        robot.drive_train.odo_move(31, 11, -Math.PI/2, speed, -1, -1, timeout);
    }

    public void park_depot(double speed, double timeout) {
        if (color == AutonomousConst.RED) {
            robot.drive_train.odo_move(36, 58, -Math.PI / 2, speed, -1, -1, timeout);
        } else if (color == AutonomousConst.BLUE) {
            robot.drive_train.odo_move(36, -40, -Math.PI / 2, speed, -1, -1, timeout);
        }
    }

    public void park_warehouse(double speed, double timeout) {

    }
}
