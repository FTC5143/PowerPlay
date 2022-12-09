package org.firstinspires.ftc.teamcode.opmodes.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

// New and improved red corner auto
@Autonomous(name = "New Red", group = "autonomous")
public class NewRed extends LiveAutoBase {

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

        // Place cone 1
        robot.lift.elevate_to(1);
        robot.drive_train.odo_move(41,7,0,0.6);
        robot.lift.open_claw();

        // Pick up cone 2
        robot.drive_train.odo_move(54,2,0,0.6);
        robot.lift.cone_level(4);
        robot.drive_train.odo_move(53,29,0,0.6);
        robot.lift.close_claw();

        // Place cone 2
        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(53,-12,-1.57,0.5);
        robot.drive_train.odo_move(60,-12,-1.57,0.4);
        robot.lift.open_claw();

        // Pick up cone 3
        robot.drive_train.odo_move(53,-12,-1.57,0.4);
        robot.lift.cone_level(3);
        robot.drive_train.odo_move(53,29,0,0.6);
        robot.lift.close_claw();

        // Place cone 3
        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(53,-12,-1.57,0.5);
        robot.drive_train.odo_move(60,-12,-1.57,0.4);
        robot.lift.open_claw();

        // Pick up cone 4
        robot.drive_train.odo_move(53,-12,-1.57,0.4);
        robot.lift.cone_level(2);
        robot.drive_train.odo_move(53,29,0,0.6);
        robot.lift.close_claw();

        // Place cone 4
        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(53,-12,-1.57,0.5);
        robot.drive_train.odo_move(60,-12,-1.57,0.4);
        robot.lift.open_claw();

        // Pick up cone 5
        robot.drive_train.odo_move(53,-12,-1.57,0.4);
        robot.lift.cone_level(1);
        robot.drive_train.odo_move(53,29,0,0.6);
        robot.lift.close_claw();

        // Place cone 5
        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(53,-12,-1.57,0.5);
        robot.drive_train.odo_move(60,-12,-1.57,0.4);
        robot.lift.open_claw();

        // Pick up cone 6
        robot.drive_train.odo_move(53,-12,-1.57,0.4);
        robot.lift.cone_level(0);
        robot.drive_train.odo_move(53,29,0,0.6);
        robot.lift.close_claw();

        // Place cone 6
        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(53,-12,-1.57,0.5);
        robot.drive_train.odo_move(60,-12,-1.57,0.4);
        robot.lift.open_claw();

        // Park in correct location
        robot.lift.elevate_to(0);
        robot.drive_train.odo_move(53,-12,-1.57,0.8);

        if (pattern == 1) {
            robot.drive_train.odo_move(53,26,0,1);
        }
        else if (pattern == 2) {
            robot.drive_train.odo_move(53,0,0,1);
        }
        else if (pattern == 3) {
            robot.drive_train.odo_move(53,-24,0,1);
        }
        sleep(30000);
    }

    @Override
    public void on_stop() {

    }
}
