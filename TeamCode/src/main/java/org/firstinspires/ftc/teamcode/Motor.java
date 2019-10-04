package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Motor extends Component{
    private boolean reverse;
    private float speed;
    private DcMotor motor;

    public Motor(int port, String name, HardwareMap map, boolean reverse){
        super(port, name);
        this.reverse = reverse;
        this.speed = 0;
        try{
            motor = map.dcMotor.get(name);
        } catch (NullPointerException e) {
            throw new ArrayIndexOutOfBoundsException("test");
        }

        if(reverse){
            motor.setDirection(DcMotor.Direction.REVERSE);
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        motor.setPower(speed);
    }

}
