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

    private final float KPR = 0.01f;
    private final float KIR = 0.00001f;
    private final float KDR = 0.0001f;

    private PIDController pidRotation = new PIDController(180.0f, KPR, KIR, KDR);

    @Override
    public void init() {
        Component[] componentList = {
                new Motor(-1, "backLeft", hardwareMap, false),
                new Motor(-1, "backRight", hardwareMap, true),
                new Motor(-1, "frontLeft", hardwareMap, false),
                new Motor(-1, "frontRight", hardwareMap, true),
                new StepperServo(-1, "foundationHook", hardwareMap)
        };

        robot = new Robot(componentList, hardwareMap, true);
        telemetry.addData("Test", "Robot");
        heading = robot.gyro.getHeading();
        changeTargetRotation(heading);
        changeTargetRotation(90.0f);
    }

    @Override
    public void loop() {
        heading = robot.gyro.getHeading();
        //rotate();
        robot.resetMotorSpeeds();
        rotatePID();
        telemetry.addData("encoders", robot.drivetrain.getYDistance());
        /*
        telemetry.addData("Rotation", heading);
        telemetry.addData("RotationTarget", targetHeading);
        telemetry.addData("backLeft", robot.drivetrain.backLeftSpeed);
        telemetry.addData("backRight", robot.drivetrain.backRightSpeed);
        telemetry.addData("frontLeft", robot.drivetrain.frontLeftSpeed);
        telemetry.addData("frontRight", robot.drivetrain.frontRightSpeed);
         */

    }

    public void changeTargetRotation(float target){
        pidRotation = new PIDController(target, KPR, KIR, KDR);
    }

    public void rotatePID(){
        float correction = pidRotation.update(heading);
        telemetry.addData("Correction", correction);
        robot.rotatePID(correction);
    }

}
