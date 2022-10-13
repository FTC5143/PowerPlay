package org.firstinspires.ftc.teamcode.robots;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.live.DriveTrain;
import org.firstinspires.ftc.teamcode.components.live.Lift;
import org.firstinspires.ftc.teamcode.components.live.OCVPhoneCamera;
import org.firstinspires.ftc.teamcode.components.live.SoundPlayer;
import org.firstinspires.ftc.teamcode.coyote.geometry.Point;

import java.util.ArrayList;

public class LiveRobot extends Robot {

    public DriveTrain       drive_train;
    public OCVPhoneCamera   phone_camera;
    public Lift             lift;
    public SoundPlayer      sound_player;

    FtcDashboard            dashboard;

    ArrayList<Point> robot_movement = new ArrayList<Point>();

    {
        name = "Boris";
    }

    public LiveRobot(LinearOpMode opmode) {
        super(opmode);

        drive_train     = new DriveTrain(this);
        phone_camera    = new OCVPhoneCamera(this);
        lift            = new Lift(this);
        sound_player    = new SoundPlayer(this);

        dashboard = FtcDashboard.getInstance();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void updateTelemetry() {
        super.updateTelemetry();
    }
}