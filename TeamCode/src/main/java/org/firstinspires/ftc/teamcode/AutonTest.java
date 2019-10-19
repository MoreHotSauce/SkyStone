package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;


@TeleOp(name="AutonTest", group="Auton Opmode")
public class AutonTest extends OpMode {

    Robot robot;
    private float heading = 0.0f;
    private float targetHeading = 0.0f;

    private PIDController pidRotation = new PIDController(0.0f, 0.0055, 0.00000, 0.0);;

    @Override
    public void init() {
        Component[] componentList = {
                new Motor(-1, "backLeft", hardwareMap, false),
                new Motor(-1, "backRight", hardwareMap, true),
                new Motor(-1, "frontLeft", hardwareMap, false),
                new Motor(-1, "frontRight", hardwareMap, true)
        };

        robot = new Robot(componentList, hardwareMap);
        telemetry.addData("Test", "Robot");
        heading = robot.gyro.getHeading();
        targetHeading = 45.0f;
    }

    @Override
    public void loop() {
        heading = robot.gyro.getHeading();
        //rotate();
        rotatePID();
        telemetry.addData("Rotation", heading);
        telemetry.addData("RotationTarget", targetHeading);
    }

    public void rotate(){
        if (heading < targetHeading){
            robot.startRotation(true);
        } else if (heading > targetHeading){
            robot.startRotation(false);
        } else {
            robot.stop();
        }
    }

    public void rotatePID(){
        float correction = pidRotation.update(heading);
        if (heading < targetHeading){
            robot.rotatePID(true, correction);
        } else if (heading > targetHeading){
            robot.rotatePID(false, correction);
        } else {
            robot.stop();
        }
    }

}
