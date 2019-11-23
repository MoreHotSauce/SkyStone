package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Lift {
    private Motor liftMotor;

    public Lift(Component liftMotor){
        this.liftMotor = (Motor) liftMotor;
        //this.liftMotor.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void up(float speed){
        liftMotor.setSpeed(speed);
    }

    public void down(float speed){
        liftMotor.setSpeed(speed * -1);
    }

    public void brake(){
        liftMotor.setSpeed(0.1f);
    }

}
