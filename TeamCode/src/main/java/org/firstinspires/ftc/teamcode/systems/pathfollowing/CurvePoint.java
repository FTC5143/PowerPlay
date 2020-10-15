package org.firstinspires.ftc.teamcode.systems.pathfollowing;

public class CurvePoint extends Point {

    // The speed we drive on the segment following this curve point
    public double speed = 1;

    // Whether the robot has "passed" this point
    public boolean passed = false;

    public CurvePoint(double x, double y) {
        super(x, y);
    }

    // Easy public speed setter
    public CurvePoint speed(double speed) {
        this.speed = speed;
        return this;
    }

}
