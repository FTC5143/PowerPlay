package org.firstinspires.ftc.teamcode.opmodes.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

@Autonomous(name = "Test", group = "autonomous")
public class Test extends LiveAutoBase {

    int pattern = 1;

    @Override
    public void on_init() {

    }

    @Override
    public void on_start() {

        robot.drive_train.odo_move(0,0,3.14,0.5);

    }

    @Override
    public void on_stop() {
        robot.sound_player.vineboom();
    }
}
