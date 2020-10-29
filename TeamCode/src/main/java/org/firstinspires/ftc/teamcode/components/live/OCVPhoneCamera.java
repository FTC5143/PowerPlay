package org.firstinspires.ftc.teamcode.components.live;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;
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

import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.BLUE;
import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.RED;
import static org.opencv.core.CvType.CV_8UC1;

public class OCVPhoneCamera extends Component {

    OpenCvCamera phone_camera;

    SamplePipeline stone_pipeline;

    {
        name = "Phone Camera (OCV)";
    }

    int color = RED;

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

        stone_pipeline = new SamplePipeline();
        phone_camera.setPipeline(stone_pipeline);

        start_streaming();
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);
        telemetry.addData("FRAME", phone_camera.getFrameCount());
        telemetry.addData("FPS", String.format("%.2f", phone_camera.getFps()));
        telemetry.addData("TOP RECT", stone_pipeline.top_sat);
        telemetry.addData("BOT RECT", stone_pipeline.bot_sat);
        telemetry.addData("PATTERN", stone_pipeline.pattern);

    }

    public void start_streaming() {
        phone_camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
    }

    public int get_pattern() {
        return stone_pipeline.pattern;
    }

    public void stop_streaming() {
        phone_camera.stopStreaming();
    }

    class SamplePipeline extends OpenCvPipeline {
        int top_sat;
        int bot_sat;

        int pattern;

        @Override
        public Mat processFrame(Mat input) {

            input.convertTo(input, CV_8UC1, 1, 10);

            int[] top_rect = {
                    (int) (input.cols() * (20f / 100f)),
                    (int) (input.rows() * (20f / 100f)),
                    (int) (input.cols() * (65f / 100f)),
                    (int) (input.rows() * (80f / 100f))
            };

            int[] bot_rect = {
                    (int) (input.cols() * (65f / 100f)),
                    (int) (input.rows() * (20f / 100f)),
                    (int) (input.cols() * (80f / 100f)),
                    (int) (input.rows() * (80f / 100f))
            };

            Mat top_block = input.submat(top_rect[1], top_rect[3], top_rect[0], top_rect[2]);
            Mat bot_block = input.submat(bot_rect[1], bot_rect[3], bot_rect[0], bot_rect[2]);


            Scalar top_mean = Core.mean(top_block);
            Scalar bot_mean = Core.mean(bot_block);

            float[] top_hsv = new float[3];
            float[] bot_hsv = new float[3];

            Color.RGBToHSV((int) top_mean.val[0], (int) top_mean.val[1], (int) top_mean.val[2], top_hsv);
            Color.RGBToHSV((int) bot_mean.val[0], (int) bot_mean.val[1], (int) bot_mean.val[2], bot_hsv);

            top_sat = (int)(100.0 * top_hsv[1]);
            bot_sat = (int)(100.0 * bot_hsv[1]);

            if (bot_sat > 30) {
                if (top_sat > 30) {
                    pattern = 3;
                } else {
                    pattern = 2;
                }
            } else {
                pattern = 1;
            }

            Imgproc.rectangle(
                    input,
                    new Point(
                            top_rect[0],
                            top_rect[1]),

                    new Point(
                            top_rect[2],
                            top_rect[3]),
                    new Scalar(0, 255, 0), 2);

            Imgproc.rectangle(
                    input,
                    new Point(
                            bot_rect[0],
                            bot_rect[1]),

                    new Point(
                            bot_rect[2],
                            bot_rect[3]),
                    new Scalar(0, 0, 255), 2);

            return input;
        }
    }
}