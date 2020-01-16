package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Blinker;

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
                new Motor(-1, "backLeft", hardwareMap, false),     //0
                new Motor(-1, "backRight", hardwareMap, true),   //1
                new Motor(-1, "frontLeft", hardwareMap, false),    //2
                new Motor(-1, "frontRight", hardwareMap, true),  //3
                new StepperServo(-1, "foundationHook", hardwareMap),      //4
                new StepperServo(-1, "intakeClawLeft", hardwareMap),      //5
                new EMotor(-1, "actuator", hardwareMap, 1),        //6
                new Motor(-1, "liftMotor", hardwareMap, false),   //7
                new Color(-1, "colorSensor", hardwareMap),                 //8
                new StepperServo(-1, "hugger", hardwareMap),               //9
                new Motor(-1, "liftMotor2", hardwareMap, true),     //10 ENCODER IS Y ODO
                new StepperServo(-1, "intakeClawRight", hardwareMap),      //11
                new StepperServo(-1, "odoServo", hardwareMap),             //12
                new Motor(-1, "fakeMotor", hardwareMap, true)                     // ENCODER IS X ODO
        };

        robot = new Robot(componentList, hardwareMap, false);
        telemetry.addData("Test", "Robot");
        telemetry.addData("avgRotation", robot.heading);
        telemetry.addData("x", robot.getOdoX());
        telemetry.addData("y", robot.getOdoY());
    }

    public void start(){
        robot.drivetrain.resetAllEncoders();
    }

    @Override
    public void loop() {
        robot.turbo(gamepad1.right_bumper);

        robot.intakeControl(gamepad2.a);

        robot.actuatorControl(gamepad2.x, gamepad2.b);
        //robot.incrementActuator(gamepad2.right_bumper, gamepad2.left_bumper);

        robot.moveLift(gamepad2.left_trigger, gamepad2.right_trigger);

        robot.foundationHookControl(gamepad1.y);

        if(gamepad1.dpad_down){
            robot.drive(0, 0.4f, 0f);
        }else if(gamepad1.dpad_up){
            robot.drive(0, -0.4f, 0f);
        } else if (gamepad1.dpad_right) {
            robot.drive(0.4f, 0f, 0f);
        } else if (gamepad1.dpad_left) {
            robot.drive(-0.4f, 0f, 0f);
        } else {
            robot.drive(gamepad1.left_stick_x, gamepad1.left_stick_y, -1*gamepad1.right_stick_x);
        }

        telemetry.addData("avgRotation", robot.heading);
        telemetry.addData("x", robot.getOdoX());
        telemetry.addData("y", robot.getOdoY());
        /*telemetry.addData("bl", robot.drivetrain.backLeft.getEncoderValue());
        telemetry.addData("br", robot.drivetrain.backRight.getEncoderValue());
        telemetry.addData("fl", robot.drivetrain.frontLeft.getEncoderValue());
        telemetry.addData("fr", robot.drivetrain.frontRight.getEncoderValue());*/
        telemetry.addData("totalMult", robot.colorSensor.getValue()[0] * robot.colorSensor.getValue()[1] * robot.colorSensor.getValue()[2]);
        telemetry.addData("skystone", robot.isSkystone());
        telemetry.update();
    }


}
