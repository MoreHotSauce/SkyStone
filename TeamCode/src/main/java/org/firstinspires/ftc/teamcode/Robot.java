package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Robot {
    private Component[] components;
    public Mecanum drivetrain;
    //public Lift lift;
    public Gyro gyro;
    public StepperServo foundationHook;
    public StepperServo chomper;
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

        /*
        lift = new Lift(
                components[4]
        );
        */
        this.gyro = new Gyro(map);
        this.foundationHook = (StepperServo) components[4];
        foundationHook.setAngle(0.0f);
    }


    public void resetMotorSpeeds(){
        drivetrain.resetMotorSpeeds();
    }

    public void rotatePID(float error){
        drivetrain.rotatePID(error);
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

    /*
    public void moveLift(float speedDown, float speedUp){
        lift.down(speedDown);
        lift.up(speedUp);
    }
     */

    public void chomperControl(boolean open){
        if (open){
            chomper.setAngle(45);
        } else {
            chomper.setAngle(135);
        }
    }

    public void foundationHookControl(boolean up){

        if (up){
            foundationHook.setAngle(135);
        } else {
            foundationHook.setAngle(45);
        }
    }
}
