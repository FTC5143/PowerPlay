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
        robot.drive_train.odo_move(1, 36, 0, 1, -1, -1, 5);
    }

    @Override
    public void on_stop() {

    }
}
