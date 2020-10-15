package org.firstinspires.ftc.teamcode.opmodes.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robots.LiveRobot;

import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.RED;

@Autonomous(name="Odo Hold", group="autonomous")
//@Disabled
public class OdoHold extends LinearOpMode {

    LiveRobot robot;

    int pattern;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new LiveRobot(this);
        robot.startup();

        int color = RED;

        robot.drive_train.color = color;

        waitForStart();

        // Hold position at 0, 0 forever
        robot.drive_train.odo_move(0.01, 0, 0, 1, -1, -1);

        robot.shutdown();
    }
}
