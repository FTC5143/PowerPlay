package org.firstinspires.ftc.teamcode.components.live;

import android.graphics.Color;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.MathUtil;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.Arrays;
import java.util.Collections;

import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.BLUE;
import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.RED;
import static org.opencv.core.CvType.CV_8UC1;

@Config
class OCVPhoneCameraConfig {
}

public class OCVPhoneCamera extends Component {

    OpenCvCamera phone_camera;

    private boolean streaming;

    {
        name = "Phone Camera (OCV)";
    }

    public OCVPhoneCamera(Robot robot) {
        super(robot);
    }

    @Override
    public void registerHardware(HardwareMap hwmap) {
        int cameraMonitorViewId = hwmap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwmap.appContext.getPackageName());
        phone_camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
    }

    @Override
    public void startup() {
        super.startup();

        phone_camera.openCameraDevice();
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);
        telemetry.addData("FRAME", phone_camera.getFrameCount());
        telemetry.addData("FPS", String.format("%.2f", phone_camera.getFps()));
    }

    public void set_pipeline(OpenCvPipeline pl) {
        phone_camera.setPipeline(pl);
    }

    public void start_streaming() {
        if (!streaming) {
            phone_camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            streaming = true;
        }
    }

    public void stop_streaming() {
        phone_camera.stopStreaming();
        streaming = false;
    }
}