package org.firstinspires.ftc.teamcode.components.live;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

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
import java.util.Comparator;
import java.util.stream.IntStream;

import static org.opencv.core.CvType.CV_8UC1;

@Config
class OCVPhoneCameraConfig {
    public static double rect_offset_x = 0.50;
    public static double rect_offset_y = 0.50;
    public static double rect_separation = 0.25;
    public static double rect_size = 0.05;
}

public class OCVPhoneCamera extends Component {

    OpenCvCamera phone_camera;

    public CapstonePipline capstone_pipline;

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

        capstone_pipline = new CapstonePipline();

        set_pipeline(capstone_pipline);
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);
        telemetry.addData("FRAME", phone_camera.getFrameCount());
        telemetry.addData("FPS", String.format("%.2f", phone_camera.getFps()));
        telemetry.addData("L SAT", capstone_pipline.sat[0]);
        telemetry.addData("M SAT", capstone_pipline.sat[0]);
        telemetry.addData("R SAT", capstone_pipline.sat[0]);
        telemetry.addData("PATTERN", capstone_pipline.pattern);
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
        return capstone_pipline.pattern;
    }

    public void stop_streaming() {
        phone_camera.stopStreaming();
        streaming = false;
    }

    class CapstonePipline extends OpenCvPipeline {
        int[] sat = new int[3];
        int pattern;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Mat processFrame(Mat input) {

            input.convertTo(input, CV_8UC1, 1, 10);

            int[] l_rect = {
                    (int) (input.cols() * (OCVPhoneCameraConfig.rect_offset_x - OCVPhoneCameraConfig.rect_separation - OCVPhoneCameraConfig.rect_size/2)),
                    (int) (input.rows() * (OCVPhoneCameraConfig.rect_offset_y - OCVPhoneCameraConfig.rect_size/2)),
                    (int) (input.cols() * (OCVPhoneCameraConfig.rect_offset_x - OCVPhoneCameraConfig.rect_separation + OCVPhoneCameraConfig.rect_size/2)),
                    (int) (input.rows() * (OCVPhoneCameraConfig.rect_offset_y + OCVPhoneCameraConfig.rect_size/2))
            };

            int[] m_rect = {
                    (int) (input.cols() * (OCVPhoneCameraConfig.rect_offset_x - OCVPhoneCameraConfig.rect_size/2)),
                    (int) (input.rows() * (OCVPhoneCameraConfig.rect_offset_y - OCVPhoneCameraConfig.rect_size/2)),
                    (int) (input.cols() * (OCVPhoneCameraConfig.rect_offset_x + OCVPhoneCameraConfig.rect_size/2)),
                    (int) (input.rows() * (OCVPhoneCameraConfig.rect_offset_y + OCVPhoneCameraConfig.rect_size/2))
            };

            int[] r_rect = {
                    (int) (input.cols() * (OCVPhoneCameraConfig.rect_offset_x + OCVPhoneCameraConfig.rect_separation - OCVPhoneCameraConfig.rect_size/2)),
                    (int) (input.rows() * (OCVPhoneCameraConfig.rect_offset_y - OCVPhoneCameraConfig.rect_size/2)),
                    (int) (input.cols() * (OCVPhoneCameraConfig.rect_offset_x + OCVPhoneCameraConfig.rect_separation + OCVPhoneCameraConfig.rect_size/2)),
                    (int) (input.rows() * (OCVPhoneCameraConfig.rect_offset_y + OCVPhoneCameraConfig.rect_size/2))
            };

            Mat l_mat = input.submat(l_rect[1], l_rect[3], l_rect[0], l_rect[2]);
            Mat m_mat = input.submat(m_rect[1], m_rect[3], m_rect[0], m_rect[2]);
            Mat r_mat = input.submat(r_rect[1], r_rect[3], r_rect[0], r_rect[2]);

            Scalar l_mean = Core.mean(l_mat);
            Scalar m_mean = Core.mean(m_mat);
            Scalar r_mean = Core.mean(r_mat);

            float[] l_hsv = new float[3];
            float[] m_hsv = new float[3];
            float[] r_hsv = new float[3];

            Color.RGBToHSV((int) l_mean.val[0], (int) l_mean.val[1], (int) l_mean.val[2], l_hsv);
            Color.RGBToHSV((int) m_mean.val[0], (int) m_mean.val[1], (int) m_mean.val[2], m_hsv);
            Color.RGBToHSV((int) r_mean.val[0], (int) r_mean.val[1], (int) r_mean.val[2], r_hsv);

            sat[0] = (int)(100.0 * l_hsv[1]);
            sat[1] = (int)(100.0 * m_hsv[1]);
            sat[2] = (int)(100.0 * r_hsv[1]);

            int maximum_index = 0;
            for (int i = 0; i < sat.length; i++) {
                maximum_index = sat[i] > sat[maximum_index] ? i : maximum_index;
            }

            pattern = maximum_index + 1; // Pattern should not be 0 index, 0 is reserved for a "null"

            Imgproc.rectangle(input, new Point(l_rect[0], l_rect[1]), new Point(l_rect[2], l_rect[3]), new Scalar(0, 0, 255), 1);
            Imgproc.rectangle(input, new Point(m_rect[0], m_rect[1]), new Point(m_rect[2], m_rect[3]), new Scalar(0, 0, 255), 1);
            Imgproc.rectangle(input, new Point(r_rect[0], r_rect[1]), new Point(r_rect[2], r_rect[3]), new Scalar(0, 0, 255), 1);

            return input;
        }
    }
}