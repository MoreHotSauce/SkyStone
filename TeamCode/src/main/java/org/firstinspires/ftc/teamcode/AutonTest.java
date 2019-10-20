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

    private final float KPR = 0.015f;
    private final float KIR = 0.00001f;
    private final float KDR = 0.00001f;

    private PIDController pidRotation = new PIDController(180.0f, KPR, KIR, KDR);

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
        changeTargetRotation(heading);
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

    public void changeTargetRotation(float target){
        pidRotation = new PIDController(target, KPR, KIR, KDR);
    }

    public void rotatePID(){
        float correction = pidRotation.update(heading);
        telemetry.addData("Correction", correction);
        robot.rotatePID(true, correction);
    }

}
