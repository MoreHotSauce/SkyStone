package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


enum StateSkystoneBlue{ //Maybe add wait states
    START,
    CHECKHEADING,
    MOVETOSTONES,
    DETECTONE,
    MOVE2, PARK
}
@Disabled
@TeleOp(name="Autonomous Skystone Foundation Blue", group="Auton Opmode")
public class AutonSkystoneFoundationParkBlue extends OpMode {

    Robot robot;

    private StateSkystoneBlue currentState;
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

        currentState = StateSkystoneBlue.START;
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
                currentState = StateSkystoneBlue.CHECKHEADING;
                break;

            case CHECKHEADING:
                robot.changeTargetRotation(0.0f);
                if (tol(robot.currentR , robot.targetR, RTOL)){
                    currentState = StateSkystoneBlue.MOVETOSTONES;
                }
                break;

            case MOVETOSTONES:
                robot.changeTargetX(26);
                if(tol(robot.currentX, robot.targetX, XTOL)){
                    robot.changeTargetX(0);
                    currentState = StateSkystoneBlue.PARK;
                }
                break;

            case DETECTONE:
                if(robot.isSkystone()){
                    currentState = StateSkystoneBlue.PARK;
                } else {
                    currentState = StateSkystoneBlue.MOVE2;
                }
                break;

            case MOVE2:
                robot.changeTargetY(8);
                if(tol(robot.currentY, robot.targetY, YTOL)){
                    robot.changeTargetY(0);
                    currentState = StateSkystoneBlue.PARK;
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
