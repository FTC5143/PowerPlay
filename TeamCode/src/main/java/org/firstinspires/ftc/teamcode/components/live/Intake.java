package org.firstinspires.ftc.teamcode.components.live;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.qus.DcMotorQUS;

@Config
class IntakeConfig {
}

public class Intake extends Component {

    //// MOTORS ////
    public DcMotorQUS spinner;

    {
        name = "Intake";
    }

    public Intake(Robot robot)
    {
        super(robot);
    }

    @Override
    public void registerHardware (HardwareMap hwmap)
    {
        super.registerHardware(hwmap);

        //// MOTORS ////
        spinner     = new DcMotorQUS(hwmap.get(DcMotorEx.class, "spinner"));
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);

        spinner.update();
    }

    @Override
    public void startup() {
        super.startup();

        spinner.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        spinner.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void shutdown() {
        spinner.queue_power(0);
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);
    }

    public void spin(double speed) {
        spinner.queue_power(speed);
    }
}