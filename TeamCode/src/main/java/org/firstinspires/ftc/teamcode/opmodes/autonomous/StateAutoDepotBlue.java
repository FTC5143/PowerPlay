package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.constants.AutonomousConst;

@Autonomous(name="SA Depot Blue", group="autonomous")
public class StateAutoDepotBlue extends StateAutoDepot {
    {
        color = AutonomousConst.BLUE;
    }
}
