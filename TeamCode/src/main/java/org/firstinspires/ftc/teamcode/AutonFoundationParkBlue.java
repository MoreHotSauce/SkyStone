package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


enum StateBlue{ //Maybe add wait states
    START,
    CHECKHEADING,
    MOVEFROMWALL,
    STRAFELINEUP,
    MOVETOFOUNDATION,
    HOOK,
    MOVEBACK,
    ROT90,
    INTOWALL,
    UNHOOK,
    FORWARD,
    PARK
}
@Disabled
@TeleOp(name="Autonomous Foundation and Park Blue", group="Auton Opmode")
public class AutonFoundationParkBlue extends OpMode {

    Robot robot;

    private StateBlue currentState;
    private final float YTOL = 1.0f;
    private final float RTOL = 3.0f;
    private final float XTOL = 1.0f;


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

        robot.foundationHookControl(true);
        robot.foundationHookControl(false);

        currentState = StateBlue.START;
    }

    @Override
    public void loop() {
        telemetry.addData("State", currentState);
        telemetry.addData("y-target", robot.targetY);
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
                currentState = StateBlue.CHECKHEADING;
                break;

            case CHECKHEADING:
                robot.changeTargetRotation(0.0f);
                if (tol(robot.currentR , robot.targetR, RTOL)){
                    currentState = StateBlue.MOVEFROMWALL;
                }
                break;

            case MOVEFROMWALL:
                robot.changeTargetY(-20.0f);
                if(tol(robot.currentY, robot.targetY, YTOL)){
                    robot.changeTargetY(0.0f);
                    currentState = StateBlue.STRAFELINEUP;
                }
                break;

            case STRAFELINEUP:
                robot.changeTargetX(5.0f);
                if(tol(robot.currentX, robot.targetX, XTOL)) {
                    robot.changeTargetX(0.0f);
                    currentState = StateBlue.MOVETOFOUNDATION;
                }
                break;

            case MOVETOFOUNDATION:
                robot.changeTargetY(-8.0f);
                if(tol(robot.currentY, robot.targetY, YTOL)){
                    robot.changeTargetY(0.0f);
                    currentState = StateBlue.HOOK;
                }
                break;

            case HOOK:
                robot.foundationHookControl(true);
                robot.foundationHookControl(false);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentState = StateBlue.MOVEBACK;
                break;

            case MOVEBACK:
                robot.epicMode = true;
                robot.changeTargetY(12.0f);
                if(tol(robot.currentY, robot.targetY, YTOL)){
                    robot.changeTargetY(0.0f);
                    currentState = StateBlue.ROT90;
                    robot.epicMode = false;
                }
                break;

            case ROT90:
                robot.changeTargetRotation(90);
                if(tol(robot.currentR, robot.targetR, RTOL)){
                    currentState = StateBlue.UNHOOK;
                }
                break;

            case UNHOOK:
                robot.foundationHookControl(true);
                robot.foundationHookControl(false);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                currentState = StateBlue.INTOWALL;
                break;

            case INTOWALL:
                robot.epicMode = true;
                robot.changeTargetY(-12.0f);
                if(tol(robot.currentY, robot.targetY, YTOL)){
                    robot.changeTargetY(0.0f);
                    currentState = StateBlue.FORWARD;
                    robot.epicMode = false;
                }
                break;

            case FORWARD:
                robot.changeTargetY(40.0f);
                if(tol(robot.currentY, robot.targetY, YTOL)){
                    robot.changeTargetY(0.0f);
                    currentState = StateBlue.PARK;
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
