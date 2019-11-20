package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Robot {
    private Component[] components;
    public Mecanum drivetrain;
    public Lift lift;
    public Gyro gyro;
    public StepperServo foundationHook;
    public StepperServo chomper;
    public Actuator actuator;

    public float heading = 0.0f;
    public float targetHeading = 0.0f;

    public float currentY = 0.0f;
    public float targetY = 0.0f;

    public float currentX = 0.0f;
    public float targetX = 0.0f;

    private final float rKPR = 0.0007f;
    private final float rKIR = 0.00001f;
    private final float rKDR = 0.0f;

    private final float yKPR = 0.015f;
    private final float yKIR = 0.00003f;
    private final float yKDR = 0.005f;

    private final float xKPR = 0.015f;
    private final float xKIR = 0.00003f;
    private final float xKDR = 0.005f;

    private boolean previousChomperButton = false;
    private boolean chomperOpen = true;

    private boolean previousFoundationButton = false;
    private boolean foundationOpen = true;

    private PIDController pidYDistance = new PIDController(0f, yKPR, yKIR, yKDR, false);
    private PIDController pidXDistance = new PIDController(0f, xKPR, xKIR, xKDR, false);
    private PIDController pidRotation = new PIDController(0.0f, rKPR, rKIR, rKDR, true);

    public Robot(Component[] comps, HardwareMap map, boolean auton){
        this.components = comps;
        if (auton){
            drivetrain = new Mecanum(
                    components[0],
                    components[1],
                    components[2],
                    components[3],
                    true
            );
        } else {
            drivetrain = new Mecanum(
                    components[0],
                    components[1],
                    components[2],
                    components[3],
                    false
            );
        }


        lift = new Lift(
                components[7]
        );

        this.chomper = (StepperServo) components[5];
        chomper.servo.setPosition(0);

        this.gyro = new Gyro(map);

        this.foundationHook = (StepperServo) components[4];
        foundationHook.setAngle(153.0f);

        actuator = new Actuator((EMotor) components[6]);

        drivetrain.resetAllEncoders();

        heading = gyro.getHeading();
        targetHeading = heading;
        changeTargetRotation(targetHeading);

        currentY = drivetrain.getYDistance();
        changeTargetY(targetY);
    }

    public void updateLoop(){
        heading = gyro.getHeading();
        currentY = drivetrain.getYDistance();
        currentX = drivetrain.getXDistance();
    }

    public void resetMotorSpeeds(){
        drivetrain.resetMotorSpeeds();
    }

    public void stop() {
        drivetrain.stop();
    }

    public void turbo(boolean turbo){
        drivetrain.setTurbo(turbo);
    }

    public void drive(float xMove, float yMove, float rotate) {
        drivetrain.move(xMove, yMove, rotate);
    }


    public void moveLift(float speedDown, float speedUp){
        lift.down(speedDown);
        lift.up(speedUp);
    }



    public void chomperControl(boolean pressed){
        if(pressed && !previousChomperButton){
            if(chomperOpen){
                chomper.servo.setPosition(0);
                chomperOpen = false;
            } else {
                chomper.servo.setPosition(0.5);
                chomperOpen = true;
            }
        }

        previousChomperButton = pressed;
    }

    //TODO: Make this not terribly designed
    public void actuatorControl(boolean extend, boolean retract){
        if (extend && !retract){
            actuator.actuatorMotor.motor.setPower(0.3);
        } else if (!extend && retract) {
            actuator.actuatorMotor.motor.setPower(-0.3);
        } else {
            actuator.actuatorMotor.motor.setPower(0);
        }
    }

    public void foundationHookControl(boolean pressed){
        if(pressed && !previousFoundationButton){
            if(foundationOpen){
                foundationHook.setAngle(135);
                foundationOpen = false;
            } else {
                foundationHook.setAngle(100);
                foundationOpen = true;
            }
        }

        previousFoundationButton = pressed;
    }

    public void changeTargetY(float target){
        if(target == this.targetY){
            return;
        } else {
            targetY = target;
            drivetrain.resetAllEncoders();
            pidYDistance = new PIDController(target, yKPR, yKIR, yKDR, false);
        }
    }

    public void changeTargetX(float target){
        if(target == this.targetX){
            return;
        } else {
            targetX = target;
            drivetrain.resetAllEncoders();
            pidXDistance = new PIDController(target, xKPR, xKIR, xKDR, false);
        }
    }

    public void changeTargetRotation(float target){
        if(target == this.targetHeading){
            return;
        } else {
            targetHeading = target;
            pidRotation = new PIDController(target, rKPR, rKIR, rKDR, true);
        }
    }

    public float rotatePID(){
        float correctionR = pidRotation.update(heading);
        drivetrain.rotatePID(correctionR);
        return correctionR;
    }

    public float moveTargetY(){
        float correctionY = pidYDistance.update(currentY);
        drivetrain.moveYDistance(correctionY);
        return correctionY;
    }

    public float moveTargetX(){
        float correctionX = pidYDistance.update(currentX);
        drivetrain.moveXDistance(correctionX);
        return correctionX;
    }
}
