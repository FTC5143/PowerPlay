package org.firstinspires.ftc.teamcode.opmodes.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

@Autonomous(name = "Park", group = "autonomous")
public class Park extends LiveAutoBase {

    int pattern = 1;

    @Override
    public void on_init() {
        robot.phone_camera.start_streaming();
        while (!isStarted() && !isStopRequested()) {
            pattern = robot.phone_camera.get_randomization_pattern();
        }
        robot.phone_camera.stop_streaming();

        robot.lift.close_claw();
    }

    @Override
    public void on_start() {

        robot.drive_train.odo_move(30,0,0,0.4);
        if (pattern == 1) {
            robot.drive_train.odo_move(30,24,0,0.4);
        }
        else if (pattern == 3) {
            robot.drive_train.odo_move(30,-24,0,0.4);
        }
    }

    @Override
        public void on_stop() {
            robot.sound_player.vineboom();
    }
}
