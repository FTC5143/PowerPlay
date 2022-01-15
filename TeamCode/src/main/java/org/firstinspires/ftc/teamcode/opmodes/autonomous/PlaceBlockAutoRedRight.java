package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.constants.AutonomousConst;
import org.firstinspires.ftc.teamcode.opmodes.LiveAutoBase;

@Autonomous(name="FF R R Block Top", group="autonomous")
public class PlaceBlockAutoRedRight extends PlaceBlockParkSquareAuto {
    {
        color = AutonomousConst.RED;
        side = AutonomousConst.RIGHT;
        duck = true;
    }
}
