package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="Autonomous Park with Delay", group="Auton Opmode")
public class AutonParkDelay extends LinearOpMode {

    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        Component[] componentList = {
                new Motor(-1, "backLeft", hardwareMap, false),              //0
                new Motor(-1, "backRight", hardwareMap, true),              //1
                new Motor(-1, "frontLeft", hardwareMap, false),             //2
                new Motor(-1, "frontRight", hardwareMap, true),             //3
                new StepperServo(-1, "foundationHook", hardwareMap),                //4
                new StepperServo(-1, "hugger", hardwareMap),                        //5
                new EMotor(-1, "actuator", hardwareMap, 1),                  //6
                new Motor(-1, "liftMotor", hardwareMap, false),             //7
                new Motor(-1, "liftMotor2", hardwareMap, true),             //8
                new StepperServo(-1, "intakeClawLeft", hardwareMap),                //9
                new StepperServo(-1, "intakeClawRight", hardwareMap),               //10
                new StepperServo(-1, "odoServo", hardwareMap),                      //11
                new Motor(-1, "fakeMotor", hardwareMap, true),              //12
                new Color(-1, "colorSensor", hardwareMap)                           //13
        };

        robot = new Robot(componentList, hardwareMap, true);

        waitForStart();
        sleep(17000);
        robot.drivetrain.backLeft.setSpeed(-0.5f);
        robot.drivetrain.backRight.setSpeed(-0.5f);
        robot.drivetrain.frontLeft.setSpeed(-0.5f);
        robot.drivetrain.frontRight.setSpeed(-0.5f);
        sleep(700);
        robot.resetMotorSpeeds();
    }


    public static boolean tol(float current, float target, float tolerance){
        return Math.abs(current - target) <= tolerance;
    }

}
