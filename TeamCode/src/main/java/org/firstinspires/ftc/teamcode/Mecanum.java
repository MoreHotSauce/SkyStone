package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

public class Mecanum {

    private float rotation = 0.0f;

    private Motor backLeft;
    private Motor backRight;
    private Motor frontLeft;
    private Motor frontRight;

    private final float MOTORSPEEDCONST = (float) Math.sqrt(2);
    private float slowConstant = 1f;

    private boolean turbo;

    public void setTurbo(boolean turbo) {
        this.turbo = turbo;
    }

    public void setSlow(boolean slow){
        if(slow){
            slowConstant = 0.1f;
        } else {
            slowConstant = 1f;
        }
    }

    public Mecanum(Component backLeft, Component backRight, Component frontLeft, Component frontRight){
        this.backLeft = (Motor) backLeft;
        this.backRight = (Motor) backRight;
        this.frontLeft = (Motor) frontLeft;
        this.frontRight = (Motor) frontRight;
    }

    public void move(float xMove, float yMove, float rotate) {
        this.rotation = rotate;
        float backLeftSpeed = 0, frontRightSpeed = 0, backRightSpeed = 0, frontLeftSpeed = 0;

        if (turbo) {
            float x = Math.abs(xMove);
            float y = Math.abs(yMove);
            int snap = -1; //[0,3] snap*90 = angle

            if (x > y){
                if (xMove >= 0){
                    snap = 0;
                } else {
                    snap = 2;
                }
            } else {
                if (yMove >= 0){
                    snap = 1;
                } else {
                    snap = 3;
                }
            }

            switch(snap){
                 case(0):
                     backLeftSpeed = 1;
                     backRightSpeed = -1;
                     frontLeftSpeed = -1;
                     frontRightSpeed = 1;
                     break;

                 case(1):
                     backLeftSpeed = 1;
                     backRightSpeed = 1;
                     frontLeftSpeed = 1;
                     frontRightSpeed = 1;
                     break;

                 case(2):
                     backLeftSpeed = -1;
                     backRightSpeed = 1;
                     frontLeftSpeed = 1;
                     frontRightSpeed = -1;
                     break;

                 case(3):
                    backLeftSpeed = -1;
                    backRightSpeed = -1;
                    frontLeftSpeed = -1;
                    frontRightSpeed = -1;
                    break;

                 default:
                     backLeftSpeed = 0;
                     backRightSpeed = 0;
                     frontLeftSpeed = 0;
                     frontRightSpeed = 0;
             }

        } else {
            backLeftSpeed = yMove + xMove - rotate;
            frontRightSpeed = yMove + xMove + rotate;
            backRightSpeed = yMove - xMove + rotate;
            frontLeftSpeed = yMove - xMove - rotate;

            backLeftSpeed = (backLeftSpeed / MOTORSPEEDCONST) * slowConstant;
            frontRightSpeed = (frontRightSpeed / MOTORSPEEDCONST) * slowConstant;
            backRightSpeed = (backRightSpeed / MOTORSPEEDCONST) * slowConstant;
            frontLeftSpeed = (frontLeftSpeed / MOTORSPEEDCONST) * slowConstant;
        }

        backLeft.setSpeed(Range.clip(backLeftSpeed, -1, 1));
        frontRight.setSpeed(Range.clip(frontRightSpeed, -1, 1));
        backRight.setSpeed(Range.clip(backRightSpeed, -1, 1));
        frontLeft.setSpeed(Range.clip(frontLeftSpeed, -1, 1));
    }
}
