package org.firstinspires.ftc.teamcode.components.debug;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;

public class SensorDebug extends Component {

    ColorSensor color_sensor;
    RevTouchSensor touch_sensor;
    DistanceSensor distance_sensor;


    {
        name = "Sensor Debug";
    }

    public SensorDebug(Robot robot) {
        super(robot);
    }

    @Override
    public void registerHardware(HardwareMap hwmap) {
        super.registerHardware(hwmap);

        color_sensor = hwmap.get(ColorSensor.class, "color_sensor");
        touch_sensor = hwmap.get(RevTouchSensor.class, "touch_sensor");
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);

        telemetry.addData("color", color_sensor.red()+","+color_sensor.green()+","+color_sensor.blue());
        telemetry.addData("touch", touch_sensor.isPressed());

    }


    public boolean isSkystone() {
        return (color_sensor.red() + color_sensor.green() + color_sensor.blue()) < 50;
    }
}
