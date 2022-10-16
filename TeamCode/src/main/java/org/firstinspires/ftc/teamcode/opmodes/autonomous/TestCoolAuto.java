package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.coyote.path.Path;
import org.firstinspires.ftc.teamcode.coyote.path.PathPoint;
import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

@Autonomous(name = "Test Cool Auto", group = "autonomous")
public class TestCoolAuto extends LiveAutoBase {

    int pattern = 1;

    @Override
    public void on_init() {
        robot.phone_camera.start_streaming();
        while (!isStarted() && !isStopRequested()) {
            pattern = robot.phone_camera.get_randomization_pattern();
        }
        robot.phone_camera.stop_streaming();
    }

    @Override
    public void on_start() {

        Path uno_path = new Path();
        uno_path.addPoint(new PathPoint(0,0))
                .addPoint(new PathPoint(72,24).addAction(() -> {
                    robot.sound_player.skypecall();
                })) //LAMBDA EXPRESSION!!!1!!1! here and here only

                .addPoint(new PathPoint(48,36))
                .speed(.5);//<---- don forgor semicolonoscopy :skull_crossbones:

        robot.drive_train.follow_curve_path(uno_path);
    }

    @Override
    public void on_stop() {

    }
}
