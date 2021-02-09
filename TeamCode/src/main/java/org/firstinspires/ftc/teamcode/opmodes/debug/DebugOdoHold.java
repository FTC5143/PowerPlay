package org.firstinspires.ftc.teamcode.opmodes.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robots.DebugRobot;
import org.firstinspires.ftc.teamcode.robots.LiveRobot;

@Autonomous(name="Debug Odo Hold", group="autonomous")
//@Disabled
public class DebugOdoHold extends LinearOpMode {

    DebugRobot robot;

    int pattern;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new DebugRobot(this);
        robot.startup();

        waitForStart();

        // Hold position at 0, 0 forever
        robot.drive_train.odo_move(0.01, 0, 0, 1, -1, -1);

        robot.shutdown();
    }
}
