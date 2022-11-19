package org.firstinspires.ftc.teamcode.opmodes.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

@Autonomous(name = "Red Corners", group = "autonomous")
public class RedCorners extends LiveAutoBase {

    int pattern = 1;

    @Override
    public void on_init() {
        robot.lift.close_claw();
        robot.phone_camera.start_streaming();
        while (!isStarted() && !isStopRequested()) {
            pattern = robot.phone_camera.get_randomization_pattern();
        }
        robot.phone_camera.stop_streaming();
    }

    @Override
    public void on_start() {
        robot.lift.elevate_to(1);
        robot.drive_train.odo_move(46,0,0,0.4);
        robot.drive_train.odo_move(40.5,6,0,0.25);
        robot.lift.open_claw();
        sleep(500);
        robot.drive_train.odo_move(52,0,0,0.4);
        robot.lift.cone_level(4);
        robot.drive_train.odo_move(52,28,0,0.4);
        robot.lift.close_claw();
        sleep(500);
        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(52,-24,0,0.4);
        robot.drive_train.odo_move(66,-18,0,0.4);
        sleep(250);
        robot.lift.open_claw();
        sleep(500);
        robot.drive_train.odo_move(66, -20, 0, 0.4);
        robot.lift.elevate_to(0);
        robot.drive_train.odo_move(52,-24,0,0.4);

        if (pattern == 1) {
            robot.drive_train.odo_move(52,24,0,0.4);
        }
        else if (pattern == 2) {
            robot.drive_train.odo_move(52,0,0,0.4);
        }
        sleep(30000);
    }

    @Override
    public void on_stop() {

    }
}
