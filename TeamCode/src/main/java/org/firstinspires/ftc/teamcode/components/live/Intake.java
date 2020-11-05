package org.firstinspires.ftc.teamcode.components.live;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;

@Config
class IntakeConfig {

}

public class Intake extends Component {

    //// SERVOS ////
    private CRServo front_intake;
    private CRServo back_intake;
    private CRServo front_lift;
    private CRServo back_lift;

    //// SENSORS ////

    {
        name = "Intake";
    }

    public Intake(Robot robot) {
        super(robot);
    }

    @Override
    public void registerHardware(HardwareMap hwmap) {
        super.registerHardware(hwmap);

        //// MOTORS ////
        front_intake    = hwmap.get(CRServo.class, "front_intake");
        back_intake     = hwmap.get(CRServo.class, "back_intake");
        front_lift      = hwmap.get(CRServo.class, "front_lift");
        back_lift       = hwmap.get(CRServo.class, "back_lift");
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);
    }

    @Override
    public void startup() {
        super.startup();
    }

    public void spin() {
        front_intake.setPower(-1);
        back_intake.setPower(1);
        front_lift.setPower(-1);
        back_lift.setPower(1);
    }

    public void stop() {
        front_intake.setPower(0);
        back_intake.setPower(0);
        front_lift.setPower(0);
        back_lift.setPower(0);
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }
}
