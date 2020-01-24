package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

enum StateBlueSkystone{ //Maybe add wait states
    START,
    CHECKHEADING,
    STRAFETOSKYSTONE,
    SKYSTONECHECKING1,
    SKYSTONECHECKING21,
    SKYSTONECHECKING22,
    SKYSTONECHECKING3,
    MOVEINTO,
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
@TeleOp
public class AutonSkystoneFoundationBlue extends OpMode {

    Robot robot;

    private StateBlueSkystone currentState;

    private final float YTOL = 1.0f;
    private final float RTOL = 3.0f;
    private final float XTOL = 1.0f;

    private float moveMore = 0.0f;


    @Override
    public void init() {
        Component[] componentList = {
                new Motor(-1, "backLeft", hardwareMap, false),              //0
                new Motor(-1, "backRight", hardwareMap, true),              //1
                new Motor(-1, "frontLeft", hardwareMap, false),             //2
                new Motor(-1, "frontRight", hardwareMap, true),             //3
                new StepperServo(-1, "foundationHook", hardwareMap),                //4
                new StepperServo(-1, "hugger", hardwareMap),                        //5
                new EMotor(-1, "actuator", hardwareMap, 1),                  //6
                new Motor(-1, "liftMotor", hardwareMap, false),             //7
                new Motor(-1, "liftMotor2", hardwareMap, true),            //8
                new StepperServo(-1, "intakeClawLeft", hardwareMap),                //9
                new StepperServo(-1, "intakeClawRight", hardwareMap),               //10
                new StepperServo(-1, "odoServo", hardwareMap),                      //11
                new Motor(-1, "fakeMotor", hardwareMap, true),              //12
                new Color(-1, "colorSensor", hardwareMap)                           //13
        };

        robot = new Robot(componentList, hardwareMap, true);
        telemetry.addData("Test", "Robot");

        currentState = StateBlueSkystone.START;
    }

    @Override
    public void loop() {
        telemetry.addData("PositionY", robot.currentY);
        telemetry.addData("PositionTargetY", robot.targetY);
        telemetry.addData("CorrectionY", robot.correctionY);
        telemetry.addData("highestY", robot.highestY);
        telemetry.addData("PositionX", robot.currentX);
        telemetry.addData("PositionTargetX", robot.targetX);
        telemetry.addData("CorrectionX", robot.correctionX);
        telemetry.addData("highestX", robot.highestX);
        //telemetry.addData("PositionX", robot.currentX);
        //telemetry.addData("PositionTargetX", robot.targetX);
        telemetry.addData("Rotation", robot.heading);
        telemetry.addData("RotationTarget", robot.targetHeading);
        telemetry.addData("CorrectionR", robot.correctionR);
        // telemetry.addData("backLeft", robot.drivetrain.backLeftSpeed);
        //telemetry.addData("backRight", robot.drivetrain.backRightSpeed);
        //telemetry.addData("frontLeft", robot.drivetrain.frontLeftSpeed);
        //telemetry.addData("frontRight", robot.drivetrain.frontRightSpeed);
        telemetry.addData("totalMult", robot.colorSensor.getValue()[0] * robot.colorSensor.getValue()[1] * robot.colorSensor.getValue()[2]);
        //telemetry.addData("alpha", robot.colorSensor.getValue()[0]);
        //telemetry.addData("red", robot.colorSensor.getValue()[1]);
        //telemetry.addData("green", robot.colorSensor.getValue()[2]);
        //telemetry.addData("blue", robot.colorSensor.getValue()[3]);
        telemetry.addData("skystone?", robot.isSkystone());
        telemetry.addData("STATE", currentState);

        robot.updateLoop();
        robot.resetMotorSpeeds();

        robot.foundationHookControl(false);

        switch(currentState){
            case START:
                currentState = StateBlueSkystone.CHECKHEADING;
                break;

            case CHECKHEADING:
                if (tol(robot.heading , robot.targetHeading, RTOL)){
                    currentState = StateBlueSkystone.STRAFETOSKYSTONE;
                }
                break;

            case STRAFETOSKYSTONE:
                robot.changeTargetX(25.0f);
                if (tol(robot.currentX , robot.targetX, XTOL)){
                    robot.changeTargetX(0.0f);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentState = StateBlueSkystone.SKYSTONECHECKING1;
                }
                break;

            case SKYSTONECHECKING1:
                if (robot.isSkystone()) {
                    currentState = StateBlueSkystone.MOVEINTO;
                    moveMore = 16.0f;
                } else {
                    currentState = StateBlueSkystone.SKYSTONECHECKING21;
                }
                break;

            case SKYSTONECHECKING21:
                robot.changeTargetY(8.0f);
                if (tol(robot.currentX , robot.targetX, XTOL)){
                    robot.changeTargetX(0.0f);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentState = StateBlueSkystone.SKYSTONECHECKING22;
                }
                break;

            case SKYSTONECHECKING22:
                if (robot.isSkystone()) {
                    currentState = StateBlueSkystone.MOVEINTO;
                    moveMore = 8.0f;
                } else {
                    currentState = StateBlueSkystone.SKYSTONECHECKING3;
                }
                break;

            case SKYSTONECHECKING3:
                robot.changeTargetY(8.0f);
                if (tol(robot.currentY , robot.targetY, YTOL)){
                    moveMore = 8.0f;
                    robot.changeTargetY(0.0f);
                    currentState = StateBlueSkystone.MOVEINTO;
                }

            case MOVEINTO:
                robot.changeTargetX(6.0f);
                if (tol(robot.currentX , robot.targetX, XTOL)){
                    robot.changeTargetX(0.0f);
                    currentState = StateBlueSkystone.YOINK;
                }
                break;

            case YOINK:
                robot.huggerControl(true);
                robot.huggerControl(false);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentState = StateBlueSkystone.PARK;
                break;

            /*case STRAFEBACK:
                robot.changeTargetX(-12.0f);
                if (tol(robot.currentX , robot.targetX, XTOL)){
                    robot.changeTargetX(0.0f);
                    currentState = StateBlueSkystone.ROT90CCW;
                }
                break;

            case ROT90CCW:
                robot.changeTargetRotation(-90.0f);
                if (tol(robot.heading , robot.targetHeading, RTOL)){
                    currentState = StateBlueSkystone.PARK;
                }
                break;

            case STRAFETOFOUNDATION:
                robot.changeTargetX(-32.0f - moveMore);
                if (tol(robot.currentX , robot.targetX, XTOL)){
                    robot.changeTargetX(0.0f);
                    currentState = StateBlueSkystone.PARK;
                }
                break;*/

            case PARK:
                break;
        }
    }

    public static boolean tol(float current, float target, float tolerance){
        return Math.abs(current - target) <= tolerance;
    }
}
