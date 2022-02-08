package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.constants.AutonomousConst;

@Autonomous(name="SA Carousel Blue", group="autonomous")
public class StateAutoCarouselBlue extends StateAutoCarousel {
    {
        color = AutonomousConst.BLUE;
    }
}
