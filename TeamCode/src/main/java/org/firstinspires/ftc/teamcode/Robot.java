package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.ArrayList;

public class Robot {
    private Component[] components;
    public Mecanum drivetrain;
    public Lift lift;
    public Gyro gyro;
    public StepperServo foundationHook;
    public Actuator actuator;
    public Color colorSensor;
    public StepperServo huggerRMain;
    public StepperServo huggerRArm;
    public StepperServo huggerLMain;
    public StepperServo huggerLArm;
    public Motor fakeMotor;
    public StepperServo intakeClawLeft;
    public StepperServo intakeClawRight;
    public LimitSensor limit;

    public float currentR = 0.0f;
    public float targetR = 0.0f;

    public float currentY = 0.0f;
    public float targetY = 0.0f;
    public float highestY = 0.0f;

    public float currentX = 0.0f;
    public float targetX = 0.0f;
    public float highestX = 0.0f;

    public float correctionX = 0.0f;
    public float correctionY = 0.0f;
    public float correctionR = 0.0f;

    public boolean epicMode = false;

    public boolean pid = true;

    private final float rKPR = 0.012f;
    private final float rKIR = 0.000005f;
    private final float rKDR = 0.0000f;

    private final float yBASE = 0.5f;
    private final float xBASE = 0.06f;

    private final float yKPR = 0.06f;
    private final float yKIR = 0.000002f;
    private final float yKDR = 0.00f;

    private final float xKPR = 0.17f;
    private final float xKIR = 0.000007f;
    private final float xKDR = 0.00f;

    private final long SKYSTONE_THRESHOLD = 500000;

    private boolean previousIntakeButton = false;
    private boolean intakeOpen = true;

    private boolean previousFoundationButton = false;
    private boolean foundationOpen = true;

    private boolean mainArmPreviousR = false;
    private boolean mainArmOpenR = true;
    private boolean mainArmPreviousL = false;
    private boolean mainArmOpenL = true;

    private boolean hugArmPreviousR = false;
    private boolean hugArmOpenR = true;
    private boolean hugArmPreviousL = false;
    private boolean hugArmOpenL = true;

    private boolean right = true;

    private boolean autonRotating = false;

    public int counterBadX = 0;
    public int counterBadY = 0;
    public boolean rlyBad = false;

    private float lastX = 0;
    private float lastY = 0;



    private PIDController pidYDistance = new PIDController(0f, yKPR, yKIR, yKDR, false);
    private PIDController pidXDistance = new PIDController(0f, xKPR, xKIR, xKDR, false);
    private PIDController pidRotation = new PIDController(0.0f, rKPR, rKIR, rKDR, true);

    public ArrayList<Integer> skystones = new ArrayList<>();

    //public SkystoneDetectionHelper skystoneDetector;
    //public VuforiaSkystone skystoneDetector;

    public Robot(HardwareMap map, boolean auton){
        this.components = new Component[]{
                new Motor(-1, "backLeft", map, false),                  //0
                new Motor(-1, "backRight", map, true),                  //1
                new Motor(-1, "frontLeft", map, false),                 //2
                new Motor(-1, "frontRight", map, true),                 //3
                new StepperServo(-1, "foundationHook", map),            //4
                new StepperServo(-1, "huggerRMain", map),                   //5
                new EMotor(-1, "actuator", map, 1),                     //6
                new Motor(-1, "liftMotor", map, false),                 //7
                new Motor(-1, "liftMotor2", map, true),                 //8
                new StepperServo(-1, "intakeClawLeft", map),                //9
                new StepperServo(-1, "intakeClawRight", map),               //10
                new StepperServo(-1, "odoServo", map),                      //11
                new Motor(-1, "fakeMotor", map, true),              //12
                new Color(-1, "colorSensor", map),                          //13
                new StepperServo(-1, "huggerRArm", map),                    //14
                new StepperServo(-1, "huggerLMain", map),                   //15
                new StepperServo(-1, "huggerLArm", map),                    //16
                new LimitSensor(-1, "limit", map)                           //17
        };

        if (auton){
            drivetrain = new Mecanum(
                    components[0],
                    components[1],
                    components[2],
                    components[3],
                    true
            );
        } else {
            drivetrain = new Mecanum(
                    components[0],
                    components[1],
                    components[2],
                    components[3],
                    false
            );
        }


        lift = new Lift(
                components[7],
                components[8]
        );

        this.gyro = new Gyro(map);

        this.foundationHook = (StepperServo) components[4];
        foundationHook.setAngle(165);

        actuator = new Actuator((EMotor) components[6]);

        this.colorSensor = (Color) components[13];

        this.huggerRMain = (StepperServo) components[5];
        this.huggerRArm = (StepperServo) components[14];
        huggerRMain.setAngle(0);
        huggerRArm.setAngle(100);

        this.huggerLMain = (StepperServo) components[15];
        this.huggerLArm = (StepperServo) components[16];
        huggerLMain.setAngle(70);
        huggerLArm.setAngle(0);

        drivetrain.resetAllEncoders();

        this.fakeMotor = (Motor) components[12];

        this.limit = (LimitSensor) components[17];

        currentR = gyro.getHeading();
        targetR = currentR;
        //changeTargetRotation(targetHeading);

        this.intakeClawLeft = (StepperServo) components[9];
        intakeClawLeft.setAngle(0);
        this.intakeClawRight = (StepperServo) components[10];
        intakeClawRight.setAngle(180);

        lift.liftMotor2.resetEncoder();
        fakeMotor.resetEncoder();

        currentY = getOdoY();
        //changeTargetY(targetY);

        currentX = getOdoX();
        //changeTargetX(targetX);

        //skystoneDetector = new VuforiaSkystone(map);


        pidXDistance = new PIDController(0, xKPR, xKIR, xKDR, false);
        pidYDistance = new PIDController(0, yKPR, yKIR, yKDR, false);
        pidRotation = new PIDController(0, rKPR, rKIR, rKDR, true);

    }

