package org.firstinspires.ftc.teamcode.systems.pathfollowing;

// A point in 2D space, has an x and y coordinate

public class Point {

    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0, 0);
    }

    // Check equality with another point, (same coordinates)
    public boolean equals(Point p2) {
        return this.x == p2.x && this.y == p2.y;
    }

    // Check if this point falls within a specified circle
    public boolean within_circle(double x, double y, double r) {
        return Math.abs(Math.hypot(this.x-x, this.y-y)) <= r;
    }

    // Convert this point to a pose with specified angle
    public Pose to_pose(double angle) {
        return new Pose(x, y, angle);
    }
}
