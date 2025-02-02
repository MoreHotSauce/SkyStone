package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

public class Mecanum {

    private float rotation = 0.0f;

    public Motor backLeft;
    public Motor backRight;
    public Motor frontLeft;
    public Motor frontRight;

    public float backLeftSpeed = 0, frontRightSpeed = 0, backRightSpeed = 0, frontLeftSpeed = 0;

    private final float MOTORSPEEDCONST = (float) Math.sqrt(2);
    private final float SLOWMODECONST = 0.5f;
    private final float AUTONCONST = 0.0f;

    private float slow = 1f;

    private boolean turbo;

    public void setTurbo(boolean turbo) {
        this.turbo = turbo;
    }

    public void setSlow(boolean slowBoolean){
        if(slowBoolean){
            slow = SLOWMODECONST;
        } else {
            slow = 1f;
        }
    }

    public Mecanum(Component backLeft, Component backRight, Component frontLeft, Component frontRight, boolean auton){
        this.backLeft = (Motor) backLeft;
        this.backRight = (Motor) backRight;
        this.frontLeft = (Motor) frontLeft;
        this.frontRight = (Motor) frontRight;
        if (auton) {
            backLeftSpeed = AUTONCONST;
            frontLeftSpeed = AUTONCONST;
            backRightSpeed = -AUTONCONST;
            frontRightSpeed = -AUTONCONST;
        }
    }

    public void move(float xMove, float yMove, float rotate) {
        this.rotation = rotate;


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
            backLeftSpeed = yMove + xMove + rotate;
            frontRightSpeed = yMove + xMove - rotate;
            backRightSpeed = yMove - xMove - rotate;
            frontLeftSpeed = yMove - xMove + rotate;

            backLeftSpeed = (backLeftSpeed / MOTORSPEEDCONST) * slow;
            frontRightSpeed = (frontRightSpeed / MOTORSPEEDCONST) * slow;
            backRightSpeed = (backRightSpeed / MOTORSPEEDCONST) * slow;
            frontLeftSpeed = (frontLeftSpeed / MOTORSPEEDCONST) * slow;
        }

        backLeft.setSpeed(Range.clip(backLeftSpeed, -1, 1));
        frontRight.setSpeed(Range.clip(frontRightSpeed, -1, 1));
        backRight.setSpeed(Range.clip(backRightSpeed, -1, 1));
        frontLeft.setSpeed(Range.clip(frontLeftSpeed, -1, 1));
    }

    public void resetMotorSpeeds(){
        backLeftSpeed = AUTONCONST;
        frontLeftSpeed = AUTONCONST;
        backRightSpeed = -AUTONCONST;
        frontRightSpeed = -AUTONCONST;
    }

    public float getYDistance(){
        float[] encoderValues = {
                backLeft.getEncoderValue() * -1,
                frontLeft.getEncoderValue() * -1,
                backRight.getEncoderValue() * -1,
                frontRight.getEncoderValue() * -1
        };

        float sumEncoderValues = 0.0f;

        for(int i = 0; i < 4; i++){
            encoderValues[i] /= 537.6;
            sumEncoderValues += encoderValues[i];
        }


        return (sumEncoderValues / 4) * 12.2004f;
    }

    public void moveYDistance(float correctionY){
        backLeftSpeed -= correctionY;
        frontLeftSpeed -= correctionY;
        backRightSpeed -= correctionY;
        frontRightSpeed -= correctionY;

        backLeft.setSpeed(Range.clip(backLeftSpeed, -1, 1));
        frontRight.setSpeed(Range.clip(frontRightSpeed, -1, 1));
        backRight.setSpeed(Range.clip(backRightSpeed, -1, 1));
        frontLeft.setSpeed(Range.clip(frontLeftSpeed, -1, 1));
    }

    public float getXDistance(){
        float[] encoderValues = {
                backLeft.getEncoderValue() * -1,
                frontLeft.getEncoderValue(),
                backRight.getEncoderValue(),
                frontRight.getEncoderValue() * -1
        };

        float sumEncoderValues = 0.0f;

        for(int i = 0; i < 4; i++){
            encoderValues[i] /= 537.6;
            sumEncoderValues += encoderValues[i];
        }


        return (sumEncoderValues / 4) * 11.3402f;
    }

    public void moveXDistance(float correctionX){
        backLeftSpeed -= correctionX;
        frontLeftSpeed += correctionX;
        backRightSpeed += correctionX;
        frontRightSpeed -= correctionX;

        backLeft.setSpeed(Range.clip(backLeftSpeed, -1, 1));
        frontRight.setSpeed(Range.clip(frontRightSpeed, -1, 1));
        backRight.setSpeed(Range.clip(backRightSpeed, -1, 1));
        frontLeft.setSpeed(Range.clip(frontLeftSpeed, -1, 1));
    }

    public void resetAllEncoders(){
        backLeft.resetEncoder();
        backRight.resetEncoder();
        frontLeft.resetEncoder();
        frontRight.resetEncoder();
    }

    public void rotatePID(float correction){

        backLeftSpeed += correction;
        frontLeftSpeed += correction;
        backRightSpeed += -correction;
        frontRightSpeed += -correction;

        backLeft.setSpeed(Range.clip(backLeftSpeed, -1, 1));
        frontRight.setSpeed(Range.clip(frontRightSpeed, -1, 1));
        backRight.setSpeed(Range.clip(backRightSpeed, -1, 1));
        frontLeft.setSpeed(Range.clip(frontLeftSpeed, -1, 1));
    }

    public void stop(){
        backLeft.setSpeed(Range.clip(0, -1, 1));
        frontRight.setSpeed(Range.clip(0, -1, 1));
        backRight.setSpeed(Range.clip(0, -1, 1));
        frontLeft.setSpeed(Range.clip(0, -1, 1));
    }

}
