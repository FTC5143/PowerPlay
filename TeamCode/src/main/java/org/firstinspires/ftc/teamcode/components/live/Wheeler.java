package org.firstinspires.ftc.teamcode.components.live;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.qus.CRServoQUS;

@Config
class WheelerConfig {

}

public class Wheeler extends Component {

    //// SERVOS ////
    private CRServoQUS wheeler;

    {
        name = "Wheeler";
    }

    public Wheeler(Robot robot) {
        super(robot);
    }

    @Override
    public void registerHardware(HardwareMap hwmap) {
        super.registerHardware(hwmap);

        //// SERVOS ////
        wheeler = new CRServoQUS(hwmap.get(CRServo.class, "wheeler"));
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);
        wheeler.update();
    }

    public void spin(double speed) {
        /**
         * Spin the wheeler wheel at a given speed
         */
        wheeler.queue_power(speed);
    }
}
