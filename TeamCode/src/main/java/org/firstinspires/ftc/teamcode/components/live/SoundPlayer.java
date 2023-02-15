package org.firstinspires.ftc.teamcode.components.live;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.opMode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;

public class SoundPlayer extends Component {
    /**
     * Sounds to help with robot debug
     */

    int vineboom_sound_id;
    int skypecall_sound_id;
    int whopper_sound_id;

    {
        name = "SoundPlayer";
    }

    public SoundPlayer(Robot robot) {
        super(robot);
    }

    @Override
    public void registerHardware (HardwareMap hwmap) {
        super.registerHardware(hwmap);

        vineboom_sound_id = hwmap.appContext.getResources().getIdentifier("vineboom","raw", hwmap.appContext.getPackageName());
        com.qualcomm.ftccommon.SoundPlayer.getInstance().preload(hwmap.appContext, vineboom_sound_id);

        skypecall_sound_id = hwmap.appContext.getResources().getIdentifier("skype","raw", hwmap.appContext.getPackageName());
        com.qualcomm.ftccommon.SoundPlayer.getInstance().preload(hwmap.appContext, skypecall_sound_id);

        whopper_sound_id = hwmap.appContext.getResources().getIdentifier("whopper","raw", hwmap.appContext.getPackageName());
        com.qualcomm.ftccommon.SoundPlayer.getInstance().preload(hwmap.appContext, whopper_sound_id);
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);

        telemetry.addData("BOOMER", "Ready to boom");
    }


    public void vineboom() {
        /**
         * Plays vine boom sound effect, move along nothing to see here
         */
        com.qualcomm.ftccommon.SoundPlayer.getInstance().startPlaying(robot.opmode.hardwareMap.appContext, vineboom_sound_id);
    }

    public void skypecall() {
        //yknow?
        com.qualcomm.ftccommon.SoundPlayer.getInstance().startPlaying(robot.opmode.hardwareMap.appContext, skypecall_sound_id);
    }

    public void whopper() {
        //perhap
        com.qualcomm.ftccommon.SoundPlayer.getInstance().startPlaying(robot.opmode.hardwareMap.appContext, whopper_sound_id);
    }
}
