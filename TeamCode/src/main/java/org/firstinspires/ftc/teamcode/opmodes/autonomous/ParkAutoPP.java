package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.coyote.geometry.Pose;
import org.firstinspires.ftc.teamcode.coyote.path.Path;
import org.firstinspires.ftc.teamcode.coyote.path.PathPoint;
import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.HIGH_GOAL;
import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.LOW_GOAL;

@Autonomous(name = "ParkAutoPurePursuit", group = "autonomous")
public class ParkAutoPP extends LiveAutoBase {

    int pattern = 1;

    @Override
    public void on_init() {
        robot.phone_camera.start_streaming();

        while (!isStarted()) {
            pattern = robot.phone_camera.get_pattern();
        }
    }

    @Override
    public void on_start() {
        robot.shooter.spin();
        robot.shooter.aim(HIGH_GOAL);

        Path path = new Path()
                .addPoint(new PathPoint(0, 40))
                .addPoint(new PathPoint(-10, 50))
                .addPoint(new PathPoint(-10, 62));
        robot.drive_train.follow_curve_path(path);

        for (int i = 0; i < 4; i++) {
            resetStartTime();

            robot.shooter.shoot();

            while (getRuntime() <= 0.5) {}

            robot.shooter.unshoot();

            while (getRuntime() <= 1) {}
        }

        robot.shooter.aim(LOW_GOAL);

        path = new Path();

        if (pattern == 1) {
            path.addPoint(new PathPoint(4, 74));
        } else if (pattern == 2) {
            path.addPoint(new PathPoint(-19, 97));
        } else if (pattern == 3) {
            path.addPoint(new PathPoint(4, 120));
        }

        robot.drive_train.follow_curve_path(path);


        path = new Path()
                .addPoint(new PathPoint(-10, 72))
                .reverse()
                .headingPrecision(-1)
                .positionPrecision(-1);
        robot.drive_train.follow_curve_path(path);

    }

    @Override
    public void on_stop() {

    }
}
