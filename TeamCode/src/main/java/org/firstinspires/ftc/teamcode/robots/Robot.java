package org.firstinspires.ftc.teamcode.robots;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Component;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevBulkData;

import java.util.ArrayList;

import static org.firstinspires.ftc.teamcode.robots.RobotConfig.*;

@Config
class RobotConfig {
    public static int COMPONENT_UPDATE_CYCLE = 1;
    public static int BULK_READ_1_CYCLE = 1;
    public static int BULK_READ_2_CYCLE = 5;
    public static int TELEMETRY_CYCLE = 20;
    public static int FREQ_CHECK_CYCLE = 20;
}

public class Robot {

    HardwareMap hwmap;
    public OpMode opmode;
    public LinearOpMode lopmode;

    ArrayList<Component> components = new ArrayList<>();

    public String name;

    boolean running = false;

    protected ExpansionHubEx expansion_hub_1;
    protected ExpansionHubEx expansion_hub_2;

    Telemetry telemetry;

    public RevBulkData bulk_data_1;
    public RevBulkData bulk_data_2;

    protected long last_update = System.nanoTime();
    protected int update_freq = 0;

    public int cycle = 0;

    Runnable update_thread;

    // The Update Thread
    // Should be called as fast as possible. Does all reads and writes to the rev hub
    class UpdateThread implements Runnable {

        Robot robot;

        public UpdateThread(Robot robot) {
            super();
            this.robot = robot;
        }

        @Override
        public void run() {
            while(robot.running) {
                robot.update();
            }
        }
    }

    public Robot(OpMode opmode) { // FIRST THIS IS *YOUR* FAULT IT HAS TO BE THIS WAY AND I HATE IT AND YOU SHOULD BE ASHAMED
        if (opmode instanceof LinearOpMode) {
            this.lopmode = (LinearOpMode) opmode;
            this.telemetry = this.lopmode.telemetry;
        } else {
            this.opmode = opmode;
            this.telemetry = this.opmode.telemetry;
        }

        this.hwmap  = opmode.hardwareMap;
        registerHardware(this.hwmap);
    }

    public void startup() {
        running = true;

        for (Component component : components) {
            component.startup();
        }

        update_thread = new UpdateThread(this);

        new Thread(update_thread).start();
    }

    public void shutdown() {
        running = false;

        for (Component component : components) {
            component.shutdown();
        }
    }

    // This method is called as fast as possible by the update thread
    public void update() {

        // Bulk read from rev hub 1
        if(cycle % BULK_READ_1_CYCLE == 0) {
            bulk_data_1 = expansion_hub_1.getBulkInputData();
        }

        // Bulk read from rev hub 2
        if(cycle % BULK_READ_2_CYCLE == 0) {
            bulk_data_2 = expansion_hub_2.getBulkInputData();
        }

        // Call update on every single component
        if(cycle % COMPONENT_UPDATE_CYCLE == 0) {
            for (Component component : components) {
                component.update(opmode);
            }
        }

        // Update telemetry on the dashboard and on the phones
        if (cycle % TELEMETRY_CYCLE == 0) {
            updateTelemetry();

            for (Component component : components) {
                component.updateTelemetry(telemetry);
            }

            telemetry.update();
        }

        // Recalculate our update thread frequency
        if (cycle % FREQ_CHECK_CYCLE == 0) {
            long update_duration = System.nanoTime()-last_update;
            update_freq = ((update_duration/(double)1000000000) * 20) != 0 ? (int)((1/(update_duration/(double)1000000000)) * 20) : Integer.MAX_VALUE;

            last_update = System.nanoTime();
        }

        // This robot cycle is complete, increment our cycle counter by one
        cycle++;
    }

    // For updating the telemetry on the phones
    public void updateTelemetry() {
        telemetry.addData("[RBT "+name+"]", components.size()+" components");
        telemetry.addData("FREQ", update_freq);
    }

    // This should automatically be called whenever you make a new component attached to a robot instance
    // Basically just adds the component to a list of registered components, and attaches all hardware the component needs from the configuration
    public void registerComponent(Component component) {
        component.registerHardware(hwmap);
        components.add(component);
    }

    // Called on robot startup, registers all hardware the robot instance needs to use, in this case both the rev hubs
    public void registerHardware(HardwareMap hwmap) {
        expansion_hub_1 = hwmap.get(ExpansionHubEx.class, "Expansion Hub 1");
        expansion_hub_2 = hwmap.get(ExpansionHubEx.class, "Expansion Hub 2");
    }
}
