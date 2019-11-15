package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class EMotor extends Component {
    public DcMotor motor;
    private float speed = 0;
    private float targetPos = 0;

    public EMotor(int port, String name, HardwareMap map, float speed){
        super(port, name);
        motor = map.dcMotor.get(name);
        speed = this.speed;
    }

    public float getEncoderValue(){
        return motor.getCurrentPosition();
    }

    public void resetEncoder(){
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setTargetPos(float target){ this.targetPos = target; }

    public void runToPosition(float position){
        this.targetPos = position;
        motor.setTargetPosition((int) targetPos);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(speed);
    }
}
