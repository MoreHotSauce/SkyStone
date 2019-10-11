package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="AutonTest", group="Linear Opmode")
public class AutonTest extends LinearOpMode {

    Robot robot;

    @Override
    public void runOpMode(){
        Component[] componentList = {
                new Motor(-1, "backLeft", hardwareMap, false),
                new Motor(-1, "backRight", hardwareMap, true),
                new Motor(-1, "frontLeft", hardwareMap, false),
                new Motor(-1, "frontRight", hardwareMap, true)
        };

        robot = new Robot(componentList);
        telemetry.addData("Test", "Robot");

        robot.drivetrain.startRotation(true);
        sleep(1000);
        robot.drivetrain.stop();
    }

}
