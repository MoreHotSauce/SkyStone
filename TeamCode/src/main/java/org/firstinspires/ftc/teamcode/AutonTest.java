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

        robot = new Robot(componentList, hardwareMap, true);
        telemetry.addData("Test", "Robot");
        robot.colorSensor.led(true);
    }

    @Override
    public void loop() {
        //robot.updateLoop();
        robot.resetMotorSpeeds();
        float correctionR = robot.rotatePID();
        float correctionY = robot.moveTargetY();
        float correctionX = robot.moveTargetX();

        robot.hugger.setAngle(130f);

        telemetry.addData("", robot);
        telemetry.addData("PositionY", robot.currentY);
        telemetry.addData("PositionTargetY", robot.targetY);
        telemetry.addData("PositionX", robot.currentX);
        telemetry.addData("PositionTargetX", robot.targetX);
        telemetry.addData("Rotation", robot.heading);
        telemetry.addData("RotationTarget", robot.targetHeading);
        telemetry.addData("CorrectionR", correctionR);
        telemetry.addData("CorrectionY", correctionY);
        telemetry.addData("CorrectionX", correctionX);
        telemetry.addData("backLeft", robot.drivetrain.backLeftSpeed);
        telemetry.addData("backRight", robot.drivetrain.backRightSpeed);
        telemetry.addData("frontLeft", robot.drivetrain.frontLeftSpeed);
        telemetry.addData("frontRight", robot.drivetrain.frontRightSpeed);
        telemetry.addData("totalMult", robot.colorSensor.getValue()[1] * robot.colorSensor.getValue()[2]);
        telemetry.addData("alpha", robot.colorSensor.getValue()[0]);
        telemetry.addData("red", robot.colorSensor.getValue()[1]);
        telemetry.addData("green", robot.colorSensor.getValue()[2]);
        telemetry.addData("blue", robot.colorSensor.getValue()[3]);
        telemetry.addData("skystone?", robot.isSkystone());


    }

}
