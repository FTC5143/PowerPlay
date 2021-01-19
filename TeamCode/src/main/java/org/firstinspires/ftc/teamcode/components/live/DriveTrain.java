package org.firstinspires.ftc.teamcode.components.live;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.coyote.geometry.Pose;
import org.firstinspires.ftc.teamcode.coyote.path.Path;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.systems.LocalCoordinateSystem;
import org.firstinspires.ftc.teamcode.util.qus.DcMotorQUS;

import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.RED;

// Drive Train component
// Includes: Drive Motors, IMU
// I hate it also

@Config
class DriveTrainConfig {

}

public class DriveTrain extends Component {

    //// MOTORS ////
    private DcMotorQUS drive_lf;   // Left-Front drive motor
    private DcMotorQUS drive_rf;   // Right-Front drive motor
    private DcMotorQUS drive_lb;   // Left-Back drive motor
    private DcMotorQUS drive_rb;   // Right-Back drive motor

    //// SENSORS ////
    private BNO055IMU imu;          // For recalibrating odometry angle periodically

    private Orientation last_imu_orientation = new Orientation();

    public LocalCoordinateSystem lcs = new LocalCoordinateSystem();

    // Drive train moves according to these. Update them, it moves
    private double drive_x = 0;
    private double drive_y = 0;
    private double drive_a = 0;

    public Path current_path;

    private PIDCoefficients drive_pos_coeffs = new PIDCoefficients(10, 0.1, 2);
    private PIDCoefficients drive_ang_coeffs = new PIDCoefficients(10, 0.1, 2);

    private PIDFController drive_pos_controller = new PIDFController(drive_pos_coeffs);
    private PIDFController drive_ang_controller = new PIDFController(drive_ang_coeffs);

    {
        name = "Drive Train";
    }

    public DriveTrain(Robot robot) {
        super(robot);
    }

    @Override
    public void registerHardware(HardwareMap hwmap) {
        super.registerHardware(hwmap);

        //// MOTORS ////
        drive_lf    = new DcMotorQUS(hwmap.get(DcMotorEx.class, "drive_lf"));
        drive_rf    = new DcMotorQUS(hwmap.get(DcMotorEx.class, "drive_rf"));
        drive_lb    = new DcMotorQUS(hwmap.get(DcMotorEx.class, "drive_lb"));
        drive_rb    = new DcMotorQUS(hwmap.get(DcMotorEx.class, "drive_rb"));

        //// SENSORS ////
        imu = hwmap.get(BNO055IMU.class, "imu");

    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);

        // Updating the localizer with the new odometry encoder counts
        lcs.update(
                robot.bulk_data_1.getMotorCurrentPosition(drive_lf.motor),
                robot.bulk_data_1.getMotorCurrentPosition(drive_rf.motor),
                robot.bulk_data_1.getMotorCurrentPosition(drive_lb.motor)
        );

        // Finding new motors powers from the drive variables
        double[] motor_powers = mecanum_math(drive_x, drive_y, drive_a);

        drive_lf.queue_power(motor_powers[0]);
        drive_rf.queue_power(motor_powers[1]);
        drive_lb.queue_power(motor_powers[2]);
        drive_rb.queue_power(motor_powers[3]);

