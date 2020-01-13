package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Actuator {
    public EMotor actuatorMotor;
    private int position = 0;

    public Actuator(EMotor motor){
        this.actuatorMotor = motor;
        /*this.actuatorMotor.resetEncoder();
        this.actuatorMotor.motor.setTargetPosition(0);
        this.actuatorMotor.runToPosition(0);*/
    }

    /*public boolean increaseFour(){
        if (position == 0){
            actuatorMotor.runToPosition(-697);
            this.position++;
            return true;
        } else if (position == 1){
            actuatorMotor.runToPosition(-1980);
            this.position++;
            return true;
        }
        return false;
    }

    public boolean decreaseFour(){
        if (position == 1){
            actuatorMotor.setTargetPos(0);
            actuatorMotor.runToPosition(0);
            this.position--;
            return true;
        } else if (position == 2){
            actuatorMotor.setTargetPos(-697);
            actuatorMotor.runToPosition(-697);
            this.position--;
            return true;
        }
        return false;
    }*/

}
