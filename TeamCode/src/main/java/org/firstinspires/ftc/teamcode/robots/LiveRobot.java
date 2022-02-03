package org.firstinspires.ftc.teamcode.robots;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.components.live.Capper;
import org.firstinspires.ftc.teamcode.components.live.DriveTrain;
import org.firstinspires.ftc.teamcode.components.live.Intake;
import org.firstinspires.ftc.teamcode.components.live.Lift;
import org.firstinspires.ftc.teamcode.components.live.OCVPhoneCamera;
import org.firstinspires.ftc.teamcode.components.live.SoundPlayer;
import org.firstinspires.ftc.teamcode.components.live.Wheeler;
import org.firstinspires.ftc.teamcode.coyote.geometry.Point;
import org.firstinspires.ftc.teamcode.util.DashboardUtil;

import java.util.ArrayList;

public class LiveRobot extends Robot {

    public DriveTrain       drive_train;
    public OCVPhoneCamera   phone_camera;
    public Lift             lift;
    public Intake           intake;
    public Wheeler          wheeler;
    public Capper           capper;
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
        intake          = new Intake(this);
        wheeler         = new Wheeler(this);
        capper          = new Capper(this);
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