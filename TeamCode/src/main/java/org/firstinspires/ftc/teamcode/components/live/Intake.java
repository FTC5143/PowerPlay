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
import org.firstinspires.ftc.teamcode.util.qus.DcMotorQUS;

@Config
class IntakeConfig {
    public static double lift_speed = 0.85;
    public static double chopper_speed = 0.5;
}

public class Intake extends Component {

    //// SERVOS ////
    private CRServoQUS chopper;

    //// MOTORS ////
    private DcMotorQUS front_lift;
    private DcMotorQUS back_lift;

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
        chopper    = new CRServoQUS(hwmap.get(CRServo.class, "chopper"));

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
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);
    }

    @Override
    public void startup() {
        super.startup();
    }


    public void spin_lift(int dir) {
        if (!spinning_lift) {
            spinning_lift = true;
            front_lift.queue_power(-1 * IntakeConfig.lift_speed * dir);
            back_lift.queue_power(1 * IntakeConfig.lift_speed * dir);
        }
    }

    public void spin(int dir) {
        spin_lift(dir);
        chopper.queue_power(IntakeConfig.chopper_speed * dir);
    }

    public void stop_lift() {
        if (spinning_lift) {
            spinning_lift = false;
            front_lift.queue_power(0);
            back_lift.queue_power(0);
        }
    }

    public void stop() {
        stop_lift();
        chopper.queue_power(0);
    }

    @Override
    public void shutdown() {
        stop();
        super.shutdown();
    }
}
