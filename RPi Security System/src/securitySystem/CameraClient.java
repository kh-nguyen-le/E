package securitySystem;

import securitySystem.Network;
import securitySystem.Network.*;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;


import java.io.IOException;


import com.esotericsoftware.kryonet.Client;

public class CameraClient {
        private Client client;
        private static java.net.InetAddress gateway;
        private final GpioController gpio;
        private final GpioPinDigitalInput pir;
        
        public CameraClient() {                
                client = new Client();
                gpio = GpioFactory.getInstance();
                Network.register(client);
                CameraListener listener = new CameraListener();
                listener.init(client, gpio);
                client.addListener(listener);
				
                // provision gpio pin #07 as an input pin with its internal pull down resistor enabled
                pir = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN);

                // create and register gpio pin listener
                pir.addListener(new GpioPinListenerDigital() 
                {
                        @Override
                        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) 
                        {
                        // if pin has changed to high then motion has been detected
                                if (event.getState().isHigh())
                                {
                                        AlertPacket alert = new AlertPacket();
                                        alert.alarmOn = true;
                                        client.sendTCP(alert);
                                        System.out.println("Motion Detected");
                                        //send a motion detected packet or some shit to server here
                                }
                                System.out.println(" Pin #: " + event.getPin() + " Pin State: " + event.getState());
                        }
                });
                
                do {
                        gateway =client.discoverHost(Network.port, 5000);
                }while (gateway==null);
                client.start();
                
                try {
                        client.connect(60000, gateway, Network.port, Network.port);
                        client.setTimeout(0);
                        client.setKeepAliveTCP(0);
                        client.setKeepAliveUDP(0);
                        

                } catch (IOException e) {
                        e.printStackTrace();
                        client.stop();
                }
                while(!client.isConnected()){
                    try {
                        client.reconnect(60000);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
        }
        
        public static void main(String[] args) throws InterruptedException {
            new CameraClient();
            while(true){
            	Thread.sleep(1000);
            }
      }
}
