package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//DEPRICATED
@TeleOp(name="Main TeleOp Nov 15", group="Juice TeleOp")
public class MainTempNov15 extends OpMode{

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
                new Motor(-1, "backLeft", hardwareMap, false),     //0
                new Motor(-1, "backRight", hardwareMap, true),   //1
                new Motor(-1, "frontLeft", hardwareMap, false),    //2
                new Motor(-1, "frontRight", hardwareMap, true),  //3
                new StepperServo(-1, "foundationHook", hardwareMap),      //4
                new StepperServo(-1, "chomper", hardwareMap),             //5
                new EMotor(-1, "actuator", hardwareMap, 1),        //6
                new Motor(-1, "liftMotor", hardwareMap, false)    //7
        };

        robot = new Robot(componentList, hardwareMap, false);
        telemetry.addData("Test", "Robot");
        telemetry.addData("avgRotation", robot.drivetrain.getYDistance());
    }

    public void start(){
        robot.drivetrain.resetAllEncoders();
    }

    @Override
    public void loop() {
        robot.turbo(gamepad1.right_bumper);

        robot.drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        robot.chomperControl(gamepad1.b);

        robot.actuatorControl(gamepad1.x, gamepad1.y);

        robot.moveLift(gamepad1.left_trigger, gamepad1.right_trigger);

        robot.foundationHookControl(gamepad1.a);

        if(gamepad1.dpad_down){
            robot.drive(0, -0.1f, 0f);
        }else if(gamepad1.dpad_up){
            robot.drive(0, -0.1f, 0f);
        } else if (gamepad1.dpad_right) {
            robot.drive(0.1f, 0f, 0f);
        } else if (gamepad1.dpad_left) {
            robot.drive(-0.1f, 0f, 0f);
        }

        telemetry.addData("servo", robot.chomper.getAngle());
        telemetry.addData("inchesForward", robot.drivetrain.getYDistance());
        telemetry.addData("bl", robot.drivetrain.backLeft.getEncoderValue());
        telemetry.addData("br", robot.drivetrain.backRight.getEncoderValue());
        telemetry.addData("fl", robot.drivetrain.frontLeft.getEncoderValue());
        telemetry.addData("fr", robot.drivetrain.frontRight.getEncoderValue());
        telemetry.update();
    }


}
