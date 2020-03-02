package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


enum StatePark{
    START,
    CHECKHEADING,
    MOVETOPARK,
    PARK
}

@TeleOp(name="Park_L1", group="Auton Opmode")
public class AutonPark extends OpMode {

    Robot robot;

    private StatePark currentState;
    private final float YTOL = 1.0f;
    private final float RTOL = 1.0f;
    private final float XTOL = 1.0f;


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
                new LimitSensor(-1, "limit", hardwareMap)                           //17
        };

        robot = new Robot(componentList, hardwareMap, true);
        telemetry.addData("Test", "Robot");

        currentState = StatePark.START;

    }

    @Override
    public void loop() {

        robot.updateLoop();

        telemetry.addData("State", currentState);
        telemetry.addData("y-target", robot.targetY);
        telemetry.addData("r-target", robot.targetR);
        telemetry.addData("PositionY", robot.currentY);
        telemetry.addData("PositionX", robot.currentX);
        telemetry.addData("PositionTargetY", robot.targetY);
        telemetry.addData("PositionTargetX", robot.targetX);
        telemetry.addData("Rotation", robot.currentR);
        telemetry.addData("RotationTarget", robot.targetR);

        switch(currentState){
            case START:
                currentState = StatePark.CHECKHEADING;
                break;

            case CHECKHEADING:
                robot.changeTarget(0.0f, 0.0f, 0.0f);
                if (tol(robot.currentR , robot.targetR, RTOL)){
                    currentState = StatePark.MOVETOPARK;
                }
                break;

            case MOVETOPARK:
                robot.changeTarget(0.0f, -32.0f, 0.0f);
                if(tol(robot.currentY, robot.targetY, YTOL)){
                    currentState = StatePark.PARK;
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
