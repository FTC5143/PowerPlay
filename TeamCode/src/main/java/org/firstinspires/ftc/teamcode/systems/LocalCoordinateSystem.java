package org.firstinspires.ftc.teamcode.systems;

public class LocalCoordinateSystem {
    public double x = 0;    // The approximated x position of the robot relative to where it started
    public double y = 0;    // The approximated y position of the robot relative to where it started
    public double a = 0;    // The approximated heading of the robot relative to its initial heading

    public double prev_le;
    public double prev_re;
    public double prev_ce;


    private double ENCODER_CPR          = 1440;             // Counts per full rotation of an encoder
    private double ROBOT_DIAMETER       = 15.75716126;      //15.7075609922;    //15.74735 //15.53           // Distance between the left and right encoder (diameter) in inches
    private double CENTER_WHEEL_OFFSET  = 7.594180357;      //7.725136416;      //7.719 //7.375 Distance of the center encoder to the line made between the left and right encoders (radius) in inches

    private double WHEEL_DIAMETER_L     = 1.487592046; //1.49420962888;
    private double WHEEL_DIAMETER_R     = 1.495218487; //1.49420962888;
    private double WHEEL_DIAMETER_C     = 1.484900111; //1.49420962888;

    private double INCHES_PER_COUNT_L   = WHEEL_DIAMETER_L * Math.PI / ENCODER_CPR;
    private double INCHES_PER_COUNT_R   = WHEEL_DIAMETER_R * Math.PI / ENCODER_CPR;
    private double INCHES_PER_COUNT_C   = WHEEL_DIAMETER_C * Math.PI / ENCODER_CPR;

    public void update(double le, double re, double ce) {

        // Calculate encoder deltas
        double ld = le - prev_le;
        double rd = re - prev_re;
        double cd = ce - prev_ce;

        // Calculate phi, or the delta of our angle
        double ph = (rd * INCHES_PER_COUNT_R - ld * INCHES_PER_COUNT_L) / ROBOT_DIAMETER;

        // The arclength of movement forward/backward
        double dc = (rd * INCHES_PER_COUNT_R + ld * INCHES_PER_COUNT_L) / 2;

        // The arclength of movement left/right
        double sc = (cd * INCHES_PER_COUNT_C) + (ph * CENTER_WHEEL_OFFSET);

        // Calculate the new angle of the robot using the difference between the left and right encoder
        a = (re * INCHES_PER_COUNT_R - le * INCHES_PER_COUNT_L) / ROBOT_DIAMETER;

        // Calculate the new position of the robot by adding the arc vector to the absolute pos
        double sinph = Math.sin(ph);
        double cosph = Math.cos(ph);

        double s;
        double c;

        // If the arc turn is small enough, do this instead to avoid a div by zero error
        if(Math.abs(ph) < 1E-9) {
            s = 1.0 - 1.0 / 6.0 * ph * ph;
            c = 0.5 * ph;
        } else {
            s = sinph / ph;
            c = (1.0 - cosph) / ph;
        }

        // Find our x and y translations relative to the origin pose (0,0,0)
        double rel_x = sc * s - dc * c;
        double rel_y = sc * c - dc * s;

        // Transform those x and y translations to the actual rotation of our robot, and translate our robots positions to the new spot
        x -= rel_x * Math.cos(a) - rel_y * Math.sin(a);
        y -= rel_x * Math.sin(a) + rel_y * Math.cos(a);

        /* OLD BAD BAD BAD CODE THAT DOESN'T REALLY WORK AT ALL REALLY
        y += (dc * Math.cos(a + (ph / 2))) - (sc * Math.sin(a + (ph / 2)));
        x -= (dc * Math.sin(a + (ph / 2))) + (sc * Math.cos(a + (ph / 2)));
        */

        // Used to calculate deltas for next loop
        prev_le = le;
        prev_re = re;
        prev_ce = ce;

    }
}
