package org.firstinspires.ftc.teamcode.components.live;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.qus.CRServoQUS;
import org.firstinspires.ftc.teamcode.util.qus.ServoQUS;

@Config
class WobblerConfig {
    public static double grabbed = 0.48;
    public static double ungrabbed = 0.7;
}

public class Wobbler extends Component {

    //// SERVOS ////
    private ServoQUS grabber;
    private CRServoQUS flopper;

    {
        name = "Wobbler";
    }

    public Wobbler(Robot robot) {
        super(robot);
    }

    @Override
    public void registerHardware(HardwareMap hwmap) {
        super.registerHardware(hwmap);

        //// SERVOS ////
        grabber     = new ServoQUS(hwmap.get(Servo.class, "grabber"));
        flopper     = new CRServoQUS(hwmap.get(CRServo.class, "flopper"));
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);

        grabber.update();
        flopper.update();
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);
    }

    @Override
    public void startup() {
        super.startup();

        grabber.queue_position(WobblerConfig.ungrabbed);
        flopper.queue_power(0);
    }

    @Override
    public void shutdown() {
        super.shutdown();

        flopper.queue_power(0);
    }

    public void grab() {
        grabber.queue_position(WobblerConfig.grabbed);
    }

    public void ungrab() {
        grabber.queue_position(WobblerConfig.ungrabbed);
    }

}
