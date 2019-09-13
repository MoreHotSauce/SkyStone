package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Mecanum;

public class Robot {
    private Component[] components;
    private Mecanum drivetrain;

    public Robot(){
        components = new Component[4];
        drivetrain = new Mecanum(
                components[0],
                components[1],
                components[2],
                components[3]
        );
    }

    public void drive(float xMove, float yMove, float rotate) {
        drivetrain.moveLinear(xMove, yMove);
        drivetrain.rotate(rotate);
    }
}
