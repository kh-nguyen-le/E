//This program will monitor GPIO pin # 25 on gertboard for its input
//GPIO pin #25 (in wiringpi notation, gpio pin #06) is connect with a jumper cable to B1
//BUF1 is connected to the output of the PIR module
//A strap should be placed on the B1 input pins so that BUF1 is treated as an input
//The power source of the pir module is the 5 V pin on the J24 header, and the GND of the pir Module goes to the GND allocated to its buffer pin (the pin adjacent to BUF1)
//compile with javac -classpath .:classes:/opt/pi4j/lib/'*' pirtest.java
//run with sudo java -classpath .:classes:/opt/pi4j/lib/'*' pirtest
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class pirtest {
   
   public pirtest(){     
	
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #06 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, PinPullResistance.PULL_DOWN);

        // create and register gpio pin listener
        myButton.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // if pin has changed to high then motion has been detected
				if (event.getState().equals("High"){
					 System.out.println("Motion Detected")
				}
                //System.out.println(" Pin #: " + event.getPin() + " Pin State: " + event.getState());
            }
            
        });

	}
	
    public static void main(String args[]) throws InterruptedException {
        System.out.println("Program will continue until user exits");
        
        // keep program running until user aborts (CTRL-C)
        for (;;) {
            Thread.sleep(500);
        }
    }
}
