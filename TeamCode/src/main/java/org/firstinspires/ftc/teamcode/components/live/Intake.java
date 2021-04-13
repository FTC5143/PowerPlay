package org.firstinspires.ftc.teamcode.components.live;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsAnalogOpticalDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.qus.CRServoQUS;
import org.firstinspires.ftc.teamcode.util.qus.DcMotorQUS;
import org.firstinspires.ftc.teamcode.util.qus.ServoQUS;

@Config
class IntakeConfig {
    public static double lift_speed = 0.90;
    public static double roller_speed = -1.0;

    public static double chopper_chopped = 0.50;
    public static double chopper_unchopped = 0;
    public static int chopper_delay = 50;
    public static int chopper_repeat_interval = 250;
    public static int chopper_focal_length = 30;

    public static int loaded_focal_length = 100;

    public static double popout_in = 0.28;
    public static double popout_out = 0.77;
    public static double popout_speed = -1.0;

    /*
    public static double knocker_left_in = 0.475;
    public static double knocker_left_out = 0.85;
    public static double knocker_right_in = 0.5;
    public static double knocker_right_out = 0.15;
    */

    public static double locker_left_locked = 0.52;
    public static double locker_left_unlocked = 0.88;
    public static double locker_right_locked = 0.5;
    public static double locker_right_unlocked = 0.6;
}

public class Intake extends Component {

    //// SERVOS ////
    private ServoQUS chopper;
    private CRServoQUS roller;
    private ServoQUS popout_joint;
    private CRServoQUS popout_wheel;
    private ServoQUS locker_left;
    private ServoQUS locker_right;

    //// MOTORS ////
    private DcMotorQUS front_lift;
    private DcMotorQUS back_lift;

    public int ring_detector_distance;

    public int loaded_distance;
    public boolean loaded;

    public boolean forced_chop = false;

    public long chopper_timer = 0;

    {
        name = "Intake";
    }

    public Intake(Robot robot) {
        super(robot);
    }

    @Override
    public void registerHardware(HardwareMap hwmap) {
        super.registerHardware(hwmap);

        //// SERVOS ////
        chopper    = new ServoQUS(hwmap.get(Servo.class, "chopper"));
        roller     = new CRServoQUS(hwmap.get(CRServo.class, "roller"));
        popout_joint = new ServoQUS(hwmap.get(Servo.class, "popout_joint"));
        popout_wheel = new CRServoQUS(hwmap.get(CRServo.class, "popout_wheel"));
        locker_left = new ServoQUS(hwmap.get(Servo.class, "locker_left"));
        locker_right = new ServoQUS(hwmap.get(Servo.class, "locker_right"));

        //// MOTORS ////
        front_lift      = new DcMotorQUS(hwmap.get(DcMotorEx.class, "front_lift"));
        back_lift       = new DcMotorQUS(hwmap.get(DcMotorEx.class, "back_lift"));
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);

        ring_detector_distance = robot.bulk_data_1.getAnalogInputValue(1);
        loaded_distance = robot.bulk_data_2.getAnalogInputValue(1);

        loaded = loaded_distance > IntakeConfig.loaded_focal_length;

        long time_waited_to_chop = (System.currentTimeMillis() - chopper_timer);

        if ((ring_detector_distance > IntakeConfig.chopper_focal_length && time_waited_to_chop > IntakeConfig.chopper_delay) || forced_chop) {
            // alternate chopping and unchopping every chopper_repeat_interval milliseconds
            boolean should_be_chopped = ((int) ((time_waited_to_chop - IntakeConfig.chopper_delay) / IntakeConfig.chopper_repeat_interval)) % 2 == 0;

            if (should_be_chopped) {
                chop();
            } else {
                unchop();
            }
        } else {
            unchop();
            if (ring_detector_distance <= IntakeConfig.chopper_focal_length) { // Bad code, will fix later
                chopper_timer = System.currentTimeMillis();
            }
        }

        front_lift.update();
        back_lift.update();
        chopper.update();
        roller.update();
        popout_joint.update();
        popout_wheel.update();
        locker_left.update();
        locker_right.update();
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);

        telemetry.addData("RDT", ring_detector_distance);
        telemetry.addData("CT", chopper_timer);
        telemetry.addData("LD", loaded);
    }

    @Override
    public void startup() {
        super.startup();
        popin();
        unchop();
        lock_lockers();
    }

    public void chop() {
        chopper.queue_position(IntakeConfig.chopper_chopped);
    }


    public void unchop() {
        chopper.queue_position(IntakeConfig.chopper_unchopped);
    }

    public void spin_lift(double dir) {
        front_lift.queue_power(-1 * IntakeConfig.lift_speed * dir);
        back_lift.queue_power(1 * IntakeConfig.lift_speed * dir);
    }

    public void spin(double dir) {
        spin_lift(dir);
        roller.queue_power(IntakeConfig.roller_speed * dir);
    }

    public void stop_lift() {
        front_lift.queue_power(0);
        back_lift.queue_power(0);
    }

    public void popout() {
        popout_joint.queue_position(IntakeConfig.popout_out);
        popout_wheel.queue_power(IntakeConfig.popout_speed);
    }

    public void popin() {
        popout_joint.queue_position(IntakeConfig.popout_in);
        popout_wheel.queue_power(0);
    }

    public void lock_lockers() {
        locker_left.queue_position(IntakeConfig.locker_left_locked);
        locker_right.queue_position(IntakeConfig.locker_right_locked);
    }

    public void unlock_lockers() {
        locker_left.queue_position(IntakeConfig.locker_left_unlocked);
        locker_right.queue_position(IntakeConfig.locker_right_unlocked);
    }

    public void stop() {
        stop_lift();
        roller.queue_power(0);
    }

    @Override
    public void shutdown() {
        stop();
        super.shutdown();
    }
}
