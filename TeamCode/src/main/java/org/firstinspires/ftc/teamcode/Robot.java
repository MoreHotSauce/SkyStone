package org.firstinspires.ftc.teamcode;

public class Robot {
    private Component[] components;
    private Mecanum drivetrain;

    public Robot(Component[] comps){
        this.components = comps;
        drivetrain = new Mecanum(
                components[0],
                components[1],
                components[2],
                components[3]
        );
    }

    public void drive(float xMove, float yMove, float rotate) {
        drivetrain.move(xMove, yMove, rotate);
    }
}
