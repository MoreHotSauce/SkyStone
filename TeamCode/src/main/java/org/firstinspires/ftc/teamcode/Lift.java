package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Lift {
    public Motor liftMotor;
    public Motor liftMotor2;

    public Lift(Component liftMotor, Component liftMotor2){
        this.liftMotor = (Motor) liftMotor;
        this.liftMotor2 = (Motor) liftMotor2;
        //this.liftMotor.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void up(float speed){
        liftMotor.setSpeed(speed);
        liftMotor2.setSpeed(speed);
    }

    public void down(float speed){
        liftMotor.setSpeed(speed * -1);
        liftMotor2.setSpeed(speed * -1);
    }

    public void brake(){
        liftMotor.setSpeed(0.05f);
        liftMotor2.setSpeed(0.05f);
    }

}
