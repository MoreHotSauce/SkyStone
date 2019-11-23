package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


enum StateParkRed{
    START,
    CHECKHEADING,
    MOVEFROMWALL,
    STRAFETOPARK,
    PARK
}

@TeleOp(name="Autonomous Park Red", group="Auton Opmode")
public class AutonParkRed extends OpMode {

    Robot robot;

    private StateParkRed currentState;
    private final float YTOL = 1.0f;
    private final float RTOL = 1.0f;
    private final float XTOL = 1.0f;


    @Override
    public void init() {
        Component[] componentList = {
                new Motor(-1, "backLeft", hardwareMap, false),     //0
                new Motor(-1, "backRight", hardwareMap, true),   //1
                new Motor(-1, "frontLeft", hardwareMap, false),    //2
                new Motor(-1, "frontRight", hardwareMap, true),  //3
                new StepperServo(-1, "foundationHook", hardwareMap),      //4
                new StepperServo(-1, "chomper", hardwareMap),             //5
                new EMotor(-1, "actuator", hardwareMap, 1),        //6
                new Motor(-1, "liftMotor", hardwareMap, false)    //7
        };

        robot = new Robot(componentList, hardwareMap, true);
        telemetry.addData("Test", "Robot");

        currentState = StateParkRed.START;
        robot.chomperControl(false);
        robot.chomperControl(true);
        robot.chomperControl(false);
        robot.chomperControl(true);

    }

    @Override
    public void loop() {
        telemetry.addData("State", currentState);
        telemetry.addData("y-target", robot.targetY);
        telemetry.addData("r-target", robot.targetHeading);

        robot.updateLoop();
        robot.resetMotorSpeeds();
        float correctionR = robot.rotatePID();
        float correctionY = robot.moveTargetY();
        float correctionX = robot.moveTargetX();

        telemetry.addData("PositionY", robot.currentY);
        telemetry.addData("PositionX", robot.currentX);
        telemetry.addData("PositionTargetY", robot.targetY);
        telemetry.addData("PositionTargetX", robot.targetX);
        telemetry.addData("Rotation", robot.heading);
        telemetry.addData("RotationTarget", robot.targetHeading);
        telemetry.addData("CorrectionR", correctionR);
        telemetry.addData("CorrectionY", correctionY);
        telemetry.addData("CorrectionX", correctionX);

        robot.chomperControl(false);
        robot.foundationHookControl(false);

        switch(currentState){
            case START:
                robot.foundationHookControl(true);
                currentState = StateParkRed.CHECKHEADING;
                break;

            case CHECKHEADING:
                robot.changeTargetRotation(0.0f);
                if (tol(robot.heading , robot.targetHeading, RTOL)){
                    currentState = StateParkRed.MOVEFROMWALL;
                }
                break;

            case MOVEFROMWALL:
                robot.changeTargetY(6.0f);
                if(tol(robot.currentY, robot.targetY, YTOL)){
                    robot.changeTargetY(0.0f);
                    currentState = StateParkRed.STRAFETOPARK;
                }
                break;

            case STRAFETOPARK:
                robot.changeTargetX(-28.0f);
                if(tol(robot.currentX, robot.targetX, XTOL)){
                    robot.changeTargetX(0.0f);
                    currentState = StateParkRed.PARK;
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
