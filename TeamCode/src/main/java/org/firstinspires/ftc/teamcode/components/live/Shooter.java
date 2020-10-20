package org.firstinspires.ftc.teamcode.components.live;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;

public class Shooter extends Component {

    private final int TARGET_SPEED = 2200; // counts per second

    //// MOTORS ////
    private DcMotorEx flywheel;   // Flywheel

    private PIDCoefficients flywheel_pid_coeffs = new PIDCoefficients(10, 1, 5);

    {
        name = "Shooter";
    }

    public Shooter(Robot robot) {
        super(robot);
    }

    @Override
    public void registerHardware(HardwareMap hwmap) {
        super.registerHardware(hwmap);

        //// MOTORS ////
        flywheel = hwmap.get(DcMotorEx.class, "flywheel");
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);

        telemetry.addData("FLYWHEEL VEL", robot.bulk_data_2.getMotorVelocity(flywheel));
    }

    @Override
    public void startup() {
        super.startup();

        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);

        flywheel.setVelocityPIDFCoefficients(
                flywheel_pid_coeffs.p,
                flywheel_pid_coeffs.i,
                flywheel_pid_coeffs.d,
                0 // no f
        );

        flywheel.setVelocity(TARGET_SPEED);

    }

    @Override
    public void shutdown() {
        super.shutdown();

        flywheel.setPower(0);
    }
}