        // Set one motor power per cycle. We do this to maintain a good odometry update speed
        // We should be doing a full drive train update at about 40hz with this configuration, which is more than enough
        if (robot.cycle % 4 == 0) {
            drive_lf.update();
        }
        if (robot.cycle % 4 == 1) {
            drive_rf.update();
        }
        if (robot.cycle % 4 == 2) {
            drive_lb.update();
        }
        if (robot.cycle % 4 == 3) {
            drive_rb.update();
        }
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);

        telemetry.addData("LE TURNS", robot.bulk_data_1.getMotorCurrentPosition(drive_lf.motor));
        telemetry.addData("RE TURNS", robot.bulk_data_1.getMotorCurrentPosition(drive_rf.motor));
        telemetry.addData("CE TURNS", robot.bulk_data_1.getMotorCurrentPosition(drive_lb.motor));

        telemetry.addData("X", lcs.x);
        telemetry.addData("Y", lcs.y);
        telemetry.addData("A", lcs.a);

        telemetry.addData("PID", drive_lf.motor.getPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION));

        telemetry.addData("IMU", last_imu_orientation.firstAngle+" "+last_imu_orientation.secondAngle+" "+last_imu_orientation.thirdAngle);
    }

    @Override
    public void startup() {
        super.startup();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        imu.initialize(parameters);

        // Set all the zero power behaviors to brake on startup, to prevent slippage as much as possible
        drive_lf.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drive_rf.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drive_lb.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drive_rb.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Reverse the left motors, because they have a different orientation on the robot
        drive_rf.motor.setDirection(DcMotor.Direction.REVERSE);
        drive_rb.motor.setDirection(DcMotor.Direction.REVERSE);
        
        set_mode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // We run without encoder because we do not have motor encoders, we have odometry instead
        set_mode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    @Override
    public void shutdown() {
        super.shutdown();
        // Cut all motor powers on robot shutdown
        stop();
    }

    // Return the motor powers needed to move in the given travel vector. Should give optimal speeds
    private double[] mecanum_math(double x, double y, double a) {
        double[] power = new double[]{- x + y - a, + x + y + a, + x + y - a, - x + y + a};

        double max = Math.max(Math.max(Math.abs(power[0]),Math.abs(power[1])),Math.max(Math.abs(power[2]),Math.abs(power[3])));

        if (max > 1) {
            power[0] /= max;
            power[1] /= max;
            power[2] /= max;
            power[3] /= max;
        }

        return power;
    }

    public void read_from_imu() {
        last_imu_orientation = imu.getAngularOrientation();
    }

    // An easy public setter for the drive variables
    public void mechanum_drive(double x, double y, double a) {
        drive_x = x;
        drive_y = y;
        drive_a = a;
    }

    // Stop all motors, and reset drive variables
    public void stop() {
        mechanum_drive(0, 0, 0);
        drive_lf.motor.setPower(0);
        drive_rf.motor.setPower(0);
        drive_lb.motor.setPower(0);
        drive_rb.motor.setPower(0);
    }
    
    // Bulk motor set mode
    private void set_mode(DcMotor.RunMode mode) {
        drive_lf.motor.setMode(mode);
        drive_rf.motor.setMode(mode);
        drive_lb.motor.setMode(mode);
        drive_rb.motor.setMode(mode);
    }

    
    // Basic run to pose with odometry
    public void odo_move(double x, double y, double a, double speed) {
        odo_move(x, y, a, speed, 1, 0.02, 0, 0);
    }

    public void odo_move(double x, double y, double a, double speed, double pos_acc, double angle_acc) {
        odo_move(x, y, a, speed, pos_acc, angle_acc, 0, 0);
    }

    public void odo_move(double x, double y, double a, double speed, double pos_acc, double angle_acc, double timeout) {
        odo_move(x, y, a, speed, pos_acc, angle_acc, timeout, 0);
    }


    public void odo_move(double x, double y, double a, double speed, double pos_acc, double angle_acc, double timeout, double time_at_target) {
        a = -a;

        double original_distance = Math.hypot(x-lcs.x, y-lcs.y);
        double original_distance_a = Math.abs(a - lcs.a);

        robot.opmode.resetStartTime();

        double time_at_goal = 0;

        if (original_distance > 0 || original_distance_a > 0) {
            while (robot.opmode.opModeIsActive()) {
                double distance = Math.hypot(x - lcs.x, y - lcs.y);
                double distance_a = Math.abs(a - lcs.a);

                double drive_angle = Math.atan2(y-lcs.y, x-lcs.x);
                double mvmt_x = Math.cos(drive_angle - lcs.a) * ((Range.clip(distance, 0, (7*speed)))/(7*speed)) * speed;
                double mvmt_y = -Math.sin(drive_angle - lcs.a) * ((Range.clip(distance, 0, (7*speed)))/(7*speed)) * speed;
                double mvmt_a = -Range.clip((a-lcs.a)*3, -1, 1) * speed;

                mechanum_drive(mvmt_x, mvmt_y, mvmt_a);



                if ((distance < pos_acc && distance_a < angle_acc) || (timeout > 0 && robot.opmode.getRuntime() > timeout)) {
                    if (robot.opmode.getRuntime()-time_at_goal >= time_at_target) {
                        stop();
                        break;
                    }
                } else {
                    time_at_goal = robot.opmode.getRuntime();
                }
            }
        }
    }

    public void odo_drive_towards(double x, double y, double a, double speed) {
        double distance = Math.hypot(x - lcs.x, y - lcs.y);

        double drive_angle = Math.atan2(y-lcs.y, x-lcs.x);
        double mvmt_x = Math.cos(drive_angle - lcs.a) * ((Range.clip(distance, 0, (8*speed)))/(8*speed)) * speed;
        double mvmt_y = -Math.sin(drive_angle - lcs.a) * ((Range.clip(distance, 0, (8*speed)))/(8*speed)) * speed;
        double mvmt_a = -Range.clip((a-lcs.a)*3, -1, 1) * speed;

        mechanum_drive(mvmt_x, mvmt_y, mvmt_a);
    }


    public void odo_reset(double x, double y, double a) {
        this.lcs.x = x;
        this.lcs.y = y;
        this.lcs.a = a;
    }
    
    // Following a pure pursuit path with odometry
    public void follow_curve_path(Path path) {
        
        // Update our current path, for telemetry
        this.current_path = path;

        while (robot.opmode.opModeIsActive()) {
            
            // Get our lookahead point
            path.update(lcs.get_pose());
            Pose lookahead_pose = path.getFollowPose();

            // Get the distance to our lookahead point
            double distance = Math.hypot(lookahead_pose.x-lcs.x, lookahead_pose.y-lcs.y);

            double speed;
            // Find our drive speed based on distance
            if (distance < current_path.getFollowCircle().radius) {
                speed = Range.clip((distance / 8) + 0.1, 0, 1);
            } else {
                speed = 1;
            }

            // Find our turn speed based on angle difference
            double turn_speed = Range.clip(Math.abs(lcs.a-lookahead_pose.angle) / (Math.PI/4) + 0.1, 0, 1);

            // Drive towards the lookahead point
            drive_to_pose(lookahead_pose, speed, turn_speed);
        }

    }

    // Set motor powers to drive to a position and angle
    public void drive_to_pose(Pose pose, double drive_speed, double turn_speed) {

        // Find the angle to the pose
        double drive_angle = Math.atan2(pose.y-lcs.y, pose.x-lcs.x);

        // Find movement vector to drive towards that point
        double mvmt_x = Math.cos(drive_angle - lcs.a) * drive_speed;
        double mvmt_y = -Math.sin(drive_angle - lcs.a) * drive_speed;
        // Find angle speed to turn towards the desired angle
        double mvmt_a = -Math.signum(Range.clip((pose.angle - lcs.a - (Math.PI/2)), -1, 1)) * turn_speed;

        // Update actual motor powers with our movement vector
        mechanum_drive(mvmt_x, mvmt_y, mvmt_a);

    }

}
