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
                new Motor(-1, "backLeft", hardwareMap, false),              //0
                new Motor(-1, "backRight", hardwareMap, true),              //1
                new Motor(-1, "frontLeft", hardwareMap, false),             //2
                new Motor(-1, "frontRight", hardwareMap, true),             //3
                new StepperServo(-1, "foundationHook", hardwareMap),                //4
                new StepperServo(-1, "huggerRMain", hardwareMap),                   //5
                new EMotor(-1, "actuator", hardwareMap, 1),                  //6
                new Motor(-1, "liftMotor", hardwareMap, false),             //7
                new Motor(-1, "liftMotor2", hardwareMap, true),             //8
                new StepperServo(-1, "intakeClawLeft", hardwareMap),                //9
                new StepperServo(-1, "intakeClawRight", hardwareMap),               //10
                new StepperServo(-1, "odoServo", hardwareMap),                      //11
                new Motor(-1, "fakeMotor", hardwareMap, true),              //12
                new Color(-1, "colorSensor", hardwareMap),                          //13
                new StepperServo(-1, "huggerRArm", hardwareMap),                    //14
                new StepperServo(-1, "huggerLMain", hardwareMap),                   //15
                new StepperServo(-1, "huggerLArm", hardwareMap),                    //16
                new LimitSensor(-1, "limit", hardwareMap)                           //17
        };

        robot = new Robot(componentList, hardwareMap, true);
        telemetry.addData("Test", "Robot");
        robot.colorSensor.led(true);
    }

    @Override
    public void init_loop(){

    }

    @Override
    public void loop() {
        robot.updateLoop();
        robot.resetMotorSpeeds();

        robot.changeTarget(0f, 0f, 90f);

        telemetry.addData("stopped", robot.stopped(true));
        telemetry.addData("counterX", robot.counterBadX);
        telemetry.addData("counterY", robot.counterBadY);
        telemetry.addData("rlyBad", robot.rlyBad);
        telemetry.addData("PositionY", robot.currentY);
        telemetry.addData("PositionTargetY", robot.targetY);
        telemetry.addData("CorrectionY", robot.correctionY);
        telemetry.addData("highestY", robot.highestY);
        telemetry.addData("PositionX", robot.currentX);
        telemetry.addData("PositionTargetX", robot.targetX);
        telemetry.addData("CorrectionX", robot.correctionX);
        telemetry.addData("highestX", robot.highestX);
        telemetry.addData("xEncoder", robot.fakeMotor.getEncoderValue());
        //telemetry.addData("PositionTargetX", robot.targetX);
        telemetry.addData("Rotation", robot.currentR);
        telemetry.addData("RotationTarget", robot.targetR);
        telemetry.addData("CorrectionR", robot.correctionR);

       // telemetry.addData("backLeft", robot.drivetrain.backLeftSpeed);
        //telemetry.addData("backRight", robot.drivetrain.backRightSpeed);
        //telemetry.addData("frontLeft", robot.drivetrain.frontLeftSpeed);
        //telemetry.addData("frontRight", robot.drivetrain.frontRightSpeed);
        //telemetry.addData("totalMult", robot.colorSensor.getValue()[0] * robot.colorSensor.getValue()[1] * robot.colorSensor.getValue()[2]);
        //telemetry.addData("alpha", robot.colorSensor.getValue()[0]);
        //telemetry.addData("red", robot.colorSensor.getValue()[1]);
        //telemetry.addData("green", robot.colorSensor.getValue()[2]);
        //telemetry.addData("blue", robot.colorSensor.getValue()[3]);
        telemetry.addData("skystone?", robot.isSkystone());


    }

}
