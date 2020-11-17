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

    private boolean spinning_intake = false;
    private boolean spinning_lift = false;

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

    public void spin_intake() {
        if (!spinning_intake) {
            spinning_intake = true;
            front_intake.setPower(1);
            back_intake.setPower(-1);
        }
    }

    public void spin_lift() {
        if (!spinning_lift) {
            spinning_lift = true;
            front_lift.setPower(-1);
            back_lift.setPower(1);
        }
    }

    public void spin() {
        spin_intake();
        spin_lift();
    }

    public void stop_intake() {
        if (spinning_intake) {
            spinning_intake = false;
            front_intake.setPower(0);
            back_intake.setPower(0);
        }
    }


    public void stop_lift() {
        if (spinning_lift) {
            spinning_lift = false;
            front_lift.setPower(0);
            back_lift.setPower(0);
        };
    }

    public void stop() {
        stop_intake();
        stop_lift();
    }

    @Override
    public void shutdown() {
        stop();
        super.shutdown();
    }
}
