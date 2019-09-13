package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Mecanum;

public class Robot {

    Mecanum drivetrain = new Mecanum();
    Component[] components = new Component[4];

    public void drive(float xMove, float yMove, float rotate) {
        drivetrain.moveLinear(xMove, yMove);
        drivetrain.rotate(rotate);
    }
}
