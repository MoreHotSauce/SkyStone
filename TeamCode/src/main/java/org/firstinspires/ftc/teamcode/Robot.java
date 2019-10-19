package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {
    private Component[] components;
    public Mecanum drivetrain;
    //public Lift lift;
    public Gyro gyro;

    public Robot(Component[] comps, HardwareMap map){
        this.components = comps;
        drivetrain = new Mecanum(
                components[0],
                components[1],
                components[2],
                components[3]
        );
        /*
        lift = new Lift(
                components[4]
        );
        */
        this.gyro = new Gyro(map);
    }

    public void rotatePID(boolean ccw, float error){
        drivetrain.rotatePID(ccw, error);
    }

    public void startRotation(boolean ccw){
        drivetrain.startRotation(ccw);
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
}
