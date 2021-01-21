package org.firstinspires.ftc.teamcode.components.live;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.qus.ServoQUS;

@Config
class ShooterConfig {
    public static PIDCoefficients flywheel_pid_coeffs = new PIDCoefficients(250, 2, 30);

    public static int target_speed = 1720; // counts per second

    public static double shunter_unshot = 0.8;
    public static double shunter_shot = 0.35;


    public static double angler_pid_p = 6;

    public static int low_goal = 0;
    public static int mid_goal = 70;
    public static int high_goal = 1780;
    public static int power_shot = 1512;
}

public class Shooter extends Component {

    //// MOTORS ////
    private DcMotorEx flywheel;     // Flywheel
    private DcMotorEx angler;       // Motor to angle our barrel

    //// SERVOS ////
    private ServoQUS shunter;

    private int[] targets = {ShooterConfig.low_goal, ShooterConfig.mid_goal, ShooterConfig.high_goal, ShooterConfig.power_shot};
    public int shot_target = 0;

    int angler_zero = 0;

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
        flywheel    = hwmap.get(DcMotorEx.class, "flywheel");
        angler      = hwmap.get(DcMotorEx.class, "angler");

        //// SERVOS ////
        shunter     = new ServoQUS(hwmap.get(Servo.class, "shunter"));
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);

        if (!robot.bulk_data_2.getDigitalInputState(0)) {
            angler_zero = robot.bulk_data_2.getMotorCurrentPosition(angler);
        }

        shunter.update();
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);

        telemetry.addData("FLYWHEEL VEL", robot.bulk_data_2.getMotorVelocity(flywheel));

        telemetry.addData("ANGLER", robot.bulk_data_2.getMotorCurrentPosition(angler));

        telemetry.addData("ANGLER ZERO", angler_zero);
    }

    @Override
    public void startup() {
        super.startup();

        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);

        angler.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        angler.setTargetPosition(targets[shot_target % targets.length]);
        angler.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        angler.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        update_pid_coeffs();
    }

    public void update_pid_coeffs() {
        flywheel.setVelocityPIDFCoefficients(
                ShooterConfig.flywheel_pid_coeffs.p,
                ShooterConfig.flywheel_pid_coeffs.i,
                ShooterConfig.flywheel_pid_coeffs.d,
                0 // no f
        );

        angler.setPositionPIDFCoefficients(ShooterConfig.angler_pid_p);
    }

    @Override
    public void shutdown() {
        super.shutdown();

        flywheel.setPower(0);
    }

    public void spin() {
        flywheel.setVelocity(ShooterConfig.target_speed);
    }

    public void stop() {
        flywheel.setVelocity(0);
    }

    public void shoot() {
        shunter.queue_position(ShooterConfig.shunter_shot);
    }

    public void unshoot() {
        shunter.queue_position(ShooterConfig.shunter_unshot);
    }

    public void raise(int dir) {
        aim(shot_target + dir);
    }

    public void aim(int target) {
        targets[0] = ShooterConfig.low_goal; targets[1] = ShooterConfig.mid_goal; targets[2] = ShooterConfig.high_goal; targets[3] = ShooterConfig.power_shot;
        update_pid_coeffs();
        shot_target = target;
        angler.setTargetPosition(targets[shot_target % targets.length]);
        angler.setPower(1);
    }
}
