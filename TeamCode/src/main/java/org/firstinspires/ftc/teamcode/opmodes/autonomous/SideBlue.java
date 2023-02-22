package org.firstinspires.ftc.teamcode.opmodes.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

// New and improved and improved blue corner auto
@Autonomous(name = "Side Blue", group = "autonomous")
public class SideBlue extends LiveAutoBase {

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
        robot.drive_train.odo_move(51.5,-0,0,0.6);
        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(63.5,0,0,0.4);
        robot.drive_train.odo_move(63.5,7,0,0.3);
        robot.lift.cone_level(4);
        robot.lift.open_claw();
        sleep(600);

        robot.drive_train.odo_move(63.5,0,0,0.5);
        robot.drive_train.odo_move(51.5,0,0,0.5);
        robot.drive_train.odo_move(51.5,0,3.14,0.8);
        robot.drive_train.odo_move(51.5,29,3.14,0.35,1,0.05,3);
        robot.lift.close_claw();
        sleep(400);

        robot.lift.elevate_to(1);
        robot.drive_train.odo_move(51.5,0,3.14,0.4);
        robot.drive_train.odo_move(49.5,2,1.57,0.8);
        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(49.5,14,1.57,0.4);
        robot.drive_train.odo_move(56.5,14,1.57,0.3);
        robot.lift.cone_level(3);
        robot.lift.open_claw();
        sleep(600);

        robot.drive_train.odo_move(49.5,14,1.57,0.5);
        robot.drive_train.odo_move(51.5,0,1.57,0.5);
        robot.drive_train.odo_move(51.5,0,3.14,0.8);
        robot.drive_train.odo_move(51.5,29,3.14,0.35,1,0.05,3);
        robot.lift.close_claw();
        sleep(400);

        robot.lift.elevate_to(1);
        robot.drive_train.odo_move(51.5,0,3.14,0.4);
        robot.drive_train.odo_move(49.5,2,1.57,0.8);
        robot.lift.elevate_to(2);
        robot.drive_train.odo_move(49.5,14,1.57,0.4);
        robot.drive_train.odo_move(56.5,14,1.57,0.3);
        robot.lift.cone_level(3);
        robot.lift.open_claw();
        sleep(600);

        robot.drive_train.odo_move(49.5,-11,1.57,0.5);
        robot.lift.elevate_to(0);

        // Park in correct location
        if (pattern == 1) {
            robot.drive_train.odo_move(49.5,18,1.57,1);
        }
        else if (pattern == 2) {
            robot.drive_train.odo_move(49.5,0,1.57,1);
        }
        else if (pattern == 3) {
            robot.drive_train.odo_move(49.5,-18,1.57,1);
        }
        sleep(30000);
    }

    @Override
    public void on_stop() {

    }
}
