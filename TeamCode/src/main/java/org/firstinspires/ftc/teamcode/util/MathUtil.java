package org.firstinspires.ftc.teamcode.util;

public class MathUtil {
    // Wraps an angle in radians
    public static double angle_wrap(double angle) {
        return Math.atan2(Math.sin(angle), Math.cos(angle));
    }

    // Returns -1 if negative, 1 if 0 or positive
    public static double signum(double n) {
        if (n == 0) return 1.0;
        else return (double) Math.signum((float) n);
    }

    public static double angle_difference(double angle1, double angle2) { // dont ask
        double diff = (angle2 - angle1 + Math.PI) % (2*Math.PI) - Math.PI;
        return diff < -Math.PI ? diff + (2*Math.PI) : diff;
    }
}
