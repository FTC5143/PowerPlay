package org.firstinspires.ftc.teamcode.opmodes.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robots.DebugRobot;
import org.firstinspires.ftc.teamcode.robots.LiveRobot;

@Autonomous(name="Bob Ross", group="autonomous")
//@Disabled
public class BobRoss extends LinearOpMode {

    DebugRobot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new DebugRobot(this);
        robot.startup();

        waitForStart();

        robot.drive_train.odo_move(0, -2, 0, 1);

        robot.shutdown();
    }
}
