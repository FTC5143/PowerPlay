package org.firstinspires.ftc.teamcode.opmodes.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

// New and improved and improved blue corner auto
@Autonomous(name = "One Blue", group = "autonomous")
public class OneBlue extends LiveAutoBase {

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

        // Place cones until park
        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(63.5,0,0,0.5);
        robot.drive_train.odo_move(63.5,7,0,0.3);
        sleep(200);
        robot.lift.elevate_to(0);
        robot.lift.open_claw();
        sleep(600);
        robot.drive_train.odo_move(63.5,0,0,0.5);
        robot.drive_train.odo_move(51.5,0,0,0.5);


        // Park in correct location
        if (pattern == 1) {
            robot.drive_train.odo_move(51.5,18,0,1);
        }
        else if (pattern == 2) {
            robot.drive_train.odo_move(51.5,0,0,1);
        }
        else if (pattern == 3) {
            robot.drive_train.odo_move(51.5,-18,0,1);
        }
        sleep(30000);
    }

    @Override
    public void on_stop() {

    }
}
