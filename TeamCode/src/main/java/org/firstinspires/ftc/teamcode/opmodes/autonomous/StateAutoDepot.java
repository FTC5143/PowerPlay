package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.constants.AutonomousConst;

public class StateAutoDepot extends StateBaseAuto {
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
        goto_shipping_hub(0.5, 2);

        deposit_freight(pattern); // preload


        int i = 0;

        while (opModeIsActive() && i < 2) {
            if (color == AutonomousConst.BLUE) {
                robot.drive_train.odo_move(0, 8, 0, 1, 1, 0.02, 2);

                robot.intake.spin(1);

                robot.drive_train.odo_move(-6, 12 * 5, -0.05, 0.5, 5, 0.04, 2);

                robot.drive_train.odo_move(-6, 8, 0.05, 1, 5, 0.04, 2);
            }
            else if (color == AutonomousConst.RED) {
                robot.drive_train.odo_move(0, 8, -Math.PI, 1, 1, 0.02, 2);

                robot.intake.spin(1);

                robot.drive_train.odo_move(-6, -12 * 5, -Math.PI + 0.05, 0.5, 5, 0.04, 2);

                robot.drive_train.odo_move(-6, 8, -Math.PI - 0.05, 1, 5, 0.04, 2);
            }

            robot.intake.spin(0);

            goto_shipping_hub(0.5, 2);

            deposit_freight(3);

            i += 1;
        }

        if (color == AutonomousConst.BLUE) {
            robot.drive_train.odo_move(0, 8, 0, 1, 1, 0.02, 2);

            robot.drive_train.odo_move(-6, 12 * 5, -0.05, 0.5, 5, 0.04, 2);
        }
        else if (color == AutonomousConst.RED) {
            robot.drive_train.odo_move(0, 8, -Math.PI, 1, 1, 0.02, 2);

            robot.drive_train.odo_move(-6, -12 * 5, -Math.PI + 0.05, 0.5, 5, 0.04, 2);
        }
    }

    @Override
    public void on_stop() {

    }
}
