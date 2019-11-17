package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

enum State{ //Maybe add wait states
    START,
    CHECKHEADING,
    MOVEFROMWALL,
    ROTATE180,
    MOVETOFOUNDATION,
    HOOKFOUNDATION,
    WAIT1,
    MOVETOWALL,
    RELEASEHOOK,
    STRAFETOGATE,
    REVERSETOGATELINEUP,
    STRAFETOPARK,
    PARK
}


@Autonomous(name="Autonomous Foundation and Park", group="Auton Opmode")
public class AutonFoundationPark extends OpMode {

    Robot robot;

    private State currentState;
    private final float YTOL = 1.0f;
    private final float RTOL = 0.5f;


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

        currentState = State.START;
    }

    @Override
    public void loop() {
        robot.updateLoop();
        robot.resetMotorSpeeds();
        robot.chomperControl(false);
        robot.foundationHookControl(false);

        switch(currentState){
            case START:
                currentState = State.CHECKHEADING;
                break;

            case CHECKHEADING:
                robot.changeTargetRotation(180.0f);
                if (tol(robot.heading , robot.targetHeading, RTOL)){
                    currentState = State.MOVEFROMWALL;
                }
                break;

            case MOVEFROMWALL:
                robot.changeTargetY(12.0f);
                if(tol(robot.currentY, robot.targetY, YTOL){
                    robot.changeTargetY(0.0f);
                    currentState = State.ROTATE180;
                }
                break;

            case ROTATE180:
                robot.changeTargetRotation(0.0f);
                if(tol(robot.heading, robot.targetHeading, RTOL)){
                    currentState = State.MOVETOFOUNDATION;
                }
                break;

            case MOVETOFOUNDATION:
                robot.changeTargetY(-12.0f);
                if(tol(robot.currentY, robot.targetY, YTOL){
                    robot.changeTargetY(0.0f);
                    currentState = State.HOOKFOUNDATION;
                }
                break;

            case HOOKFOUNDATION:
                robot.foundationHookControl(true);
                currentState = State.WAIT1;
                break;

            case WAIT1:
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentState = State.MOVETOWALL;
                break;

            case MOVETOWALL:
                robot.changeTargetY(24.0f);
                if(tol(robot.currentY, robot.targetY, YTOL){
                    robot.changeTargetY(0.0f);
                    currentState = State.HOOKFOUNDATION;
                }
                break;

            //TODO: Continue Implementation of cases
        }
    }

    public static boolean tol(float current, float target, float tolerance){
        return Math.abs(current - target) <= tolerance;
    }

}
