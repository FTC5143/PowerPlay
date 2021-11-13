package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;
import org.firstinspires.ftc.teamcode.robots.LiveRobot;

@Autonomous(name="FF Duck", group="autonomous")
//@Disabled
public class DuckAuto extends LiveAutoBase {
    @Override
    public void on_init() {

    }

    @Override
    public void on_start() {
        robot.drive_train.odo_move(8, -24, 0, 1, 1, 0.02, 5);

        robot.wheeler.spin(-1);

        halt(10);

        robot.wheeler.spin(0);
    }

    @Override
    public void on_stop() {

    }
}
