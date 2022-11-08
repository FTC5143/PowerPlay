package org.firstinspires.ftc.teamcode.robots;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.live.DriveTrain;
import org.firstinspires.ftc.teamcode.coyote.geometry.Point;
import org.firstinspires.ftc.teamcode.util.DashboardUtil;

import java.util.ArrayList;

public class DebugRobot extends Robot {

    public DriveTrain       drive_train;

    FtcDashboard            dashboard;

    ArrayList<Point> robot_movement = new ArrayList<Point>();

    {
        name = "Taurus";
    }

    public DebugRobot(LinearOpMode opmode) {
        super(opmode);

        drive_train     = new DriveTrain(this);

        dashboard = FtcDashboard.getInstance();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void updateTelemetry() {
        super.updateTelemetry();

        TelemetryPacket packet = new TelemetryPacket();

        Canvas canvas = packet.fieldOverlay();

        int offset_x = -33;
        int offset_y = -63;
        double offset_a = Math.PI/2;

        DashboardUtil.drawRobot(canvas, new Pose2d(drive_train.lcs.x+offset_x, drive_train.lcs.y+offset_y, drive_train.lcs.a+offset_a));

        canvas.setStrokeWidth(1);
        canvas.setStroke("#0000ff");

        packet.put("x", drive_train.lcs.x);
        packet.put("y", drive_train.lcs.y);
        packet.put("a", drive_train.lcs.a);
        packet.put("le", drive_train.lcs.prev_le);
        packet.put("re", drive_train.lcs.prev_re);
        packet.put("ce", drive_train.lcs.prev_ce);
        packet.put("freq", update_freq);

        if (drive_train.current_path != null) {
            drive_train.current_path.dashboard_draw(canvas);
        }

        dashboard.sendTelemetryPacket(packet);
    }
}