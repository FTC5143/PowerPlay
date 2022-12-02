package org.firstinspires.ftc.teamcode.opmodes.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

// Two cone auto starting in a blue corner
@Autonomous(name = "Blue Corners", group = "autonomous")
public class BlueCorners extends LiveAutoBase {

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

        // Drop first cone
        robot.lift.elevate_to(1);
        robot.drive_train.odo_move(16.5,6,0,0.4);
        sleep(250);
        robot.lift.open_claw();
        sleep(500);

        // Grab second cone
        robot.drive_train.odo_move(16.5, 2, 0, 0.4);
        sleep(500);
        robot.lift.cone_level(4);
        robot.drive_train.odo_move(52,0,3.142,0.4);
        sleep(150);
        robot.drive_train.odo_move(52,-28,3.142,0.4);
        sleep(500);
        robot.lift.close_claw();

        // Place second cone
        robot.lift.elevate_to(3);
        robot.drive_train.odo_move(52,0,0,0.4);
        robot.drive_train.odo_move(66,18,0,0.4);
        sleep(250);
        robot.lift.open_claw();
        sleep(500);

        // Move to park and reset robot
        robot.drive_train.odo_move(52,24,0,0.4);

        if (pattern == 2) {
            robot.drive_train.odo_move(52,0,0,0.4);
        }
        else if (pattern == 3) {
            robot.drive_train.odo_move(52,-24,0,0.4);
        }
        sleep(30000);
    }

    @Override
    public void on_stop() {
        robot.sound_player.vineboom();

    }
}
