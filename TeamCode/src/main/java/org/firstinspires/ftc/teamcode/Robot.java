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
    public Actuator actuator;
    public Color colorSensor;
    public StepperServo hugger;
    public Motor fakeMotor;
    public StepperServo intakeClawLeft;
    public StepperServo intakeClawRight;

    public float heading = 0.0f;
    public float targetHeading = 0.0f;

    public float currentY = 0.0f;
    public float targetY = 0.0f;
    public float highestY = 0.0f;

    public float currentX = 0.0f;
    public float targetX = 0.0f;
    public float highestX = 0.0f;

    public float correctionX = 0.0f;
    public float correctionY = 0.0f;
    public float correctionR = 0.0f;


    private boolean pidX = false;
    private boolean pidY = false;

    private final float rKPR = 0.006f;
    private final float rKIR = 0.00001f;
    private final float rKDR = 0.0001f;

    private final float yKPR = 0.1f;
    private final float yKIR = 0.00005f;
    private final float yKDR = 100f;

    private final float xKPR = 0.04f;
    private final float xKIR = 0.00002f;
    private final float xKDR = 0.000005f;

    private final long SKYSTONE_THRESHOLD = 500000;

    private boolean previousIntakeButton = false;
    private boolean intakeOpen = true;

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
                components[7],
                components[8]
        );

        this.gyro = new Gyro(map);

        this.foundationHook = (StepperServo) components[4];
        foundationHook.setAngle(133);

        actuator = new Actuator((EMotor) components[6]);

        this.colorSensor = (Color) components[13];

        this.hugger = (StepperServo) components[5];
        hugger.setAngle(0);

        drivetrain.resetAllEncoders();

        this.fakeMotor = (Motor) components[12];

        heading = gyro.getHeading();
        targetHeading = heading;
        //changeTargetRotation(targetHeading);

        this.intakeClawLeft = (StepperServo) components[9];
        intakeClawLeft.setAngle(80);
        this.intakeClawRight = (StepperServo) components[10];
        intakeClawRight.setAngle(100);

        lift.liftMotor2.resetEncoder();
        fakeMotor.resetEncoder();


        currentY = getOdoY();
        //changeTargetY(targetY);

        currentX = getOdoX();
        //changeTargetX(targetX);
    }

    public void updateLoop(){
        heading = gyro.getHeading();
        currentY = getOdoY();
        currentX = getOdoX();

        if(pidX) {
            moveTargetX();
        }else if(pidY) {
            moveTargetY();
        }
        rotatePID();
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

    public boolean isSkystone(){
        return (colorSensor.getValue()[0] * colorSensor.getValue()[1] * colorSensor.getValue()[2] < SKYSTONE_THRESHOLD);
    }

    public void moveLift(float speedDown, float speedUp){
        if(speedDown == 0 && speedUp == 0){
            lift.brake();
        }else {
            lift.down(speedDown);
            lift.up(speedUp);
        }
    }

    public void intakeControl(boolean pressed){
        if(pressed && !previousFoundationButton){
            if(intakeOpen){
                intakeClawLeft.setAngle(50);
                intakeClawRight.setAngle(100);
                intakeOpen = false;
            } else {
                intakeClawLeft.setAngle(100);
                intakeClawRight.setAngle(50);
                intakeOpen = true;
            }
        }

        previousIntakeButton = pressed;
    }

    public void chomperControl(boolean pressed){

    }

    public float getOdoX(){
        return (fakeMotor.getEncoderValue() / (8192f)) * 6.1842375f;
    }

    public float getOdoY(){
        return (lift.liftMotor2.getEncoderValue() / (8192f)) * 6.1842375f;
    }

    public void actuatorControl(boolean extend, boolean retract){
        if (extend && !retract){
            actuator.actuatorMotor.motor.setPower(0.6);
        } else if (!extend && retract) {
            actuator.actuatorMotor.motor.setPower(-0.6);
        } else {
            actuator.actuatorMotor.motor.setPower(0);
        }
    }

    public void foundationHookControl(boolean pressed){
        if(pressed && !previousFoundationButton){
            if(foundationOpen){
                foundationHook.setAngle(133);
                foundationOpen = false;
            } else {
                foundationHook.setAngle(85);
                foundationOpen = true;
            }
        }

        previousFoundationButton = pressed;
    }

    public void changeTargetY(float target){
        if(target == this.targetY){
            return;
        } else {
            pidY = true;
            pidX = false;
            targetY = target;
            drivetrain.resetAllEncoders();
            pidYDistance = new PIDController(target, yKPR, yKIR, yKDR, false);
        }
    }

    public void changeTargetX(float target){
        if(target == this.targetX){
            return;
        } else {
            pidY = false;
            pidX = true;
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
        correctionR = pidRotation.update(heading);
        drivetrain.rotatePID(correctionR);
        return correctionR;
    }

    public float moveTargetY(){
        correctionY = pidYDistance.update(currentY);
        drivetrain.moveYDistance(correctionY);
        if(this.currentY > this.highestY) {
            this.highestY = this.currentY;
        }
        return correctionY;
    }

    public float moveTargetX(){
        correctionX = pidXDistance.update(currentX);
        drivetrain.moveXDistance(correctionX);
        if(this.currentX > this.highestX) {
            this.highestX = this.currentX;
        }
        return correctionX;
    }


}
