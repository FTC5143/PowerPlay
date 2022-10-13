package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.constants.AutonomousConst;
import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

public abstract class StateBaseAuto extends LiveAutoBase {

    int color = AutonomousConst.RED;
    int pattern = 1;
    boolean park = true;

    @Override
    public void on_init() {
        robot.phone_camera.start_streaming();

        while (!isStarted() && !isStopRequested()) {
            pattern = robot.phone_camera.get_randomization_pattern();
        }

        robot.phone_camera.stop_streaming();
    }


}
