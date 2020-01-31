package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.detectors.skystone.SkystoneDetector;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import java.util.Locale;

/*
    POSITION 1: TOWARDS NEUTRAL BRIDGE
    POSITION 2: CENTER STONE
    POSITION 3: TOWARDS WALL
 */


public class SkystoneDetectionHelper {
    private OpenCvCamera phoneCam;
    private SkystoneDetector skyStoneDetector;

    public SkystoneDetectionHelper(HardwareMap map){
        int cameraMonitorViewId = map.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", map.appContext.getPackageName());
        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();
        skyStoneDetector = new SkystoneDetector();

        phoneCam.setPipeline(skyStoneDetector);
        phoneCam.startStreaming(720, 1080, OpenCvCameraRotation.UPRIGHT);
    }

    public float getStoneX() {
        return (float) skyStoneDetector.getScreenPosition().x;
    }

    public float getStoneY() {
        return (float) skyStoneDetector.getScreenPosition().y;
    }

    public int skystonePos() {
        if (this.getStoneX() < 100) { //BRIDGE
            return 1;
        } else if (this.getStoneX() > 100 && this.getStoneX() < 200){ //MIDDLE
            return 2;
        } else {
            return 3; //WALL
        }
    }

}
