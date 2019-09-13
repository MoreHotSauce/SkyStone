package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;


public class Motor extends Component{
    private boolean counterClockWise;
    private int speed;
    private DcMotor motor;

    public Motor(String name){
        super.port = -1;
        super.name = name;
        //motor = hardwareMap.dcMotor.get("arm");
    }
}
