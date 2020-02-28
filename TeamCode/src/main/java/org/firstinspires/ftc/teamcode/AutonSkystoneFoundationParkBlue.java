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
    PARK
}
@Disabled
@TeleOp(name="Autonomous Skystone Foundation Blue", group="Auton Opmode")
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
        Component[] componentList = {
                new Motor(-1, "backLeft", hardwareMap, false),              //0
                new Motor(-1, "backRight", hardwareMap, true),              //1
                new Motor(-1, "frontLeft", hardwareMap, false),             //2
                new Motor(-1, "frontRight", hardwareMap, true),             //3
                new StepperServo(-1, "foundationHook", hardwareMap),                //4
                new StepperServo(-1, "huggerRMain", hardwareMap),                   //5
                new EMotor(-1, "actuator", hardwareMap, 1),                  //6
                new Motor(-1, "liftMotor", hardwareMap, false),             //7
                new Motor(-1, "liftMotor2", hardwareMap, true),             //8
                new StepperServo(-1, "intakeClawLeft", hardwareMap),                //9
                new StepperServo(-1, "intakeClawRight", hardwareMap),               //10
                new StepperServo(-1, "odoServo", hardwareMap),                      //11
                new Motor(-1, "fakeMotor", hardwareMap, true),              //12
                new Color(-1, "colorSensor", hardwareMap),                          //13
                new StepperServo(-1, "huggerRArm", hardwareMap),                    //14
                new StepperServo(-1, "huggerLMain", hardwareMap),                   //15
                new StepperServo(-1, "huggerLArm", hardwareMap),                    //16
        };

        robot = new Robot(componentList, hardwareMap, false);
        telemetry.addData("Test", "Robot");

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        camera.openCameraDevice();

        pipeline = new SkystoneDetector(25, 25, 50, 50, null);

        camera.setPipeline(pipeline);
        camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);

        robot.foundationHookControl(true);
        robot.foundationHookControl(false);

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
                robot.changeTarget(0.0f, 0.0f, 0.0f);
                if (tol(robot.currentR , robot.targetR, RTOL)){
                    currentState = StateSkystoneBlue.MOVETOSTONES;
                }
                break;

            case MOVETOSTONES:
                if(averagePosition == 3){ //MV RT
                    robot.changeTarget(28,4,0);
                    if (tol(robot.currentX, robot.targetX, 2) && tol(robot.currentY, robot.targetY, 1)){
                        currentState=StateSkystoneBlue.YOINKERSPT1;
                    }
                } else if (averagePosition == 2){ //MV CNT
                    robot.changeTarget(28,-4,0);
                    if (tol(robot.currentX, robot.targetX, 2) && tol(robot.currentY, robot.targetY, 1)){
                        currentState=StateSkystoneBlue.YOINKERSPT1;
                    }
                } else { //MV LT
                    robot.changeTarget(28,-12,0);
                    if (tol(robot.currentX, robot.targetX, 2) && tol(robot.currentY, robot.targetY, 1)){
                        currentState=StateSkystoneBlue.YOINKERSPT1;
                    }
                }
                break;

            case YOINKERSPT1:
                currentState = StateSkystoneBlue.PARK;
                break;

            case PARK:
                break;
        }
    }

    public static boolean tol(float current, float target, float tolerance){
        return Math.abs(current - target) <= tolerance;
    }

}
