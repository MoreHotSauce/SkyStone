package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

public class Mecanum {

    private Vector velocity = new Vector();
    private float rotation = 0.0f;

    private Motor backLeft;
    private Motor backRight;
    private Motor frontLeft;
    private Motor frontRight;

    private final float MOTORSPEEDCONST = (float) Math.sqrt(2);

    public Mecanum(Component backLeft, Component backRight, Component frontLeft, Component frontRight){
        this.backLeft = (Motor) backLeft;
        this.backRight = (Motor) backRight;
        this.frontLeft = (Motor) frontLeft;
        this.frontRight = (Motor) frontRight;
    }

    public void move(float xMove, float yMove, float rotate) {
        this.rotation = rotate;

        this.velocity.setVector(xMove, yMove);

        float backLeftSpeed = velocity.getY() + velocity.getX() - rotate;
        float frontRightSpeed = velocity.getY() + velocity.getX() + rotate;
        float backRightSpeed = velocity.getY() - velocity.getX() + rotate;
        float frontLeftSpeed = velocity.getY() - velocity.getX() - rotate;

        backLeft.setSpeed(Range.clip(backLeftSpeed / MOTORSPEEDCONST, -1, 1));
        frontRight.setSpeed(Range.clip(frontRightSpeed / MOTORSPEEDCONST, -1, 1));
        backRight.setSpeed(Range.clip(backRightSpeed / MOTORSPEEDCONST, -1, 1));
        frontLeft.setSpeed(Range.clip(frontLeftSpeed / MOTORSPEEDCONST, -1, 1));
    }
}
