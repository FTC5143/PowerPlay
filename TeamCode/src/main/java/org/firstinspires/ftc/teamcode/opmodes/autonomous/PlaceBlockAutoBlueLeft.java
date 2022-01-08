package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

@Autonomous(name="FF B L Block Top", group="autonomous")
//@Disabled
public class PlaceBlockAutoBlueLeft extends LiveAutoBase {
    @Override
    public void on_init() {

    }

    @Override
    public void on_start() {
        robot.intake.cradle_intake();
        robot.drive_train.odo_move(-24, -18, 0, 0.5, -1, -1, 4);

        robot.intake.cradle_lift();
        robot.lift.elevate_to(4);

        halt(2);

        robot.intake.cradle_dump();

        halt(2);

        robot.intake.cradle_intake();
        robot.lift.min_lift();

        robot.drive_train.odo_move(-24 * 3, -24, 0, 1, -1, -1, 5);
    }

    @Override
    public void on_stop() {

    }
}
