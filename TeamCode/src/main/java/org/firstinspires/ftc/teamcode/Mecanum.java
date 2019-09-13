package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Motor;

public class Mecanum {

    private Motor[] motors = new Motor[4];
    private Vector velocity = new Vector();
    private float rotation = 0.0f;

    Motor backLeft;
    Motor backRight;
    Motor frontLeft;
    Motor frontRight;

    public Mecanum(Component backLeft, Component backRight, Component frontLeft, Component frontRight){
        this.backLeft = (Motor) backLeft;
        this.backRight = (Motor) backRight;
        this.frontLeft = (Motor) frontLeft;
        this.frontRight = (Motor) frontRight;
    }

    public void move(float xMove, float yMove, float rotate) {
        backLeft.setSpeed(velocity.getY() + rotate);
        backRight.setSpeed(velocity.getX() - rotate);
        frontLeft.setSpeed(velocity.getX() + rotate);
        frontRight.setSpeed(velocity.getY() - rotate);

    }

}
