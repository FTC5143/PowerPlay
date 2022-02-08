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
        goto_shipping_hub(0.5, 5);

        deposit_freight(pattern);

        robot.intake.spin(-1);

        if (duck) {
            if (color == AutonomousConst.RED) {
                robot.drive_train.odo_move(16, 54, -Math.PI / 4, 0.4, -1, -1, 5);
                robot.wheeler.spin(-1);
            }
            else if (color == AutonomousConst.BLUE) {
                robot.drive_train.odo_move(-4, -70, -Math.PI/4, 0.4, -1, -1, 5);
                robot.wheeler.spin(1);
            }

            halt(5);

            robot.wheeler.spin(0);
        }

        robot.intake.spin(0);

        if (park) {
            park_depot(1, 5);
        }
    }

    @Override
    public void on_stop() {

    }
}
