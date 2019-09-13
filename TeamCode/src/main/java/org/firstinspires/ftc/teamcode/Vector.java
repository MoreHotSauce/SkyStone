package org.firstinspires.ftc.teamcode;

public class Vector {
    private float theta;
    private float magnitude;

    public Vector(float t, float m){
        setTheta(t);
        setMagnitude(m);
    }

    public void setTheta(float theta) {
        this.theta = theta;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public float getX(){
        double radians = Math.toRadians(theta);
        return magnitude * (float)Math.sin(radians);
    }

    public float getY(){
        double radians = Math.toRadians(theta);
        return magnitude * (float)Math.cos(radians);
    }

    public void add(Vector v2){
        float Vrx = v2.getX() + this.getX();
        float Vry = v2.getY() + this.getY();

        this.setTheta((float)Math.atan(Vry/Vrx));
        this.setMagnitude((float)Math.sqrt(Math.pow(Vrx, 2) + Math.pow(Vry, 2)));
    }
}
