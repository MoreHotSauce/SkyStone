package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by mohit on 9/20/2019.
 **/
@TeleOp(name="testBot", group="Juice")
 public class GamePadTestbot extends OpMode {

 DcMotor Left;
 DcMotor Right;
 DcMotor rightTrigger;

 Servo claw;

 double LeftPower;
 double RightPower;

 boolean servoButton;
 double servoPower;

 @Override
 public void init() {
  Left = hardwareMap.dcMotor.get("Left");
  Right = hardwareMap.dcMotor.get("Right");
  claw = hardwareMap.servo.get("claw");
  //rightTrigger = hardwareMap.dcMotor.get("rightTrigger");

  //rightTrigger.setDirection(DcMotor.Direction.FORWARD);

  Left.setDirection(DcMotor.Direction.REVERSE);

  servoButton = false;
  servoPower = 1;
 }

 @Override
 public void loop() {
  LeftPower = gamepad1.left_stick_y;
  RightPower = gamepad1.right_stick_y;

  Left.setPower(LeftPower);
  Right.setPower(RightPower);

 /*if(gamepad1.right_trigger == 1){
 rightTrigger.setDirection(DcMotor.Direction.REVERSE);
 rightTrigger.setPower(1);
 }*/


  if (gamepad1.x && servoButton == false) {

   claw.setPosition(1);
   servoButton = true;
  } else if (gamepad1.x && servoButton == true) {
   claw.setPosition(-1);
   servoButton = false;
  }
 }
}
