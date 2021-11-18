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

import static org.firstinspires.ftc.teamcode.components.live.LiftConfig.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Elevator lifts the stone and extender up
// Extender extends over the tower, and the grabber releases the stone


@Config
class LiftConfig {
    public static int LIFT_OFFSET = 0;

    public static int INTAKE_LEVEL_COUNTS = 0;
    public static int LOW_LEVEL_COUNTS = 300;
    public static int MID_LEVEL_COUNTS = 600;
    public static int HIGH_LEVEL_COUNTS = 900;
    public static int CAP_LEVEL_COUNTS = 1200;

    public static double PID_P = 15;
    public static double PID_I = 0.1;
    public static double PID_D = 4;

    public static int LIFT_DOWN_OVERSHOOT = 100;

    public static int TWEAK_MAX_ADD = 100;

}

public class Lift extends Component {

    //// MOTORS ////
    public DcMotorEx lift;

    public int level;

    private boolean starting_move = false;

    public int lift_target = 0;

    public int lift_offset = 0;

    static double tweak = 0;
    static double tweak_cache = 0;

    static List<Integer> level_positions;

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
                HIGH_LEVEL_COUNTS,
                CAP_LEVEL_COUNTS
        );

        max_level = level_positions.size() - 1;
    }

    @Override
    public void registerHardware (HardwareMap hwmap)
    {
        super.registerHardware(hwmap);

        //// MOTORS ////
        lift     = hwmap.get(DcMotorEx.class, "lift");
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);

        if (starting_move) {

            //if (level == 0) {
            //    if(robot.bulk_data_1.getDigitalInputState(3) && robot.bulk_data_1.getDigitalInputState(5)) {
            //        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //        set_power(-1);
            //    }
            //} else {
            lift.setTargetPosition(lift_target+lift_offset);
            lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            set_power(1);
            //}

            starting_move = false;
        }

        /*
        if (level == 0 && (lift.getPower() == -1)) {
            if ((!robot.bulk_data_1.getDigitalInputState(5)) && (lift.getPower() != 0)) {
                lift_offset = robot.bulk_data_2.getMotorCurrentPosition(lift);
                lift.setPower(0);
            }
        }
        */


        if (tweak != tweak_cache) {
            tweak_cache = tweak;
            lift.setTargetPosition(
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

        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        PIDCoefficients pid_coeffs = new PIDCoefficients(PID_P, PID_I, PID_D);
        lift.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pid_coeffs);
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

        telemetry.addData("LL TURNS",TELEMETRY_DECIMAL.format(robot.bulk_data_2.getMotorCurrentPosition(lift)));
        telemetry.addData("LL TARGET",TELEMETRY_DECIMAL.format(lift_target));
        telemetry.addData("LL OFFSET", TELEMETRY_DECIMAL.format(lift_offset));
        telemetry.addData("LIFT BUSY",robot.bulk_data_2.isMotorAtTargetPosition(lift));
        telemetry.addData("LIFT RUNNING", running_lift());
        telemetry.addData("LEVEL", level);
        telemetry.addData("MAX LEVEL", max_level);
        telemetry.addData("BD", robot.bulk_data_1.getDigitalInputState(1));
    }

    public void set_power(double speed) {
        lift.setPower(speed);
    }

    public boolean running_lift() {
        return lift.getPower() != 0;
    }

    private void set_target_position(int pos) {
        lift_target = pos;
    }

    public void elevate_to(int target) {
        level = Math.max(Math.min(target, max_level), 0);
        set_target_position(level_positions.get(level) + LIFT_OFFSET);
        starting_move = true;
    }

    public void min_lift() {
        elevate_to(0);
    }

    public void max_lift() {
        elevate_to(max_level);
    }

    public void tweak(double tweak) {
        this.tweak = tweak;
    }
}