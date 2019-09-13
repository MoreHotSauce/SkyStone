package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Motor;

public class Mecanum {

    private Motor[] motors = new Motor[4];
    private Vector velocity = new Vector();
    private float rotation = 0.0f;

    public Mecanum(Component backLeft, Component backRight, Component frontLeft, Component frontRight){
        motors[0] = (Motor) backLeft;
        motors[1] = (Motor) backRight;
        motors[2] = (Motor) frontLeft;
        motors[3] = (Motor) frontRight;
    }

    public void moveLinear(float xMove, float yMove) {
        
    }

    public void rotate(float rotate) {

    }
}
