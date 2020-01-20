package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

enum StateBlueSkystone{ //Maybe add wait states
    START,
    CHECKHEADING,
    STRAFETOSKYSTONE,
    SKYSTONECHECKING1,
    SKYSTONECHECKING2,
    SKYSTONECHECKING3,
    YOINK,
    STRAFEBACK,
    ROT90CCW,
    STRAFETOFOUNDATION,
    DROPSKYSTONE,
    PULLYBACKYSPINY,
    PUSHINTOWALL,
    STRAFELINEUP,
    FORWARDTOPARK,
    PARK
}

public class AutonSkystoneFoundationBlue extends OpMode {

    Robot robot;

    private StateBlueSkystone currentState;
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

        currentState = StateBlueSkystone.START;
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


        robot.foundationHookControl(false);

        switch(currentState){
            case START:
                currentState = StateBlueSkystone.CHECKHEADING;
                break;

            case CHECKHEADING:
                robot.changeTargetRotation(0.0f);
                if (tol(robot.heading , robot.targetHeading, RTOL)){
                    currentState = StateBlueSkystone.STRAFETOSKYSTONE;
                }
                break;

            case STRAFETOSKYSTONE:
                robot.changeTargetX(28.0f);
                if (tol(robot.currentX , robot.targetX, XTOL)){
                    robot.changeTargetX(0.0f);
                    currentState = StateBlueSkystone.SKYSTONECHECKING1;
                }
                break;

            case SKYSTONECHECKING1:
                currentState = robot.isSkystone() ? StateBlueSkystone.YOINK : StateBlueSkystone.SKYSTONECHECKING2;
                break;

            case SKYSTONECHECKING2:
                robot.changeTargetY(8.0f);
                if (tol(robot.currentY , robot.targetY, YTOL)){
                    robot.changeTargetX(0.0f);
                }
                currentState = robot.isSkystone() ? StateBlueSkystone.YOINK : StateBlueSkystone.SKYSTONECHECKING3;
                break;

            case SKYSTONECHECKING3:
                robot.changeTargetY(8.0f);
                if (tol(robot.currentY , robot.targetY, YTOL)){
                    robot.changeTargetX(0.0f);
                    currentState = StateBlueSkystone.YOINK;
                }

            case YOINK:
                robot.huggerControl

            case PARK:
                break;
        }
    }

    public static boolean tol(float current, float target, float tolerance){
        return Math.abs(current - target) <= tolerance;
    }
}
