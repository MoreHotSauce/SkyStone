package org.firstinspires.ftc.teamcode;

public class Lift {
    private Motor liftMotor;

    public Lift(Component liftMotor){
        this.liftMotor = (Motor) liftMotor;
    }

    public void up(float speed){
        liftMotor.setSpeed(speed);
    }

    public void down(float speed){
        liftMotor.setSpeed(speed * -1);
    }

}
