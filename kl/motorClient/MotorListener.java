package motorClient;

import securityServer.Packet.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.SoftPwm;

public class MotorListener extends Listener {
	private final GpioController gpio = GpioFactory.getInstance();
	private final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_17); 

	public MotorListener(){
		// initialize wiringPi library
        com.pi4j.wiringpi.Gpio.wiringPiSetup();
        // create soft-pwm pins (min=0 ; max=100)
        SoftPwm.softPwmCreate(18, 0, 100); 
        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
	}
	public void turn(boolean direction) {
		//set direction
		if(direction) pin.setState(PinState.HIGH); else pin.setState(PinState.LOW);
		
		//turn motor for 1 second 
		SoftPwm.softPwmWrite(18,20);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SoftPwm.softPwmWrite(18,0);
	}
	
	public void connected(Connection c) {
		Packet0Message name = new Packet0Message();
		name.message = "Motor";
		c.sendTCP(name);
	}

	public void received(Connection c, Object o) {
		if (o instanceof Packet2Motor) {
			boolean direction = ((Packet2Motor) o).direction;
			turn(direction);
		}
	}

}



