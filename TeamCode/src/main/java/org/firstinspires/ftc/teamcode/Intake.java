package org.firstinspires.ftc.teamcode;

public class Intake {
    private boolean open = true;
    //private Servo intakeServo;

    public Intake(Component servo){
        //this.intakeServo = (Servo) servo;
    }

    public void setServo(){

    }

    public void setToOpen(){
        this.open = true;
        setServo();
    }

    public void setToClose(){

    }

    public boolean getOpen() {
        return open;
    }

}
