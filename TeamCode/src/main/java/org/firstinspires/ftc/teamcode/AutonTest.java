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
                new Motor(-1, "backLeft", hardwareMap, true),
                new Motor(-1, "backRight", hardwareMap, false),
                new Motor(-1, "frontLeft", hardwareMap, true),
                new Motor(-1, "frontRight", hardwareMap, false),
                //new Motor(-1, "liftMotor", hardwareMap, false),
                new StepperServo(-1, "foundationHook", hardwareMap),
                new StepperServo(-1, "chomper", hardwareMap),
                new EMotor(-1, "actuator", hardwareMap, 1)
        };

        robot = new Robot(componentList, hardwareMap, true);
        telemetry.addData("Test", "Robot");
    }

    @Override
    public void loop() {
        robot.resetMotorSpeeds();
        float correctionR = robot.rotatePID();
        //robot.moveTargetY();

        telemetry.addData("frontRight", robot.drivetrain.frontRight.getEncoderValue());
        telemetry.addData("frontLeft", robot.drivetrain.frontLeft.getEncoderValue());
        telemetry.addData("backRight", robot.drivetrain.backRight.getEncoderValue());
        telemetry.addData("backRight", robot.drivetrain.backLeft.getEncoderValue());


        telemetry.addData("Rotation", robot.heading);
        telemetry.addData("RotationTarget", robot.targetHeading);
        telemetry.addData("CorrectionR", correctionR);
        telemetry.addData("backLeft", robot.drivetrain.backLeftSpeed);
        telemetry.addData("backRight", robot.drivetrain.backRightSpeed);
        telemetry.addData("frontLeft", robot.drivetrain.frontLeftSpeed);
        telemetry.addData("frontRight", robot.drivetrain.frontRightSpeed);


    }

}
