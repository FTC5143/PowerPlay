package org.firstinspires.ftc.teamcode.components.live;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.systems.LocalCoordinateSystem;
import org.firstinspires.ftc.teamcode.systems.pathfollowing.CurvePath;
import org.firstinspires.ftc.teamcode.systems.pathfollowing.Pose;

import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.RED;

// Drive Train component
// Includes: Drive Motors, IMU
// I hate it also

public class DriveTrain extends Component {

    //// MOTORS ////
    private DcMotorEx drive_lf;   // Left-Front drive motor
    private DcMotorEx drive_rf;   // Right-Front drive motor
    private DcMotorEx drive_lb;   // Left-Back drive motor
    private DcMotorEx drive_rb;   // Right-Back drive motor

    public LocalCoordinateSystem lcs = new LocalCoordinateSystem();

    public int color = RED;

    // Drive train moves according to these. Update them, it moves
    private double drive_x = 0;
    private double drive_y = 0;
    private double drive_a = 0;

    // Cached motor powers so we only write when we need to
    private double cache_lf_power = 0;
    private double cache_rf_power = 0;
    private double cache_lb_power = 0;
    private double cache_rb_power = 0;

    public CurvePath current_path;

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
        drive_lf    = hwmap.get(DcMotorEx.class, "drive_lf");
        drive_rf    = hwmap.get(DcMotorEx.class, "drive_rf");
        drive_lb    = hwmap.get(DcMotorEx.class, "drive_lb");
        drive_rb    = hwmap.get(DcMotorEx.class, "drive_rb");

    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);

        // Updating the localizer with the new odometry encoder counts
        lcs.update(
                robot.bulk_data_1.getMotorCurrentPosition(drive_lf),
                robot.bulk_data_1.getMotorCurrentPosition(drive_rf),
                robot.bulk_data_1.getMotorCurrentPosition(drive_lb)
        );

        // Finding new motors powers from the drive variables
        double[] motor_powers = mecanum_math(drive_x, drive_y, drive_a);

        // Set one motor power per cycle. We do this to maintain a good odometry update speed
        // We should be doing a full drive train update at about 40hz with this configuration, which is more than enough
        if (robot.cycle % 4 == 0 /*&& motor_powers[0] != cache_lf_power*/) {
            drive_lf.setPower(motor_powers[0]);
            cache_lf_power = motor_powers[0];
        }
        if (robot.cycle % 4 == 1 /*&& motor_powers[0] != cache_rf_power*/) {
            drive_rf.setPower(motor_powers[1]);
            cache_rf_power = motor_powers[1];
        }
        if (robot.cycle % 4 == 2 /*&& motor_powers[0] != cache_lb_power*/) {
            drive_lb.setPower(motor_powers[2]);
            cache_lb_power = motor_powers[2];
        }
        if (robot.cycle % 4 == 3 /*&& motor_powers[0] != cache_rb_power*/) {
            drive_rb.setPower(motor_powers[3]);
            cache_rb_power = motor_powers[3];
        }
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);

        telemetry.addData("LE TURNS", robot.bulk_data_1.getMotorCurrentPosition(drive_lf));
        telemetry.addData("RE TURNS", robot.bulk_data_1.getMotorCurrentPosition(drive_rf));
        telemetry.addData("CE TURNS", robot.bulk_data_1.getMotorCurrentPosition(drive_lb));

        telemetry.addData("X", lcs.x);
        telemetry.addData("Y", lcs.y);
        telemetry.addData("A", lcs.a);

        telemetry.addData("PID", drive_lf.getPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION)+" | "+drive_rf.getPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION)+" | "+drive_lb.getPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION)+" | "+drive_rb.getPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION));
    }

    @Override
    public void startup() {
        super.startup();

        // Set all the zero power behaviors to brake on startup, to prevent slippage as much as possible
        drive_lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drive_rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drive_lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drive_rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Reverse the left motors, because they have a different orientation on the robot
        drive_lf.setDirection(DcMotor.Direction.REVERSE);
        drive_lb.setDirection(DcMotor.Direction.REVERSE);
        
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
        double[] power = new double[]{-x + y + a, +x + y - a, +x + y + a, -x + y - a};

        double max = Math.max(Math.max(Math.abs(power[0]),Math.abs(power[1])),Math.max(Math.abs(power[2]),Math.abs(power[3])));

        if (max > 1) {
            power[0] /= max;
            power[1] /= max;
            power[2] /= max;
            power[3] /= max;
        }

        return power;
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
        set_power(0);
    }
    
    // Bulk motor set mode
    private void set_mode(DcMotor.RunMode mode) {
        drive_lf.setMode(mode);
        drive_rf.setMode(mode);
        drive_lb.setMode(mode);
        drive_rb.setMode(mode);
    }

    // Bulk motor set power
    private void set_power(double lf, double rf, double lb, double rb) {
        drive_lf.setPower(lf);
        drive_rf.setPower(rf);
        drive_lb.setPower(lb);
        drive_rb.setPower(rb);
    }

    // For setting all motors to the same power
    private void set_power(double power) {
        set_power(power, power, power ,power);
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

        if (color == RED) {
            a = -a;
        } else {
            x = -x;
        }

        double original_distance = Math.hypot(x-lcs.x, y-lcs.y);
        double original_distance_a = Math.abs(a - lcs.a);

        robot.lopmode.resetStartTime();

        double time_at_goal = 0;

        if (original_distance > 0 || original_distance_a > 0) {
            while (robot.lopmode.opModeIsActive()) {
                double distance = Math.hypot(x - lcs.x, y - lcs.y);
                double distance_a = Math.abs(a - lcs.a);

                double progress = distance/original_distance;
                double progress_a = distance_a/original_distance_a;

                double drive_angle = Math.atan2(y-lcs.y, x-lcs.x);
                double mvmt_x = Math.cos(drive_angle - lcs.a) * ((Range.clip(distance, 0, (8*speed)))/(8*speed)) * speed;
                double mvmt_y = -Math.sin(drive_angle - lcs.a) * ((Range.clip(distance, 0, (8*speed)))/(8*speed)) * speed;
                double mvmt_a = -Range.clip((a-lcs.a)*3, -1, 1) * speed;

                mechanum_drive(mvmt_x, mvmt_y, mvmt_a);



                if ((distance < pos_acc && distance_a < angle_acc) || (timeout > 0 && robot.lopmode.getRuntime() > timeout)) {
                    if (robot.lopmode.getRuntime()-time_at_goal >= time_at_target) {
                        stop();
                        break;
                    }
                } else {
                    time_at_goal = robot.lopmode.getRuntime();
                }
            }
        }
    }
    
    // Following a pure pursuit path with odometry
    public void follow_curve_path(CurvePath path) {
        
        // Update our current path, for telemetry
        this.current_path = path;

        while (robot.lopmode.opModeIsActive()) {
            
            // Get our lookahead point
            Pose lookahead_pose = path.get_lookahead_pose(lcs.x, lcs.y);

            // Get the distance to our lookahead point
            double distance = Math.hypot(lookahead_pose.x-lcs.x, lookahead_pose.y-lcs.y);

            double speed;
            // Find our drive speed based on distance
            if (distance < current_path.radius) {
                speed = Range.clip((distance / 8) + 0.1, 0, 1);
            } else {
                speed = 1;
            }

            // Find our turn speed based on angle difference
            double turn_speed = Range.clip(Math.abs(lcs.a-lookahead_pose.a) / (Math.PI/4) + 0.1, 0, 1);

            // Drive towards the lookahead point
            drive_to_pose(lookahead_pose, speed, 1);
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
        double mvmt_a = -Math.signum(Range.clip((pose.a - lcs.a - (Math.PI/2)), -1, 1)) * turn_speed;

        // Update actual motor powers with our movement vector
        mechanum_drive(mvmt_x, mvmt_y, mvmt_a);

    }

}
