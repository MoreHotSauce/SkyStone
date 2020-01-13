package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


enum StateRed{ //Maybe add wait states
    START,
    CHECKHEADING,
    MOVEFROMWALL,
    ROTATE180,
    LINEUPFOUNDATION,
    MOVETOFOUNDATION,
    HOOKFOUNDATION,
    WAIT1,
    MOVETOWALL,
    RELEASEHOOK,
    WAIT2,
    STRAFETOGATE,
    REVERSEFORPUSH,
    STRAFEFORPUSH,
    PUSH,
    STRAFETOPARK,
    PARK
}

@TeleOp(name="Autonomous Foundation and Park Red", group="Auton Opmode")
public class AutonFoundationParkRed extends OpMode {

    Robot robot;

    private StateRed currentState;
    private final float YTOL = 1.0f;
    private final float RTOL = 3.0f;
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

        currentState = StateRed.START;
        //telemetry.addData("ChomperPos", robot.chomper.servo.getPosition());
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
        //telemetry.addData("ChomperPos", robot.chomper.servo.getPosition());


        robot.foundationHookControl(false);

        switch(currentState){
            case START:
                currentState = StateRed.CHECKHEADING; //TEMP
                //robot.chomperControl(true);
                //robot.chomperControl(false);
                //robot.chomperControl(true);
                break;

            case CHECKHEADING:
                robot.changeTargetRotation(0.0f);
                if (tol(robot.heading , robot.targetHeading, RTOL)){
                    currentState = StateRed.MOVEFROMWALL;
                }
                break;

            case MOVEFROMWALL:
                robot.changeTargetY(-12.0f);
                if(tol(robot.currentY, robot.targetY, YTOL)){
                    robot.changeTargetY(0.0f);
                    currentState = StateRed.LINEUPFOUNDATION;
                }
                break;

            case LINEUPFOUNDATION:
                robot.changeTargetX(10.0f);
                if(tol(robot.currentX, robot.targetX, XTOL)){
                    robot.changeTargetX(0.0f);
                    currentState = StateRed.MOVETOFOUNDATION; //TEMP
                }
                break;

            case MOVETOFOUNDATION:
                robot.changeTargetY(-16.0f);
                if(tol(robot.currentY, robot.targetY, YTOL)){
                    robot.changeTargetY(0.0f);
                    robot.foundationHookControl(true);
                    robot.foundationHookControl(false);

                    currentState = StateRed.HOOKFOUNDATION;
                }
                break;

            case HOOKFOUNDATION:
                robot.foundationHookControl(true);
                currentState = StateRed.WAIT1;
                break;

            case WAIT1:
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentState = StateRed.MOVETOWALL;
                break;

            case MOVETOWALL:
                robot.changeTargetY(24.0f);
                if(tol(robot.currentY, robot.targetY, YTOL)){
                    robot.changeTargetY(0.0f);
                    currentState = StateRed.RELEASEHOOK;
                }
                break;

            case RELEASEHOOK:
                robot.foundationHookControl(true);
                currentState = StateRed.WAIT2;

            case WAIT2:
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentState = StateRed.STRAFETOGATE;
                break;

            case STRAFETOGATE:
                robot.changeTargetX(-24.0f);
                if(tol(robot.currentX, robot.targetX, XTOL)){
                    robot.changeTargetX(0.0f);
                    currentState = StateRed.REVERSEFORPUSH;
                }
                break;

            case REVERSEFORPUSH:
                robot.changeTargetY(-35.0f);
                if(tol(robot.currentY, robot.targetY, YTOL)){
                    robot.changeTargetY(0.0f);
                    currentState = StateRed.STRAFEFORPUSH;
                }
                break;

            case STRAFEFORPUSH:
                robot.changeTargetX(28.0f);
                if(tol(robot.currentX, robot.targetX, XTOL)){
                    robot.changeTargetX(0.0f);
                    currentState = StateRed.PUSH;
                }
                break;

            case PUSH:
                robot.changeTargetY(20.0f);
                if(tol(robot.currentY, robot.targetY, YTOL)){
                    robot.changeTargetY(0.0f);
                    currentState = StateRed.STRAFETOPARK;
                }
                break;

            case STRAFETOPARK:
                robot.changeTargetX(-45.0f);
                if(tol(robot.currentX, robot.targetX, XTOL)){
                    robot.changeTargetX(0.0f);
                    currentState = StateRed.PARK;
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
