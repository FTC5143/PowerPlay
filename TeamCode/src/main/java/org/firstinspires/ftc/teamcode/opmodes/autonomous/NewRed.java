package org.firstinspires.ftc.teamcode.opmodes.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

// New and improved red corner auto
@Autonomous(name = "New Red", group = "autonomous")
public class NewRed extends LiveAutoBase {

    int pattern = 1;
    ElapsedTime timer;

    @Override
    public void on_init() {
        timer = new ElapsedTime();
        robot.lift.close_claw();
        robot.phone_camera.start_streaming();
        while (!isStarted() && !isStopRequested()) {
            pattern = robot.phone_camera.get_randomization_pattern();
        }
        robot.phone_camera.stop_streaming();
    }

    @Override
    public void on_start() {

        // Place cone 1 and move signal sleeve
        robot.lift.elevate_to(1);

        robot.drive_train.odo_move(40.5, 0, 0, 0.5);

        robot.drive_train.odo_move(40.5, 7.5, 0, 0.5, 0.1, 0.02, 2);

        sleep(200);
        robot.lift.open_claw();
        sleep(300);

        robot.drive_train.odo_move(40.5, 0, 0, 0.5);

        robot.drive_train.odo_move(56.5, 3, 0, 0.5);

        robot.drive_train.odo_move(52, 6.5, 0, 0.5);

        robot.lift.cone_level(4);

        robot.drive_train.odo_move(52.5, 29.25, 0, 0.5, 0.1, 0.02, 2);

        sleep(200);
        robot.lift.close_claw();
        sleep(300);

        robot.lift.elevate_to(3);
        sleep(200);

        robot.drive_train.odo_move(51, -20, 0, 0.5);

        robot.drive_train.odo_move(62.5, -20, 0, 0.5);

        robot.drive_train.odo_move(62.5, -16, 0, 0.5, 0.1, 0.02, 2);

        sleep(200);
        robot.lift.open_claw();
        sleep(300);

        robot.drive_train.odo_move(62.5, -20, 0, 0.5);

        robot.drive_train.odo_move(52.5, -20, 0, 0.5);

        robot.lift.cone_level(3);

        robot.drive_train.odo_move(52.5, 29.25, 0,0.5, 0.1, 0.02, 3);

        sleep(200);
        robot.lift.close_claw();
        sleep(300);

        robot.lift.elevate_to(3);
        sleep(200);

        robot.drive_train.odo_move(51, -20, 0, 0.5);

        robot.drive_train.odo_move(62.5, -20, 0, 0.5);

        robot.drive_train.odo_move(62.5, -16, 0, 0.5, 0.1, 0.02, 2);

        sleep(200);
        robot.lift.open_claw();
        sleep(300);
;
        robot.drive_train.odo_move(63.5, -15, 0, 0.5);

        robot.drive_train.odo_move(51.5, -20, 0, 0.5);

        // Park in correct location
        robot.lift.elevate_to(0);

        if (pattern == 1) {
            robot.drive_train.odo_move(53,26,0,0.5);
        }
        else if (pattern == 2) {
            robot.drive_train.odo_move(53,0,0,0.5);
        }
        else if (pattern == 3) {
            robot.drive_train.odo_move(53,-24,0,0.5);
        }
        sleep(30000);
    }

    @Override
    public void on_stop() {

    }
}
