package org.firstinspires.ftc.teamcode.opmodes.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

// New and improved and improved blue corner auto
@Autonomous(name = "Old Blue", group = "autonomous")
public class OldBlue extends LiveAutoBase {

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

        robot.drive_train.odo_move(66,0,0,0.5);

        sleep(400);

        robot.drive_train.odo_move(63.5,6,0,0.3);

        sleep(100);

        robot.lift.open_claw();

        sleep(800);

        robot.drive_train.odo_move(63.5,2,0,0.6);

        robot.lift.cone_level(4);

        robot.drive_train.odo_move(49,2,3.14,0.3);

        sleep(200);

        robot.drive_train.odo_move(49,-27,3.14,0.4,1,0.02,4);

        robot.lift.close_claw();

        sleep(300);

        robot.lift.elevate_to(3);

        robot.drive_train.odo_move(49,2,3.14,0.4);

        robot.drive_train.odo_move(63.5,2,0,0.3,1,0.01,3);

        robot.drive_train.odo_move(63.5,6,0,0.3,1,0.1,3);


        sleep(100);

        robot.lift.open_claw();

        sleep(800);

        robot.drive_train.odo_move(63.5,2,0,0.4);

        robot.lift.cone_level(3);

        robot.drive_train.odo_move(49,2,3.14,0.3,1,0.1);

        sleep(200);

        robot.drive_train.odo_move(49,-27,3.14,0.4,1,0.02,4);

        robot.lift.close_claw();

        sleep(300);

        robot.lift.elevate_to(3);

        robot.drive_train.odo_move(49,2,3.14,0.4);

        robot.drive_train.odo_move(63.5,2,0,0.3,1,0.1);

        robot.drive_train.odo_move(63.5,6,0,0.4);

        sleep(100);

        robot.lift.open_claw();

        sleep(800);

        robot.drive_train.odo_move(63.5,2,0,0.4);

        robot.lift.elevate_to(0);

        robot.drive_train.odo_move(49,2,0,0.4);

        // Park in correct location

        if (pattern == 1) {
            robot.drive_train.odo_move(49,20,0,1);
        }
        else if (pattern == 2) {
            robot.drive_train.odo_move(49,0,0,1);
        }
        else if (pattern == 3) {
            robot.drive_train.odo_move(49,-20,0,1);
        }
        sleep(30000);
    }

    @Override
    public void on_stop() {

    }
}
