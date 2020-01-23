package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class LimitSensor extends Component {

    public DigitalChannel digitalTouch;

    public LimitSensor(int port, String name, HardwareMap map) {
        super(port, name);
        this.digitalTouch = map.get(DigitalChannel.class, "sensor_digital");
        digitalTouch.setMode(DigitalChannel.Mode.INPUT);
    }

    public boolean isPressed(){
        return digitalTouch.getState();
    }

}
