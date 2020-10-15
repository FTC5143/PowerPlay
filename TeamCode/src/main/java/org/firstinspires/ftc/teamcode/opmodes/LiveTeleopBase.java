package org.firstinspires.ftc.teamcode.opmodes;

import android.util.ArrayMap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robots.LiveRobot;

public abstract class LiveTeleopBase extends LinearOpMode {

    protected LiveRobot robot;

    private ArrayMap<Double, Runnable> todo_tasks = new ArrayMap<>();

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new LiveRobot(this);
        robot.startup();

        on_init();

        waitForStart();
        on_start();

        while(opModeIsActive() && !isStopRequested()) {

            on_loop();

            for(int entry = 0; entry < todo_tasks.size(); entry++) {
                if(todo_tasks.keyAt(entry) <= getRuntime()) {
                    todo_tasks.valueAt(entry).run();
                    todo_tasks.remove(todo_tasks.keyAt(entry));
                }
            }

        }
        on_stop();

        robot.shutdown();

        stop();
    }

    // Called when init is pressed, runs once
    public abstract void on_init();

    // Called when start is pressed, runs once
    public abstract void on_start();

    // Called when the program stops, runs once
    public abstract void on_stop();

    // Called repeatedly while the program is running
    public abstract void on_loop();

    public void run_in(Runnable command, double milliseconds) {
        todo_tasks.put(getRuntime()+((milliseconds)/1000.0), command);
    }
}
