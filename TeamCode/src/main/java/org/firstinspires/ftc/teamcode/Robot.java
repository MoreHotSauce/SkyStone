package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {
    private Component[] components;
    public Mecanum drivetrain;
    private Gyro gyro;

    public Robot(Component[] comps, HardwareMap map){
        this.components = comps;
        drivetrain = new Mecanum(
                components[0],
                components[1],
                components[2],
                components[3]
        );
        this.gyro = new Gyro(map);
    }

    public void turbo(boolean turbo){
        drivetrain.setTurbo(turbo);
    }

    public void drive(float xMove, float yMove, float rotate) {
        drivetrain.move(xMove, yMove, rotate);
    }
}
