package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.vision.SkystoneDetector;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;


enum StateSkystoneBlue{ //Maybe add wait states
    START,
    CHECKHEADING,
    MOVETOSTONES,
    YOINKERSPT1,
    MOVETOFOUNDATION,
    STRAFETOFOUNDATION,
    DROPSTONE,
    FIXROTATION,
    MOVETOSTONE2,
    STARFETOSTONE2,
    YOINKERSPT2,
    MOVETOFOUNDATION2,
    STRAFETOFOUNDATION2,
    DROPSTONE2,
    BACKFROMFOUNDATION,
    ROTATE90,
    MOVETOGRABFOUNDATION,
    GRABFOUNDATION,
    PLACEFOUNDATION,
    UNGRABFOUNDATION,
    PUSHFOUNDATION,
    MOVETOBRIDGE,
    PARK
}

@TeleOp(name="1 Stone Blue", group="Auton Opmode")
public class AutonSkystoneFoundationParkBlue extends OpMode {

    Robot robot;
    OpenCvCamera camera;
    SkystoneDetector pipeline;

    private StateSkystoneBlue currentState;
    private final float YTOL = 1.0f;
    private final float RTOL = 3.0f;
    private final float XTOL = 1.0f;

    public float averagePosition;


    @Override
    public void init() {
        robot = new Robot(hardwareMap, false);
        telemetry.addData("Test", "Robot");

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        camera.openCameraDevice();

        pipeline = new SkystoneDetector(25, 25, 50, 50, null);

        camera.setPipeline(pipeline);
        camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);

