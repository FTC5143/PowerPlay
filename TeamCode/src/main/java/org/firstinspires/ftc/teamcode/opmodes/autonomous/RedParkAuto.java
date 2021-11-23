package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

@Autonomous(name="FF R Park", group="autonomous")
//@Disabled
public class RedParkAuto extends LiveAutoBase {
    @Override
    public void on_init() {

    }

    @Override
    public void on_start() {
        robot.lift.elevate_to(0);
        robot.lift.tweak(1);
        robot.drive_train.odo_move(-24, 28, 0, 1, -1, -1, 5);
        robot.lift.tweak(0);
    }

    @Override
    public void on_stop() {

    }
}
