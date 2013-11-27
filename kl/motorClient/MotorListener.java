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

/**
 * Getting sound out the 3.5mm analog audio jack on the RPI thanks to 
 * http://www.raspberrypi-spy.co.uk/2013/06/raspberry-pi-command-line-audio/
 * 
The first thing to do is run :

lsmod | grep snd_bcm2835
and check snd_bcm2835 is listed. If it isn’t then run the following command :

sudo modprobe snd_bcm2835  

If the module isn’t loaded automatically when you boot then you can force it to load by using the following process :

cd /etc
sudo nano modules

Then add ‘snd-bcm2835′ so it looks like this :

# /etc/modules: kernel modules to load at boot time.
#
# This file contains the names of kernel modules that should be
# loaded at boot time, one per line. Lines beginning with "#" are
# ignored. Parameters can be specified after the module name.
 
snd-bcm2835


By default the output is set to automatically select the default audio interface 
(HDMI if available otherwise analog). You can force it to use a specific interface using :

amixer cset numid=3 n
Where <n> is the required interface : 0=auto, 1=analog, 2=hdmi. To force the Raspberry Pi to use the analog output :

amixer cset numid=3 1

Do this if I boot the Pi with an HDMI cable plugged in. Otherwise it defaults to the 3.5mm jack automatically.


Playing A WAV File Using aplay

To play wav files :

aplay filename.wav

**/

