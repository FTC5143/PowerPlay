package org.firstinspires.ftc.teamcode.components.live;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.qus.CRServoQUS;
import org.firstinspires.ftc.teamcode.util.qus.DcMotorQUS;
import org.firstinspires.ftc.teamcode.util.qus.ServoQUS;

public class Capper extends Component {

    {
        name = "Capper";
    }

    CRServoQUS extender;
    CRServoQUS anglerh;
    ServoQUS anglerv;

    double anglerv_angle = 0.3;
    double anglerv_speed = 0;

    public Capper(Robot robot) {
        super(robot);
    }

    @Override
    public void registerHardware (HardwareMap hwmap)
    {
        super.registerHardware(hwmap);


        //// SERVOS ////
        extender = new CRServoQUS(hwmap.get(CRServo.class, "extender"));
        anglerh = new CRServoQUS(hwmap.get(CRServo.class, "anglerh"));
        anglerv = new ServoQUS(hwmap.get(Servo.class, "anglerv"));
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);

        anglerv_angle += anglerv_speed;

        anglerv.queue_position(anglerv_angle);

        extender.update();
        anglerh.update();
        anglerv.update();
    }

    public void extend(double speed) {
        extender.queue_power(speed);
    }

    public void spin(double speed) {
        anglerh.queue_power(speed);
    }

    public void rise(double speed) {
        anglerv_speed = speed;
    }
}
