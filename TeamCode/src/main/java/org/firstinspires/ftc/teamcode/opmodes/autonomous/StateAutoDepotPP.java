package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.constants.AutonomousConst;
import org.firstinspires.ftc.teamcode.coyote.path.Path;
import org.firstinspires.ftc.teamcode.coyote.path.PathPoint;

@Autonomous(name="SA Depot PP", group="autonomous")
public class StateAutoDepotPP extends StateBaseAuto {
    @Override
    public void on_init() {
        if (color == AutonomousConst.BLUE) {
            robot.drive_train.odo_reset(0, 26, 0);
        } else if (color == AutonomousConst.RED) {
            robot.drive_train.odo_reset(0, -22, 0);
        }

        super.on_init();
    }

    @Override
    public void on_start() {

    }

    @Override
    public void on_stop() {

    }
}
