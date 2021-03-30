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
    public static double top_rect_left = 40;
    public static double top_rect_top = 6;
    public static double top_rect_width = 9;
    public static double top_rect_height = 15;

    public static double bot_rect_left = 37;
    public static double bot_rect_top = 6;
    public static double bot_rect_width = 3;
    public static double bot_rect_height = 15;
}

public class OCVPhoneCamera extends Component {

    OpenCvCamera phone_camera;

    public RingsPipeline rings_pipeline;
    public WobblePipeline wobble_pipeline;

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

        rings_pipeline = new RingsPipeline();
        wobble_pipeline = new WobblePipeline();

        set_pipeline(rings_pipeline);
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);
        telemetry.addData("FRAME", phone_camera.getFrameCount());
        telemetry.addData("FPS", String.format("%.2f", phone_camera.getFps()));
        telemetry.addData("TOP RECT", rings_pipeline.top_sat);
        telemetry.addData("BOT RECT", rings_pipeline.bot_sat);
        telemetry.addData("PATTERN", rings_pipeline.pattern);

        telemetry.addData("REDNESS", Arrays.toString(wobble_pipeline.redness));
        telemetry.addData("REDEST", wobble_pipeline.rectangle);
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

    public int get_pattern() {
        return rings_pipeline.pattern;
    }

    public int get_wobble_goal_pos() {
        return wobble_pipeline.rectangle;
    }

    public void stop_streaming() {
        phone_camera.stopStreaming();
        streaming = false;
    }

    class RingsPipeline extends OpenCvPipeline {
        int top_sat;
        int bot_sat;

        int pattern;

        @Override
        public Mat processFrame(Mat input) {

            input.convertTo(input, CV_8UC1, 1, 10);

            int[] top_rect = {
                    (int) (input.cols() * (OCVPhoneCameraConfig.top_rect_left / 100f)),
                    (int) (input.rows() * (OCVPhoneCameraConfig.top_rect_top / 100f)),
                    (int) (input.cols() * ((OCVPhoneCameraConfig.top_rect_left+OCVPhoneCameraConfig.top_rect_width) / 100f)),
                    (int) (input.rows() * ((OCVPhoneCameraConfig.top_rect_top+OCVPhoneCameraConfig.top_rect_height) / 100f))
            };

            int[] bot_rect = {
                    (int) (input.cols() * (OCVPhoneCameraConfig.bot_rect_left / 100f)),
                    (int) (input.rows() * (OCVPhoneCameraConfig.bot_rect_top / 100f)),
                    (int) (input.cols() * ((OCVPhoneCameraConfig.bot_rect_left+OCVPhoneCameraConfig.bot_rect_width) / 100f)),
                    (int) (input.rows() * ((OCVPhoneCameraConfig.bot_rect_top+OCVPhoneCameraConfig.bot_rect_height) / 100f))
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
                    new Scalar(0, 255, 0), 1);

            Imgproc.rectangle(
                    input,
                    new Point(
                            bot_rect[0],
                            bot_rect[1]),

                    new Point(
                            bot_rect[2],
                            bot_rect[3]),
                    new Scalar(0, 0, 255), 1);

            return input;
        }
    }


    class WobblePipeline extends OpenCvPipeline {
        double[] redness;
        int rectangle = 0;

        int rect_count = 19;

        @Override
        public Mat processFrame(Mat input) {

            input.convertTo(input, CV_8UC1, 1, 10);

            Mat[] rects = new Mat[rect_count];

            for (int i = 0; i < rects.length; i++) {
                rects[i] = input.submat((int)(input.rows() * i * (1.0/rect_count)), (int)(input.rows() * (i+1) * (1.0/rect_count)), (int)(input.cols()*0.4), (int)(input.cols()*1.0));

                Imgproc.rectangle(
                        input,
                        new Point(
                                (int)(input.cols() * 0.4),
                                (int)(input.rows() * i * (1.0/rect_count))
                        ),

                        new Point(
                                (int)(input.cols() * 1.0),
                                (int)(input.rows() * (i+1) * (1.0/rect_count))
                        ),
                        new Scalar(0, 255, 0), 1
                );
            }

            Scalar[] means = new Scalar[rect_count];

            for (int i = 0; i < means.length; i++) {
                means[i] = Core.mean(rects[i]);
            }

            redness = new double[rect_count];

            double max_redness = 0;
            int max_redness_index = 0;

            for (int i = 0; i < redness.length; i++) {
                redness[i] = Math.sqrt(3 * Math.pow(255, 2)) - MathUtil.color_distance(means[i].val[0], means[i].val[1], means[i].val[2], 255, 0, 0);

                if (redness[i] > max_redness) {
                    max_redness = redness[i];
                    max_redness_index = i;
                }
            }

            rectangle = max_redness_index;

            return input;
        }
    }
}