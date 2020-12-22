package org.firstinspires.ftc.teamcode.components.live;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.qus.CRServoQUS;

@Config
class IntakeConfig {
    public static double lift_speed = 1.0;
}

public class Intake extends Component {

    //// SERVOS ////
    private CRServoQUS front_intake;
    private CRServoQUS back_intake;
    private CRServoQUS chopper;

    //// MOTORS ////
    private DcMotorEx front_lift;
    private DcMotorEx back_lift;

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

        //// SERVOS ////
        front_intake    = new CRServoQUS(hwmap.get(CRServo.class, "front_intake"));
        back_intake    = new CRServoQUS(hwmap.get(CRServo.class, "back_intake"));
        chopper    = new CRServoQUS(hwmap.get(CRServo.class, "chopper"));

        //// MOTORS ////
        front_lift      = hwmap.get(DcMotorEx.class, "front_lift");
        back_lift       = hwmap.get(DcMotorEx.class, "back_lift");
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);

        front_intake.update();
        back_intake.update();
        chopper.update();
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
            front_intake.queue_power(1);
            back_intake.queue_power(-1);
        }
    }

    public void spin_lift() {
        if (!spinning_lift) {
            spinning_lift = true;
            front_lift.setPower(-1 * IntakeConfig.lift_speed);
            back_lift.setPower(1 * IntakeConfig.lift_speed);
        }
    }

    public void spin() {
        spin_intake();
        spin_lift();
        chopper.queue_power(1);
    }

    public void stop_intake() {
        if (spinning_intake) {
            spinning_intake = false;
            front_intake.queue_power(0);
            back_intake.queue_power(0);
        }
    }


    public void stop_lift() {
        if (spinning_lift) {
            spinning_lift = false;
            front_lift.setPower(0);
            back_lift.setPower(0);
        }
    }

    public void stop() {
        stop_intake();
        stop_lift();
        chopper.queue_power(0);
    }

    @Override
    public void shutdown() {
        stop();
        super.shutdown();
    }
}