    public Robot(Component[] comps, HardwareMap map, boolean auton){
        this.components = comps;
        if (auton){
            drivetrain = new Mecanum(
                    components[0],
                    components[1],
                    components[2],
                    components[3],
                    true
            );
        } else {
            drivetrain = new Mecanum(
                    components[0],
                    components[1],
                    components[2],
                    components[3],
                    false
            );
        }


        lift = new Lift(
                components[7],
                components[8]
        );

        this.gyro = new Gyro(map);

        this.foundationHook = (StepperServo) components[4];
        foundationHook.setAngle(165);

        actuator = new Actuator((EMotor) components[6]);

        this.colorSensor = (Color) components[13];

        this.huggerRMain = (StepperServo) components[5];
        this.huggerRArm = (StepperServo) components[14];
        huggerRMain.setAngle(0);
        huggerRArm.setAngle(100);

        this.huggerLMain = (StepperServo) components[15];
        this.huggerLArm = (StepperServo) components[16];
        huggerLMain.setAngle(70);
        huggerLArm.setAngle(0);

        drivetrain.resetAllEncoders();

        this.fakeMotor = (Motor) components[12];

        this.limit = (LimitSensor) components[17];

        currentR = gyro.getHeading();
        targetR = currentR;
        //changeTargetRotation(targetHeading);

        this.intakeClawLeft = (StepperServo) components[9];
        intakeClawLeft.setAngle(0);
        this.intakeClawRight = (StepperServo) components[10];
        intakeClawRight.setAngle(180);

        lift.liftMotor2.resetEncoder();
        fakeMotor.resetEncoder();

        currentY = getOdoY();
        //changeTargetY(targetY);

        currentX = getOdoX();
        //changeTargetX(targetX);

        //skystoneDetector = new VuforiaSkystone(map);


        pidXDistance = new PIDController(0, xKPR, xKIR, xKDR, false);
        pidYDistance = new PIDController(0, yKPR, yKIR, yKDR, false);
        pidRotation = new PIDController(0, rKPR, rKIR, rKDR, true);


    }

    public void updateLoop(){
        currentR = gyro.getHeading();
        lastY = currentY;
        currentY = getOdoY();
        lastX = currentX;
        currentX = getOdoX();

        /*skystones.add(skystoneDetector.currentStone);
        if(skystones.size() > 20){
            skystones.remove(0);
        }*/

        if(pid){
            autonMove();
        }
    }

    public int getAverageSkystone(){
        int total = 0;
        for(int value : skystones){
            total+=value;
        }
        return Math.round(total/(skystones.size()));
    }

    public void resetMotorSpeeds(){
        drivetrain.resetMotorSpeeds();
    }

    public void stop() {
        drivetrain.stop();
    }

    public void turbo(boolean turbo){
        drivetrain.setTurbo(turbo);
    }

    public void drive(float xMove, float yMove, float rotate) {
        drivetrain.move(xMove, yMove, rotate);
    }

    public boolean isSkystone(){
        return (colorSensor.getValue()[0] * colorSensor.getValue()[1] * colorSensor.getValue()[2] < SKYSTONE_THRESHOLD);
    }

    public void moveLift(float speedDown, float speedUp){
        if(speedDown == 0 && speedUp == 0){
            if(!limit.isPressed()) {
                lift.brake();
            }
        }else {
            if(!limit.isPressed()){
                lift.down(speedDown);
            }
            lift.up(speedUp);
        }
    }

    public void intakeControl(boolean pressed){
        if(pressed && !previousIntakeButton){
            if(intakeOpen){
                intakeClawLeft.setAngle(25);
                intakeClawRight.setAngle(155);
                intakeOpen = false;
            } else {
                intakeClawLeft.setAngle(60);
                intakeClawRight.setAngle(120);
                intakeOpen = true;
            }
        }

        previousIntakeButton = pressed;
    }

    public void chomperControl(boolean pressed){

    }

