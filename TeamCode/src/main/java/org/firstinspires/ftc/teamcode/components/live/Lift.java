package org.firstinspires.ftc.teamcode.components.live;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.util.qus.ServoQUS;

import static org.firstinspires.ftc.teamcode.components.live.LiftConfig.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Config
class LiftConfig {
    // Basic value to add to any lift position, the effective zero
    public static int LIFT_OFFSET = 0;

    // The encoder counts for the intake level, AKA ground level
    public static int INTAKE_LEVEL_COUNTS = 0;
    // The encoder counts lift position for the low level, the first level of the alliance shipping hub or the shared shipping hub
    public static int LOW_LEVEL_COUNTS = 1700;
    // The encoder counts lift position for the middle level, the second level of the alliance shipping hub
    public static int MID_LEVEL_COUNTS = 2800;
    // The encoder counts lift position for the high level, the third level of the alliance shipping hub
    public static int HIGH_LEVEL_COUNTS = 3950;

    // Lift PID proportion coefficient
    public static double PID_P = 15;
    // Lift PID integral coefficient
    public static double PID_I = 0.1;
    // Lift PID derivative coefficient
    public static double PID_D = 4;

    // How many encoder counts to overshoot by when going to the minimum lift level, to ensue the limit switch is hit
    public static int LIFT_DOWN_OVERSHOOT = 100;

    // The amount of encoder accounts corresponding to how much the lift should be offset by a maximum tweak
    public static int TWEAK_MAX_ADD = 100;

    public static double CLAW_OPEN_POSITION = 1;
    public static double CLAW_CLOSE_POSITION = 0;

}

public class Lift extends Component {
    /**
     * Component for the linear slide lift that raises the intake up and down
     * Includes the lift motor, controlled by a PID loop
     */

    //// MOTORS ////
    public DcMotorEx lift;

    //// SERVOS ////
    public ServoQUS claw;

    // The current level the lift should be holding
    public int level;

    // Should be set to true when the lift level changes so the update loop knows to set new encoder positions
    private boolean starting_move = false;

    // The current encoder target for the lift
    public int lift_target = 0;

    // The offset of the lift as set by the limit switch re-zeroing
    public int lift_offset = 0;

    // Normalized amount of tweak (-1 to 1), will be multiplied by the maximum tweak amount to calculate total tweak offset counts
    static double tweak = 0;
    // The cached amount of tweak so we don't reapply it multiple times
    static double tweak_cache = 0;

    // A list of all of the level positions so encoder counts can be retrieved from level indices
    static List<Integer> level_positions;

    // The maximum level index the lift can safely raise to
    public int max_level;

    {
        name = "Lift";
    }

    public Lift(Robot robot)
    {
        super(robot);

        level_positions = Arrays.asList(
                INTAKE_LEVEL_COUNTS,
                LOW_LEVEL_COUNTS,
                MID_LEVEL_COUNTS,
                HIGH_LEVEL_COUNTS
        );

        max_level = level_positions.size() - 1;
    }

    @Override
    public void registerHardware (HardwareMap hwmap)
    {
        super.registerHardware(hwmap);

        //// MOTORS ////
        lift = hwmap.get(DcMotorEx.class, "lift");

        //// SERVOS ////
        claw = new ServoQUS(hwmap.get(Servo.class, "claw"));
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);

        claw.update();

        if (starting_move) {
            // If we just changed our level, set the new target to the new lift counts as per the level index
            // If the motor wasn't already running, start running it
            lift.setTargetPosition(lift_target+lift_offset);
            lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            set_power(1);

            starting_move = false;
        }

        if (tweak != tweak_cache) {
            // If the tweak has changed from last time, adjust the target position to account for it
            tweak_cache = tweak;
            lift.setTargetPosition(
                // Ensure that the tweak does not drive the lift past its maximum safe level
                Range.clip(
                    lift_target + lift_offset + (int) (tweak * TWEAK_MAX_ADD),
                    0,
                    level_positions.get(max_level)+TWEAK_MAX_ADD
                )
            );
        }
    }

    @Override
    public void startup() {
        super.startup();

        // Ensure that we brake to hold position on zero power
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Apply the PID coefficients as per the config
        PIDCoefficients pid_coeffs = new PIDCoefficients(PID_P, PID_I, PID_D);
        lift.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pid_coeffs);

        // Reset the zero position of the encoders to the current position on startup
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void shutdown() {
        set_power(0);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);

        telemetry.addData("LL TURNS", TELEMETRY_DECIMAL.format(lift.getCurrentPosition()));
        telemetry.addData("LIFT BUSY", lift.isBusy());
        telemetry.addData("LL TARGET",TELEMETRY_DECIMAL.format(lift_target));
        telemetry.addData("LL OFFSET", TELEMETRY_DECIMAL.format(lift_offset));
        telemetry.addData("LIFT RUNNING", running_lift());
        telemetry.addData("LEVEL", level);
        telemetry.addData("MAX LEVEL", max_level);
    }


    private void set_target_position(int pos) {
        lift_target = pos;
    }

    private void set_power(double speed) {
        lift.setPower(speed);
    }

    public boolean running_lift() {
        /**
         * Get whether the lift is currently holding position, or if it is resting at the bottom
         */
        return lift.getPower() != 0;
    }

    public void elevate_to(int target) {
        /**
         * Elevate to an arbitrary lift index level
         */
        level = Math.max(Math.min(target, max_level), 0);
        set_target_position(level_positions.get(level) + LIFT_OFFSET);
        starting_move = true;
    }

    public void min_lift() {
        /**
         * Bring the lift back to its minimum level index
         */
        elevate_to(0);
    }

    public void max_lift() {
        /**
         * Elevate to the maximum safe lift level index
         */
        elevate_to(max_level);
    }

    public void tweak(double tweak) {
        /**
         * Apply an arbitrary tweak amount to the lift level, used for fine tuning elevation between levels, -1 to 1
         */
        this.tweak = tweak;
    }

    public void open_claw() {
        claw.queue_position(CLAW_OPEN_POSITION);
    }

    public void close_claw() {
        claw.queue_position(CLAW_CLOSE_POSITION);
    }

}