package org.firstinspires.ftc.teamcode.robots;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.components.live.DriveTrain;
import org.firstinspires.ftc.teamcode.components.live.Intake;
import org.firstinspires.ftc.teamcode.components.live.OCVPhoneCamera;
import org.firstinspires.ftc.teamcode.components.live.Shooter;
import org.firstinspires.ftc.teamcode.coyote.geometry.Point;
import org.firstinspires.ftc.teamcode.util.DashboardUtil;

import java.util.ArrayList;

public class LiveRobot extends Robot {

    public DriveTrain       drive_train;
    public Shooter          shooter;
    public Intake           intake;
    public OCVPhoneCamera   phone_camera;

    FtcDashboard            dashboard;

    ArrayList<Point> robot_movement = new ArrayList<Point>();

    {
        name = "Boris";
    }

    public LiveRobot(LinearOpMode opmode) {
        super(opmode);

        drive_train     = new DriveTrain(this);
        shooter         = new Shooter(this);
        intake          = new Intake(this);
        phone_camera    = new OCVPhoneCamera(this);

        dashboard = FtcDashboard.getInstance();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void updateTelemetry() {
        super.updateTelemetry();

        telemetry.addData("DIGITAL",
                "D10:"+(bulk_data_1.getDigitalInputState(0) ? 1 : 0)+
                        " D11:"+(bulk_data_1.getDigitalInputState(1) ? 1 : 0)+
                        " D12:"+(bulk_data_1.getDigitalInputState(2) ? 1 : 0)+
                        " D13:"+(bulk_data_1.getDigitalInputState(3) ? 1 : 0)+
                        " D14:"+(bulk_data_1.getDigitalInputState(4) ? 1 : 0)+
                        " D15:"+(bulk_data_1.getDigitalInputState(5) ? 1 : 0)+
                        " D16:"+(bulk_data_1.getDigitalInputState(6) ? 1 : 0)+
                        " D17:"+(bulk_data_1.getDigitalInputState(7) ? 1 : 0)+
                        " D20:"+(bulk_data_2.getDigitalInputState(0) ? 1 : 0)+
                        " D21:"+(bulk_data_2.getDigitalInputState(1) ? 1 : 0)+
                        " D22:"+(bulk_data_2.getDigitalInputState(2) ? 1 : 0)+
                        " D23:"+(bulk_data_2.getDigitalInputState(3) ? 1 : 0)+
                        " D24:"+(bulk_data_2.getDigitalInputState(4) ? 1 : 0)+
                        " D25:"+(bulk_data_2.getDigitalInputState(5) ? 1 : 0)+
                        " D26:"+(bulk_data_2.getDigitalInputState(6) ? 1 : 0)+
                        " D27:"+(bulk_data_2.getDigitalInputState(7) ? 1 : 0)
        );

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

        packet.put("flyvel", bulk_data_2.getMotorVelocity(0));

        if (drive_train.current_path != null) {
            drive_train.current_path.dashboard_draw(canvas);
        }

        dashboard.sendTelemetryPacket(packet);
    }
}