    public float getOdoX(){
        return (fakeMotor.getEncoderValue() / (8192f)) * 6.1842375f;
    }

    public float getOdoY(){
        return (lift.liftMotor2.getEncoderValue() / (8192f)) * 6.1842375f;
    }

    public void actuatorControl(boolean extend, boolean retract){
        if (extend && !retract){
            actuator.actuatorMotor.motor.setPower(0.8);
        } else if (!extend && retract) {
            actuator.actuatorMotor.motor.setPower(-0.8);
        } else {
            actuator.actuatorMotor.motor.setPower(0);
        }
    }

    public void foundationHookControl(boolean pressed){
        if(pressed && !previousFoundationButton){
            if(foundationOpen){
                foundationHook.setAngle(133);
                foundationOpen = false;
            } else {
                foundationHook.setAngle(100);
                foundationOpen = true;
            }
        }

        previousFoundationButton = pressed;
    }

    public void switchHuggers(boolean l, boolean r){
        if (l){
            this.right = false;
        } else if (r){
            this.right = true;
        }
    }

    public void huggerControl(boolean mainArm, boolean grab){
        if (right){
            if (mainArm && !mainArmPreviousR){
                if (mainArmOpenR){
                    huggerRMain.setAngle(70);
                    mainArmOpenR = false;
                } else {
                    huggerRMain.setAngle(10);
                    mainArmOpenR = true;
                }
            } else if (grab && !hugArmPreviousR){
                if (hugArmOpenR){
                    huggerRArm.setAngle(30);
                    hugArmOpenR = false;
                } else {
                    huggerRArm.setAngle(80);
                    hugArmOpenR = true;
                }
            }
            mainArmPreviousR = mainArm;
            hugArmPreviousR = grab;
        } else {
            if (mainArm && !mainArmPreviousL){
                if (mainArmOpenL){
                    huggerLMain.setAngle(60);
                    mainArmOpenL = false;
                } else {
                    huggerLMain.setAngle(0);
                    mainArmOpenL = true;
                }
            } else if (grab && !hugArmPreviousL){
                if (hugArmOpenL){
                    huggerLArm.setAngle(70);
                    hugArmOpenL = false;
                } else {
                    huggerLArm.setAngle(30);
                    hugArmOpenL = true;
                }
            }
            mainArmPreviousL = mainArm;
            hugArmPreviousL = grab;
        }

    }

    public void changeTarget(float x, float y, float r){
        //check if targetX has changed
        if(x != targetX){
            float xKP = xKPR;
            /*if(Math.abs(targetX) <= 10){
                xKP = xKPR_SMALL;
                counterBadX++;
            }

             */
            fakeMotor.resetEncoder();
            targetX = x;
            pidXDistance = new PIDController(targetX, xKP, xKIR, xKDR, false);

            //fakeMotor.resetEncoder(); //reset x odometry encoder
        }

        //check if targetY has changed
        if(y != targetY){
            lift.liftMotor2.resetEncoder();
            targetY = y;
            pidYDistance = new PIDController(targetY, yKPR, yKIR, yKDR, false);
            counterBadY++;
            if(targetY!=10.0){
                rlyBad = true;
            }
            //lift.liftMotor2.resetEncoder();//reset y odometry encoder
        }

        if(r != this.targetR){
            targetR = r;
            pidRotation = new PIDController(targetR, rKPR, rKIR, rKDR, true);
        }

    }

    public void autonMove(){
        correctionX = pidXDistance.update(currentX);
        correctionY = pidYDistance.update(currentY);
        correctionR = pidRotation.update(currentR);

        float xSpeed = -Range.clip(correctionX, -1, 1);
        float ySpeed = Range.clip(correctionY, -1, 1);
        float rSpeed = correctionR;

        if(autonRotating){
            xSpeed = 0;
            ySpeed = 0;
        }

        drivetrain.autonMove(xSpeed, ySpeed, rSpeed);
    }

    public static boolean tol(float current, float target, float tolerance){
        return Math.abs(current - target) <= tolerance;
    }

    public boolean stopped(boolean x){
        if(x){
            return Math.abs(lastX - currentX) <= 0.01;
        } else {
            return Math.abs(lastY - currentY) <= 0.01;
        }
    }

    @Deprecated
    public void setPid(boolean pid){
        this.pid = pid;
    }

    @Deprecated
    public void changeTargetY(float target){
    }

    @Deprecated
    public void changeTargetX(float target){
    }

    @Deprecated
    public void changeTargetRotation(float target){
    }

    @Deprecated
    public float rotatePID(){
        return 0;
    }

    @Deprecated
    public float moveTargetY(){
        return 0f;
    }

    @Deprecated
    public float moveTargetX(){
        return 0f;
    }

    @Deprecated
    public boolean isAutonRotating() {
        return autonRotating;
    }

    public void setAutonRotating(boolean autonRotating) {
        this.autonRotating = autonRotating;
    }
}
