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

    private final float rKPR = 0.007f;
    private final float rKIR = 0.0f;
    private final float rKDR = 0.0f;

    private final float yKPR = 0.2f;
    private final float yKIR = 0.001f;
    private final float yKDR = 0.01f;



    private PIDController pidYDistance = new PIDController(0f, yKPR, yKIR, yKDR);
    private PIDController pidRotation = new PIDController(180.0f, rKPR, rKIR, rKDR);

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
        foundationHook.setAngle(0.0f);

        actuator = new Actuator((EMotor) components[6]);

        heading = gyro.getHeading();
        targetHeading = heading;
        changeTargetRotation(targetHeading);

        currentY = drivetrain.getYDistance();
        changeTargetY(targetY);
    }

    public void updateLoop(){
        heading = gyro.getHeading();
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



    public void chomperControl(boolean open){
        if(open){
            chomper.servo.setPosition(0.5);
        }else{
            chomper.servo.setPosition(0);

        }
    }

    //TODO: Make this not terribly designed
    public void actuatorControl(boolean extend, boolean retract){
        if (extend && !retract){
            actuator.actuatorMotor.motor.setPower(1.0);
        } else if (!extend && retract) {
            actuator.actuatorMotor.motor.setPower(-1.0);
        }
    }

    public void foundationHookControl(boolean up){

        if (up){
            foundationHook.setAngle(135);
        } else {
            foundationHook.setAngle(45);
        }
    }

    public void changeTargetY(float target){
        targetY = target;
        pidYDistance = new PIDController(target, yKPR, yKIR, yKDR);
    }

    public void changeTargetRotation(float target){
        pidRotation = new PIDController(target, rKPR, rKIR, rKDR);
    }

    public float rotatePID(){
        float correctionR = pidRotation.update(heading);
        drivetrain.rotatePID(correctionR);
        return correctionR;
    }

    public float moveTargetY(){
        float correctionY = pidYDistance.update(drivetrain.getYDistance());
        drivetrain.moveYDistance(correctionY);
        return correctionY;
    }
}
