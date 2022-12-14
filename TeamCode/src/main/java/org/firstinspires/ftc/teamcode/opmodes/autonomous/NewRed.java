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

        timer.reset();

        // Place cone 1 and move signal sleeve
        robot.lift.elevate_to(1);
        robot.drive_train.odo_move(40.5,0,0,0.8);
        robot.drive_train.odo_move(40.5,7,0,0.6);
        robot.lift.open_claw();
        sleep(200);
        robot.drive_train.odo_move(40.5,0,0,0.8);
        robot.drive_train.odo_move(60,0,0,1);

        for (int cone = 4; cone >= 0; cone--) {
            // Pick up cone
            robot.drive_train.odo_move(53,2,0,0.8);
            robot.lift.cone_level(cone);
            robot.drive_train.odo_move(53,26,0,0.8);
            robot.drive_train.odo_move(53,29,0,0.5);
            robot.lift.close_claw();

            // Place cone
            sleep(100);
            robot.lift.elevate_to(3);
            sleep(200);
            robot.drive_train.odo_move(53,0,-2.36,0.6);
            robot.drive_train.odo_move(61.46,-8.46,-2.36,0.4);
            robot.lift.open_claw();
            sleep(200);
            robot.drive_train.odo_move(58,-5,-2.36,0.8);

            if (timer.seconds()>=23) {
                break;
            }
        }

        // Park in correct location
        robot.lift.elevate_to(0);

        if (pattern == 1) {
            robot.drive_train.odo_move(53,26,-3.14,1);
        }
        else if (pattern == 2) {
            robot.drive_train.odo_move(53,0,-3.14,1);
        }
        else if (pattern == 3) {
            robot.drive_train.odo_move(53,-24,-3.14,1);
        }
        sleep(30000);
    }

    @Override
    public void on_stop() {

    }
}
