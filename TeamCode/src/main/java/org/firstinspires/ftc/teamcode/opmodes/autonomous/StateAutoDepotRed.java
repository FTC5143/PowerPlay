package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.constants.AutonomousConst;

@Autonomous(name="SA Depot Red", group="autonomous")
public class StateAutoDepotRed extends StateAutoDepot {
    {
        color = AutonomousConst.RED;
    }
}
