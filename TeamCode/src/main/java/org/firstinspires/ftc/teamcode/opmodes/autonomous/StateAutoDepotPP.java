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
        int i = 0;

        while (opModeIsActive() && i < 100) {
            final int level = i == 0 ? pattern : 3;

            if (color == AutonomousConst.BLUE) {

            }

            else if (color == AutonomousConst.RED) {
                final Path intake_path = new Path();
                intake_path
                        .addPoint(new PathPoint(31, 11))
                        .addPoint(new PathPoint(5, 0).addAction(() -> {
                            robot.intake.spin(1);
                            robot.intake.cradle_intake();
                        }))
                        .addPoint(new PathPoint(-4, -12*2).addAction(() -> {
                            intake_path.speed(0.5);
                        }))
                        .addPoint(new PathPoint(0, -12*4))
                        .positionPrecision(5)
                        .headingPrecision(0.04)
                        .constantHeading(-Math.PI/2)
                        .followRadius(18)
                        .speed(1)
                        .until(() -> robot.intake.has_element());


                final Path deposit_path = new Path();

                if (i == 0) {
                    deposit_path.headingMethod(Path.HeadingMethod.AWAY_FROM_PATH_END);
                }
                else {
                    deposit_path.addPoint(new PathPoint(-3, -12*2));
                }

                deposit_path
                        .addPoint(new PathPoint(0, 8).addAction(() -> {
                            robot.intake.spin(0);
                            robot.intake.cradle_lift();
                            robot.lift.elevate_to(level);
                            deposit_path.speed(0.5);
                        }))
                        .addPoint(new PathPoint(10, 11).addAction(() -> {
                            robot.intake.spin(-1);
                        }))
                        .addPoint(new PathPoint(31, 11))
                        .reverse()
                        .positionPrecision(2)
                        .followRadius(18)
                        .speed(1)
                        .timeout(5);


                if (i != 0) {
                    robot.intake.enable_color_sensor(true);

                    robot.drive_train.follow_curve_path(intake_path);

                    robot.intake.enable_color_sensor(false);

                    robot.intake.spin(0);
                    robot.intake.cradle_move();
                }

                robot.drive_train.follow_curve_path(deposit_path);
            }

            dump_freight(level);
            robot.lift.min_lift();
            robot.intake.spin(0);

            i += 1;
        }
    }

    @Override
    public void on_stop() {

    }
}