        currentState = StateSkystoneBlue.START;


    }

    @Override
    public void start(){
        /*
        SkystoneDetector.SkystonePosition skyPos = pipeline.getSkystonePosition();
        int sigma = 0;
        int count = 0;
        for(int i = 0; i<20;i++){
            skyPos = pipeline.getSkystonePosition();

            if(skyPos == SkystoneDetector.SkystonePosition.LEFT_STONE){
                sigma += 1;
            } else if (skyPos == SkystoneDetector.SkystonePosition.CENTER_STONE) {
                sigma += 2;
            } else if (skyPos == SkystoneDetector.SkystonePosition.RIGHT_STONE){
                sigma += 3;
            }

            count++;

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        averagePosition = ((float)sigma)/count;
        */

        SkystoneDetector.SkystonePosition skyPos = pipeline.getSkystonePosition();

        if(skyPos == SkystoneDetector.SkystonePosition.LEFT_STONE){
            averagePosition = 1;
        } else if (skyPos == SkystoneDetector.SkystonePosition.CENTER_STONE) {
            averagePosition = 2;
        } else if (skyPos == SkystoneDetector.SkystonePosition.RIGHT_STONE) {
            averagePosition = 3;
        }

    }

    @Override
    public void loop() {
        telemetry.addData("State", currentState);
        telemetry.addData("y-target", robot.targetY);
        telemetry.addData("x-target", robot.targetX);
        telemetry.addData("r-target", robot.targetR);

        robot.updateLoop();
        robot.resetMotorSpeeds();

        telemetry.addData("PositionY", robot.currentY);
        telemetry.addData("PositionX", robot.currentX);
        telemetry.addData("PositionTargetY", robot.targetY);
        telemetry.addData("PositionTargetX", robot.targetX);
        telemetry.addData("Rotation", robot.currentR);
        telemetry.addData("RotationTarget", robot.targetR);



        robot.foundationHookControl(false);

        switch(currentState){
            case START:
                currentState = StateSkystoneBlue.CHECKHEADING;
                break;

            case CHECKHEADING:
                robot.switchHuggers(false, true);
                robot.huggerControl(false,true);
                robot.huggerControl(false,false);
                robot.changeTarget(0.0f, 0.0f, 0.0f);
                if (tol(robot.currentR , robot.targetR, RTOL)){
                    currentState = StateSkystoneBlue.MOVETOSTONES;
                }
                break;

            case MOVETOSTONES:
                if(averagePosition == 3){ //MV RT
                    robot.changeTarget(-26,4,0);
                    if (tol(robot.currentX, robot.targetX, 1) && tol(robot.currentY, robot.targetY, 1)){
                        robot.changeTarget(0,0,0);
                        currentState=StateSkystoneBlue.YOINKERSPT1;
                    }
                } else if (averagePosition == 2){ //MV CNT
                    robot.changeTarget(-26,-4,0);
                    if (tol(robot.currentX, robot.targetX, 1) && tol(robot.currentY, robot.targetY, 1)){
                        robot.changeTarget(0,0,0);
                        currentState=StateSkystoneBlue.YOINKERSPT1;
                    }
                } else { //MV LT
                    robot.changeTarget(-26,-12,0);
                    if (tol(robot.currentX, robot.targetX, 1) && tol(robot.currentY, robot.targetY, 1)){
                        robot.changeTarget(0,0,0);
                        currentState=StateSkystoneBlue.YOINKERSPT1;
                    }
                }
                break;

            case YOINKERSPT1:
                robot.huggerControl(true,false);
                robot.huggerControl(false,false);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.huggerControl(false,true);
                robot.huggerControl(false,false);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.huggerControl(true,false);
                robot.huggerControl(false,false);
                currentState = StateSkystoneBlue.MOVETOFOUNDATION;
                break;

            case MOVETOFOUNDATION:
                if(averagePosition == 3){
                    robot.changeTarget(3,-88,0);
                    if (tol(robot.currentX, robot.targetX, 1) && tol(robot.currentY, robot.targetY, 2)){
                        robot.changeTarget(0,0,0);
                        currentState=StateSkystoneBlue.STRAFETOFOUNDATION;
                    }
                } else if(averagePosition == 2){
                    robot.changeTarget(3,-82,0);
                    if (tol(robot.currentX, robot.targetX, 1) && tol(robot.currentY, robot.targetY, 2)){
                        robot.changeTarget(0,0,0);
                        currentState=StateSkystoneBlue.STRAFETOFOUNDATION;
                    }
                } else {
                    robot.changeTarget(3,-74,0);
                    if (tol(robot.currentX, robot.targetX, 1) && tol(robot.currentY, robot.targetY, 2)){
                        robot.changeTarget(0,0,0);
                        currentState=StateSkystoneBlue.STRAFETOFOUNDATION;
                    }
                }


                break;

            case STRAFETOFOUNDATION:
                robot.changeTarget(-8,0,0);
                if (tol(robot.currentX, robot.targetX, 2) && tol(robot.currentY, robot.targetY, 5)){
                    robot.changeTarget(0,0,0);
                    currentState=StateSkystoneBlue.DROPSTONE;
                }
                break;

            case DROPSTONE:
                robot.huggerControl(true,false);
                robot.huggerControl(false,false);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.huggerControl(false,true);
                robot.huggerControl(false,false);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.huggerControl(true,false);
                robot.huggerControl(false,false);
                currentState=StateSkystoneBlue.BACKFROMFOUNDATION;
                break;

            /*case FIXROTATION:
                robot.changeTarget(0,0,0);
                if (tol(robot.currentR, robot.targetR, 1)){
                    robot.changeTarget(0,0,0);
                    currentState=StateSkystoneBlue.MOVETOSTONE2;
                }
                break;

             */

            case MOVETOSTONE2:
                robot.changeTarget(8,105,0);
                if (tol(robot.currentX, robot.targetX, 1) && tol(robot.currentY, robot.targetY, 2)){
                    robot.changeTarget(0,0,0);
                    currentState=StateSkystoneBlue.STARFETOSTONE2;
                }
                break;

            case STARFETOSTONE2:
                robot.changeTarget(-10,0,0);
                if (tol(robot.currentX, robot.targetX, 3) && tol(robot.currentY, robot.targetY, 2)){
                    robot.changeTarget(0,0,0);
                    currentState=StateSkystoneBlue.YOINKERSPT2;
                }
                break;

            case YOINKERSPT2:
                robot.huggerControl(true,false);
                robot.huggerControl(false,false);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.huggerControl(false,true);
                robot.huggerControl(false,false);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.huggerControl(true,false);
                robot.huggerControl(false,false);
                currentState = StateSkystoneBlue.MOVETOFOUNDATION2;
                break;

            case MOVETOFOUNDATION2:
                robot.changeTarget(2,-99,0);
                if (tol(robot.currentX, robot.targetX, 1) && tol(robot.currentY, robot.targetY, 5)){
                    robot.changeTarget(0,0,0);
                    currentState=StateSkystoneBlue.STRAFETOFOUNDATION2;
                }
                break;

            case STRAFETOFOUNDATION2:
                robot.changeTarget(-14,0,0);
                if (tol(robot.currentX, robot.targetX, 4) && tol(robot.currentY, robot.targetY, 5)){
                    robot.changeTarget(0,0,0);
                    currentState=StateSkystoneBlue.DROPSTONE2;
                }
                break;

            case DROPSTONE2:
                robot.huggerControl(true,false);
                robot.huggerControl(false,false);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.huggerControl(false,true);
                robot.huggerControl(false,false);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.huggerControl(true,false);
                robot.huggerControl(false,false);
                currentState=StateSkystoneBlue.PARK;
                break;

            case BACKFROMFOUNDATION:
                robot.changeTarget(9,5,0);
                if (tol(robot.currentX, robot.targetX, 2) && tol(robot.currentY, robot.targetY, 2)){
                    robot.changeTarget(0,0,0);
                    currentState=StateSkystoneBlue.ROTATE90;
                }
                break;

            case ROTATE90:
                robot.setAutonRotating(true);
                robot.changeTarget(0,0,90);
                if (tol(robot.currentR, robot.targetR, 4)){
                    robot.setAutonRotating(false);
                    //robot.changeTarget(0,0,0);
                    currentState=StateSkystoneBlue.MOVETOGRABFOUNDATION;
                }
                break;

            case MOVETOGRABFOUNDATION:
                robot.changeTarget(5,15,90);
                if (tol(robot.currentX, robot.targetX, 2) && tol(robot.currentY, robot.targetY, 6) && tol(robot.currentR, robot.targetR, 4)){
                    //robot.changeTarget(0,0,0);
                    currentState=StateSkystoneBlue.GRABFOUNDATION;
                }
                break;

            case GRABFOUNDATION:
                robot.foundationHookControl(false);
                robot.foundationHookControl(true);
                robot.foundationHookControl(false);
                robot.foundationHookControl(true);
                robot.foundationHookControl(false);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentState=StateSkystoneBlue.PLACEFOUNDATION;

            case PLACEFOUNDATION:
                robot.changeTarget(0,-40,180);
                if (tol(robot.currentX, robot.targetX, 5) && tol(robot.currentY, robot.targetY, 5) && tol(robot.currentR, robot.targetR, 4)){
                    //robot.changeTarget(0,0,0);
                    currentState=StateSkystoneBlue.UNGRABFOUNDATION;
                }
                break;

            case UNGRABFOUNDATION:
                robot.foundationHookControl(false);
                robot.foundationHookControl(true);
                robot.foundationHookControl(false);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentState=StateSkystoneBlue.PUSHFOUNDATION;

            case PUSHFOUNDATION:
                robot.changeTarget(0,20,180);
                if (tol(robot.currentX, robot.targetX, 4) && tol(robot.currentY, robot.targetY, 7) && tol(robot.currentR, robot.targetR, 4)){
                    //robot.changeTarget(0,0,0);
                    currentState=StateSkystoneBlue.MOVETOBRIDGE;
                }
                break;

            case MOVETOBRIDGE:
                robot.changeTarget(0,-40,180);
                if (tol(robot.currentX, robot.targetX, 4) && tol(robot.currentY, robot.targetY, 9) && tol(robot.currentR, robot.targetR, 4)){
                    //robot.changeTarget(0,0,0);
                    currentState=StateSkystoneBlue.PARK;
                }
                break;

            case PARK:
                break;
        }
    }

    public static boolean tol(float current, float target, float tolerance){
        return Math.abs(current - target) <= tolerance;
    }



}
