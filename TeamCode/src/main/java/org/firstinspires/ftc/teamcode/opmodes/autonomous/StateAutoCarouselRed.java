package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.constants.AutonomousConst;

@Autonomous(name="SA Carousel Red", group="autonomous")
public class StateAutoCarouselRed extends StateAutoCarousel {
    {
        color = AutonomousConst.RED;
    }
}
