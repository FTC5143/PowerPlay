package org.firstinspires.ftc.teamcode.systems.pathfollowing;

// A Pose is a point with an angle

public class Pose extends Point {

    public double a;

    public Pose(double x, double y, double a) {
        this.x = x;
        this.y = y;
        this.a = a;
    }

    public Pose() {
        this(0, 0, 0);
    }

    public boolean equals(Pose p2) {
        return this.x == p2.x && this.y == p2.y && this.a == p2.a;
    }

}
