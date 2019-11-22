package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;


@TeleOp(name="AutonTest", group="Auton Opmode")
public class AutonTest extends OpMode {

    Robot robot;

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
        robot.changeTargetX(-20.0f);
    }

    @Override
    public void loop() {
        robot.updateLoop();
        robot.resetMotorSpeeds();
        float correctionR = robot.rotatePID();
        float correctionY = robot.moveTargetY();
        float correctionX = robot.moveTargetX();


        telemetry.addData("Position", robot.currentY);
        telemetry.addData("PositionTarget", robot.targetY);
        telemetry.addData("Rotation", robot.heading);
        telemetry.addData("RotationTarget", robot.targetHeading);
        telemetry.addData("CorrectionR", correctionR);
        telemetry.addData("CorrectionY", correctionY);
        telemetry.addData("CorrectionX", correctionX);
        telemetry.addData("backLeft", robot.drivetrain.backLeftSpeed);
        telemetry.addData("backRight", robot.drivetrain.backRightSpeed);
        telemetry.addData("frontLeft", robot.drivetrain.frontLeftSpeed);
        telemetry.addData("frontRight", robot.drivetrain.frontRightSpeed);


    }

}
