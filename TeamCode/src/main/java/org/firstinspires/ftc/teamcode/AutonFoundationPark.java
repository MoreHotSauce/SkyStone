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
    private float heading = 0.0f, targetHeading = 0.0f;
    private Gyro gyro;

    private PIDController pidRotation;

    private State currentState, nextState;

    private float distanceFromWall = 0f;
    private float distanceFromRotate = 0f;

    private final float FOUNDATIONDISTANCE = 4f; //TODO: Change to actual distance
    private final float DISTANCETOROTATE = 1f; //TODO: Change to actual distance

    @Override
    public void init() {
        Component[] componentList = {
                new Motor(-1, "backLeft", hardwareMap, false),
                new Motor(-1, "backRight", hardwareMap, true),
                new Motor(-1, "frontLeft", hardwareMap, false),
                new Motor(-1, "frontRight", hardwareMap, true)
        };

        robot = new Robot(componentList, hardwareMap);
        telemetry.addData("Init", "Robot initalized");

        heading = robot.gyro.getHeading();
        targetHeading = 45.0f;

        pidRotation = new PIDController(targetHeading, 0.55, 0.00000, 0.0);

        currentState = State.START;
        nextState = State.START;
    }

    @Override
    public void loop() {
        currentState = nextState;

        switch(currentState){
            case START:
                nextState = State.CHECKHEADING;
                break;

            case CHECKHEADING:
                if (heading == 180.0f){
                    nextState = State.MOVEFROMWALL;
                } else {
                    rotatePID(180.0f);
                }
                break;

            case MOVEFROMWALL:
                if (distanceFromWall == DISTANCETOROTATE){
                    nextState = State.ROTATE180;
                } else {
                    movePID(180, DISTANCETOROTATE);
                }
                break;

            case ROTATE180:
                if (heading == 0.0f) {
                    nextState = State.MOVETOFOUNDATION;
                } else {
                    rotatePID(0.0f);
                }
                break;

            case MOVETOFOUNDATION:
                if (distanceFromRotate == FOUNDATIONDISTANCE - DISTANCETOROTATE){
                    nextState = State.HOOKFOUNDATION;
                } else {
                    movePID(0.0f, FOUNDATIONDISTANCE - DISTANCETOROTATE);
                }
                break;

            //TODO: Continue Implementation of cases
        }
    }

    private void movePID(float targetHeading, float distance) {
        //TODO: Implement move to a distance with PID
        //TODO: Update distanceFromWall
    }

    public void rotate(){
        if (heading < targetHeading){
            robot.startRotation(true);
        } else if (heading > targetHeading){
            robot.startRotation(false);
        } else {
            robot.stop();
        }
    }

    public void rotatePID(float targetHeading){
        //TODO: Implement rotation with PID
    }

}
