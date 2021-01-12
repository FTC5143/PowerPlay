package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Preloaded Wobble Goal", group = "autonomous")
public class PreloadedWobbleGoal extends UltimateGoalAuto {
    {
        preloaded_wobble_goal = true;
        second_wobble_goal = false;
        shoot_preloaded_rings = true;
    }
}
