package org.firstinspires.ftc.teamcode;

public class Mecanum {

    private Vector velocity = new Vector();
    private float rotation = 0.0f;

    private Motor backLeft;
    private Motor backRight;
    private Motor frontLeft;
    private Motor frontRight;

    public Mecanum(Component backLeft, Component backRight, Component frontLeft, Component frontRight){
        this.backLeft = (Motor) backLeft;
        this.backRight = (Motor) backRight;
        this.frontLeft = (Motor) frontLeft;
        this.frontRight = (Motor) frontRight;
    }

    public void move(float xMove, float yMove, float rotate) {
        this.rotation = rotate;

        this.velocity.setVector(xMove, yMove);

        backLeft.setSpeed(velocity.getY() + rotate);
        backRight.setSpeed(velocity.getX() - rotate);
        frontLeft.setSpeed(velocity.getX() + rotate);
        frontRight.setSpeed(velocity.getY() - rotate);
    }
}
