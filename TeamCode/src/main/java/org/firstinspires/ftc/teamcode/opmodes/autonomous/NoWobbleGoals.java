package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "No Wobble Goals", group = "autonomous")
public class NoWobbleGoals extends UltimateGoalAuto {
    {
        preloaded_wobble_goal = false;
        second_wobble_goal = false;
        shoot_preloaded_rings = true;
    }
}
