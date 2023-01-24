package org.firstinspires.ftc.teamcode.opmodes.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

// New and improved blue corner auto
@Autonomous(name = "New Blue", group = "autonomous")
public class NewBlue extends LiveAutoBase {

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
        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(65,0,0,0.5);
        robot.drive_train.odo_move(65,7,0,0.3);
        robot.lift.open_claw();
        sleep(200);
        robot.drive_train.odo_move(65,0,0,0.6);

        for (int cone = 4; cone >= 0; cone--) {
            // Pick up cone
            robot.drive_train.odo_move(53,-2,-3.14,0.6);
            robot.lift.cone_level(cone);
            robot.drive_train.odo_move(53,-26,-3.14,0.6);
            robot.drive_train.odo_move(53,-29,-3.14,0.4);
            robot.lift.close_claw();

            // Place cone
            sleep(100);
            robot.lift.elevate_to(3);
            sleep(200);
            robot.drive_train.odo_move(53,0,-0.78,0.5);
            robot.drive_train.odo_move(61.46,8.46,-0.78,0.3);
            robot.lift.open_claw();
            sleep(200);
            robot.drive_train.odo_move(58,5,-0.78,0.6);

            if (timer.seconds()>=23) {
                break;
            }
        }

        // Park in correct location
        robot.lift.elevate_to(0);

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
