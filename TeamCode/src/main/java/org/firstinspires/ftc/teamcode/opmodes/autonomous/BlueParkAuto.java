package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;
import org.firstinspires.ftc.teamcode.robots.LiveRobot;

import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.RED;

@Autonomous(name="FF B Park", group="autonomous")
//@Disabled
public class BlueParkAuto extends LiveAutoBase {
    @Override
    public void on_init() {

    }

    @Override
    public void on_start() {
        robot.drive_train.odo_move(24, 28, 0, 1, -1, -1, 5);
    }

    @Override
    public void on_stop() {

    }
}
