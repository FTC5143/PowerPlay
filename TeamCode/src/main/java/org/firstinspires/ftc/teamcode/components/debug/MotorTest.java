package org.firstinspires.ftc.teamcode.components.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.qus.DcMotorQUS;

public class MotorTest extends Component {

    public MotorTest(Robot robot) {
        super(robot);
    }

    {
        name = "Motor Test";
    }

    private DcMotorQUS test_motor;   // Left-Front drive motor


    public void registerHardware(HardwareMap hwmap) {
        super.registerHardware(hwmap);

        //// MOTORS ////
        test_motor = new DcMotorQUS(hwmap.get(DcMotorEx.class, "test_motor"));

    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);

        test_motor.queue_power(opmode.gamepad1.left_stick_x);
        test_motor.update();
    }

    @Override
    public void shutdown() {
        super.shutdown();
        // Cut all motor powers on robot shutdown
        stop();
    }

    public void stop() {
        test_motor.motor.setPower(0);
    }

    private void set_mode(DcMotor.RunMode mode) {
        test_motor.motor.setMode(mode);
    }
}
