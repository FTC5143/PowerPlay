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
    public static double lift_speed = 0.85;
    public static double chopper_speed = 0.55;
    public static double roller_speed = -1.0;

    public static double chopper_chopped = 1;
    public static double chopper_unchopped = 0;

    public static double popout_in = 0.20;
    public static double popout_out = 0.70;
    public static double popout_speed = -1.0;
}

public class Intake extends Component {

    //// SERVOS ////
    private ServoQUS chopper;
    private CRServoQUS roller;
    private ServoQUS popout_joint;
    private CRServoQUS popout_wheel;

    //// MOTORS ////
    private DcMotorQUS front_lift;
    private DcMotorQUS back_lift;

    public int ring_detector_distance;

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

        //// MOTORS ////
        front_lift      = new DcMotorQUS(hwmap.get(DcMotorEx.class, "front_lift"));
        back_lift       = new DcMotorQUS(hwmap.get(DcMotorEx.class, "back_lift"));
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);

        front_lift.update();
        back_lift.update();
        chopper.update();
        roller.update();
        popout_joint.update();
        popout_wheel.update();

        ring_detector_distance = robot.bulk_data_1.getAnalogInputValue(1);

        if (ring_detector_distance > 25) {
            chop();
        } else {
            unchop();
        }
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);

        telemetry.addData("RDT", ring_detector_distance);
    }

    @Override
    public void startup() {
        super.startup();
        popin();
        unchop();
    }

    public void chop() {
        chopper.queue_position(IntakeConfig.chopper_chopped);
    }


    public void unchop() {
        chopper.queue_position(IntakeConfig.chopper_unchopped);
    }

    public void spin_lift(int dir) {
        front_lift.queue_power(-1 * IntakeConfig.lift_speed * dir);
        back_lift.queue_power(1 * IntakeConfig.lift_speed * dir);
    }

    public void spin(int dir) {
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
