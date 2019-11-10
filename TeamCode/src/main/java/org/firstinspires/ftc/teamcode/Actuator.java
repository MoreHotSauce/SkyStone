package org.firstinspires.ftc.teamcode;

public class Actuator {
    private EMotor actuatorMotor;
    private final float FOURINCHENCODER = 100;
    private final float MAXPOSITION = 400;
    public Actuator(EMotor motor){
        this.actuatorMotor = motor;
    }

    public boolean increaseFour(){
        if (actuatorMotor.getEncoderValue() + FOURINCHENCODER <= MAXPOSITION){
            actuatorMotor.setTargetPos(actuatorMotor.getEncoderValue() + FOURINCHENCODER);
            return true;
        } else {
            return false;
        }
    }

    public boolean decrease(){
        if (actuatorMotor.getEncoderValue() - FOURINCHENCODER >= 0){
            actuatorMotor.setTargetPos(actuatorMotor.getEncoderValue() - FOURINCHENCODER);
            return true;
        } else {
            return false;
        }
    }


}
