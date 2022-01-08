package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.constants.AutonomousConst;
import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

@Autonomous(name="FF B L Block Top", group="autonomous")
public class PlaceBlockAutoBlueLeft extends PlaceBlockParkSquareAuto {
    {
        color = AutonomousConst.BLUE;
        side = AutonomousConst.LEFT;
    }
}
