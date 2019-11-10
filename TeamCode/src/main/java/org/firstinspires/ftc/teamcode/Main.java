package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Main TeleOp", group="Juice TeleOp")
public class Main extends OpMode{

    Robot robot;

    /*
    GAMEPAD CONTROLS:

    GAMEPAD 1:
        left stick x = drive horizontal
        left stick y = drive vertical
        right stick x = rotation
        left trigger = lift down
        right trigger = lift up
        left bumper = slow speed
        right bumper = turbo speed
        a = intake claw
        b = foundation
        x
        y
        dpad
     */

    @Override
    public void init() {
        Component[] componentList = {
                new Motor(-1, "backLeft", hardwareMap, false),
                new Motor(-1, "backRight", hardwareMap, true),
                new Motor(-1, "frontLeft", hardwareMap, false),
                new Motor(-1, "frontRight", hardwareMap, true),
                //new Motor(-1, "liftMotor", hardwareMap, false),
                new StepperServo(-1, "foundationHook", hardwareMap),
                new StepperServo(-1, "chomper", hardwareMap),
                new EMotor(-1, "actuator", hardwareMap, 1)
        };

        robot = new Robot(componentList, hardwareMap, false);
        telemetry.addData("Test", "Robot");
        telemetry.addData("inchesForward", robot.drivetrain.getYDistance());
        telemetry.addData("frontRight", robot.drivetrain.frontRight.getEncoderValue());
        telemetry.addData("frontLeft", robot.drivetrain.frontLeft.getEncoderValue());
        telemetry.addData("backRight", robot.drivetrain.backRight.getEncoderValue());
        telemetry.addData("backLeft", robot.drivetrain.backLeft.getEncoderValue());
    }

    public void start(){

    }

    @Override
    public void loop() {
        robot.turbo(gamepad1.right_bumper);
        robot.drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        robot.chomperControl(gamepad1.x);
        //robot.moveLift(gamepad1.left_trigger, gamepad1.right_trigger);
        robot.foundationHookControl(gamepad1.b);
        if(gamepad1.dpad_down){

        }else if(gamepad1.dpad_up){

        }

        telemetry.addData("servo", robot.chomper.getAngle());
        telemetry.addData("inchesForward", robot.drivetrain.getYDistance());
        telemetry.addData("frontRight", robot.drivetrain.frontRight.getEncoderValue());
        telemetry.addData("frontLeft", robot.drivetrain.frontLeft.getEncoderValue());
        telemetry.addData("backRight", robot.drivetrain.backRight.getEncoderValue());
        telemetry.addData("backLeft", robot.drivetrain.backLeft.getEncoderValue());

        telemetry.update();
    }
}
