package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Color extends Component {

    public ColorSensor sensor;

    public Color(int port, String name, HardwareMap map) {
        super(port, name);
        sensor = map.colorSensor.get(name);
    }

    public long[] getValue(){
        return new long[]{sensor.alpha(), sensor.red(), sensor.green(), sensor.blue()};
    }

    public void led(boolean enabled){
        sensor.enableLed(enabled);
    }

}
