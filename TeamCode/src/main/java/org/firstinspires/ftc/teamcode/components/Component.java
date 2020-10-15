package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.text.DecimalFormat;

public abstract class Component {

    protected final static int STATUS_OFFLINE = 0;
    protected final static int STATUS_ONLINE = 1;

    // The name of the Component, used in telemetry
    protected String name = "Component";

    // The string that appears in telemetry
    protected int status = STATUS_OFFLINE;

    // The robot that we are a part of
    protected Robot robot;

    protected final DecimalFormat TELEMETRY_DECIMAL = new DecimalFormat("##.00");

    // Trying to stray away from using the constructors at all in child classes
    public Component(Robot robot) {
        this.robot = robot;
        robot.registerComponent(this);
    }

    // Where all hardware used in the component is registered to its respective variable
    public void registerHardware(HardwareMap hwmap) {

    }

    // Called every time the robot update method is called
    public void update(OpMode opmode) {
    }

    // Called when robot.startup() is called, which should be called when an opmode is started
    public void startup() {
        status = STATUS_ONLINE;
    }

    // Called when robot.shutdown() is called, which should be called when an opmode is stopped
    public void shutdown() {
        status = STATUS_OFFLINE;
    }

    // Called on every update. Modify statusString here to update telemetry on the phone
    public void updateTelemetry(Telemetry telemetry) {
        telemetry.addData("[CMP "+name+"]", status == STATUS_ONLINE ? "ONLINE": "OFFLINE");
    }
}
