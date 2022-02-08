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
        goto_shipping_hub(0.5, 2);

        deposit_freight(pattern); // preload

        int i = 0;

        while (opModeIsActive() && i < 2) {
            if (color == AutonomousConst.BLUE) {
                robot.drive_train.follow_curve_path(
                    new Path()
                            .addPoint(new PathPoint(8, -8).addAction(() -> {
                                robot.intake.spin(1);
                            }))
                            .addPoint(new PathPoint(-1, 12*5))
                            .positionPrecision(5)
                            .headingPrecision(0.04)
                            .timeout(5)
                );

                robot.drive_train.follow_curve_path(
                    new Path()
                            .addPoint(new PathPoint(-3, 8).addAction(() -> {
                                robot.intake.spin(0);
                            }))
                            .addPoint(new PathPoint(31, 11))
                            .reverse()
                            .timeout(5)
                );
            }

            else if (color == AutonomousConst.RED) {
                robot.drive_train.follow_curve_path(
                    new Path()
                            .addPoint(new PathPoint(8, 8).addAction(() -> {
                                robot.intake.spin(1);
                            }))
                            .addPoint(new PathPoint(-1, -12*5))
                            .positionPrecision(5)
                            .headingPrecision(0.04)
                            .timeout(5)
                );

                robot.drive_train.follow_curve_path(
                    new Path()
                            .addPoint(new PathPoint(-3, 8).addAction(() -> {
                                robot.intake.spin(0);
                            }))
                            .addPoint(new PathPoint(31, 11))
                            .reverse()
                            .timeout(5)
                );
            }

            deposit_freight(3);

            i += 1;
        }

        if (color == AutonomousConst.BLUE) {
            robot.drive_train.follow_curve_path(
                new Path()
                        .addPoint(new PathPoint(0, 8))
                        .addPoint(new PathPoint(-1, 12*5))
            );
        }

        else if (color == AutonomousConst.RED) {
            robot.drive_train.follow_curve_path(
                new Path()
                        .addPoint(new PathPoint(0, 8))
                        .addPoint(new PathPoint(-1, -12*5))
            );
        }
    }

    @Override
    public void on_stop() {

    }
}
