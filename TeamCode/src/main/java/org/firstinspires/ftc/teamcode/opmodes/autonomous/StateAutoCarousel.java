package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.constants.AutonomousConst;

public class StateAutoCarousel extends StateBaseAuto {

    boolean duck = true;

    @Override
    public void on_init() {
        if (color == AutonomousConst.BLUE) {
            robot.drive_train.odo_reset(0, -26, 0);
        } else if (color == AutonomousConst.RED) {
            robot.drive_train.odo_reset(0, 22, 0);
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
