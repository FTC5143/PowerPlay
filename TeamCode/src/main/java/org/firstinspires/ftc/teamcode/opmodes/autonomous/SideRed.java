package org.firstinspires.ftc.teamcode.opmodes.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

// New and improved and improved blue corner auto
@Autonomous(name = "Side Red", group = "autonomous")
public class SideRed extends LiveAutoBase {

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
        robot.drive_train.odo_move(51.5,-1,0,0.6,1,0.05);
        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(51.5,-11,1.57,0.4,1,0.05);
        robot.drive_train.odo_move(56.5,-11,1.57,0.3,1,0.05);
        sleep(600);
        robot.lift.cone_level(4);
        robot.lift.open_claw();
        sleep(400);

        robot.drive_train.odo_move(52.5,-11,1.57,0.5,1,0.05);
        robot.drive_train.odo_move(52.5,24,0,0.5,1,0.05);
        robot.drive_train.odo_move(52.5,29,0,0.3,1,0.05,4);
        sleep(600);
        robot.lift.close_claw();
        sleep(400);

        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(52.5,24,0,0.4,1,0.05);
        robot.drive_train.odo_move(52.5,-11,1.57,0.4,1,0.05);
        robot.drive_train.odo_move(57.5,-11,1.57,0.3,1,0.05);
        sleep(600);
        robot.lift.cone_level(3);
        robot.lift.open_claw();
        sleep(400);

        robot.drive_train.odo_move(53.5,-11,1.57,0.5,1,0.05);
        robot.drive_train.odo_move(53.5,24,0,0.5,1,0.05);
        robot.drive_train.odo_move(53.5,29,0,0.3,1,0.05,4);
        sleep(600);
        robot.lift.close_claw();
        sleep(400);

        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(53.5,24,0,0.4,1,0.05);
        robot.drive_train.odo_move(53.5,-11,1.57,0.4,1,0.05);
        robot.drive_train.odo_move(58.5,-11,1.57,0.3,1,0.05);
        sleep(600);
        robot.lift.cone_level(2);
        robot.lift.open_claw();
        sleep(400);

        robot.drive_train.odo_move(54.5,-11,1.57,0.5,1,0.05);
        robot.drive_train.odo_move(54.5,24,0,0.5,1,0.05);
        robot.drive_train.odo_move(54.5,29,0,0.3,1,0.05,4);
        sleep(600);
        robot.lift.close_claw();
        sleep(400);

        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(54.5,24,0,0.4,1,0.05);
        robot.drive_train.odo_move(54.5,-11,1.57,0.4,1,0.05);
        robot.drive_train.odo_move(59.5,-11,1.57,0.3,1,0.05);
        sleep(600);
        robot.lift.elevate_to(0);
        robot.lift.open_claw();
        sleep(400);

        robot.drive_train.odo_move(54.5,-11,1.57,0.5,1,0.05);
        robot.lift.elevate_to(0);

        // Park in correct location
        if (pattern == 1) {
            robot.drive_train.odo_move(54.5,18,1.57,1);
        }
        else if (pattern == 2) {
            robot.drive_train.odo_move(54.5,0,1.57,1);
        }
        else if (pattern == 3) {
            robot.drive_train.odo_move(54.5,-18,1.57,1);
        }
        sleep(30000);
    }

    @Override
    public void on_stop() {

    }
}
