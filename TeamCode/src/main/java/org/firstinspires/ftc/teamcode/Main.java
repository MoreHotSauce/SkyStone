package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Main TeleOp", group="Juice TeleOp")
public class Main extends OpMode {

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
                new Motor(-1, "backRight", hardwareMap, false),
                new Motor(-1, "frontLeft", hardwareMap, false),
                new Motor(-1, "frontRight", hardwareMap, false)
        };

        robot = new Robot(componentList);
        telemetry.addData("Test", "Robot");
    }

    public void start(){

    }

    @Override
    public void loop() {
        robot.turbo(gamepad1.right_bumper);
        robot.drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
    }
}
