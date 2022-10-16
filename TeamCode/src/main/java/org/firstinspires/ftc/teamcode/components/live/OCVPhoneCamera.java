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
    // Normalized offset of center rect x from left of image
    public static double rect_offset_x = 0.50;
    // Normalized offset of center rect y from top of image
    public static double rect_offset_y = 0.50;
    // Width of all rects
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
        // Load and get an instance of the phone camera from the hardware map
        int cameraMonitorViewId = hwmap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwmap.appContext.getPackageName());
        phone_camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
    }

    @Override
    public void startup() {
        super.startup();

        phone_camera.openCameraDevice();

        // Instantiate all possible pipelines
        capstone_pipline = new CapstonePipline();

        // Load the default pipeline
        set_pipeline(capstone_pipline);
    }

    //fuck java

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);
        telemetry.addData("FRAME", phone_camera.getFrameCount());
        telemetry.addData("FPS", String.format("%.2f", phone_camera.getFps()));
        telemetry.addData("PATTERN", capstone_pipline.pattern);
    }

    public void set_pipeline(OpenCvPipeline pl) {
        phone_camera.setPipeline(pl);
    }

    public void start_streaming() {
        /**
         * Start the camera device and display the output of the camera to the robot controller phone
         */
        if (!streaming) {
            phone_camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            streaming = true;
        }
    }

    public int get_randomization_pattern() {
        /**
         * Get the current pattern from the team marker pipeline
         */
        return capstone_pipline.pattern;
    }

    public void stop_streaming() {
        /**
         * Close the camera device, stop sending output through the pipeline
         */
        phone_camera.stopStreaming();
        streaming = false;
    }

    class CapstonePipline extends OpenCvPipeline {
        /**
         * Pipeline for recognizing position of team marker on the 3 pieces of tape, using the color saturation
         */

        int pattern;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Mat processFrame(Mat input) {

            input.convertTo(input, CV_8UC1, 1, 10);

            // Denormalize positions and sizes of the 3 rects
            int[] rect = {
                    (int) (input.cols() * (OCVPhoneCameraConfig.rect_offset_x - OCVPhoneCameraConfig.rect_size/2)),
                    (int) (input.rows() * (OCVPhoneCameraConfig.rect_offset_y - OCVPhoneCameraConfig.rect_size/2)),
                    (int) (input.cols() * (OCVPhoneCameraConfig.rect_offset_x + OCVPhoneCameraConfig.rect_size/2)),
                    (int) (input.rows() * (OCVPhoneCameraConfig.rect_offset_y + OCVPhoneCameraConfig.rect_size/2))
            };



            // Load the rects into matrices
            Mat mat = input.submat(rect[1], rect[3], rect[0], rect[2]);

            // Get the average color of each rect
            Scalar mean = Core.mean(mat);

            //
            if (mean.val[0] >= mean.val[1] && mean.val[0] >= mean.val[2]) {
                pattern = 1;
            }
            else if (mean.val[1] >= mean.val[0] && mean.val[1] >= mean.val[2]) {
                pattern = 2;
            }
            else if (mean.val[2] >= mean.val[0] && mean.val[2] >= mean.val[1]) {
                pattern = 3;
            }
            else {
                pattern = 0;
            }

            // Draw the rects on he image for visualization and lineup purposess
            Imgproc.rectangle(input, new Point(rect[0], rect[1]), new Point(rect[2], rect[3]), new Scalar(0, 0, 255), 1);

            return input;
        }
    }
}