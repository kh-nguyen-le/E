package securitySystem;

import com.esotericsoftware.kryonet.Client;
import securitySystem.Network.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;



public class CameraListener extends Listener {
    
        private Client client;
		private GpioController gpio;
        
        
        public void init(Client client, GpioController gpio) {
            this.client = client;
			this.gpio = gpio;
        }
        
        public void connected(Connection c) {
            
                System.out.println("Camera Connected");
                // Video stream from the  PI_CAMERA with the use of command line command
                try{
                    System.out.println("Begin stream");
                    java.lang.Runtime rt = Runtime.getRuntime();
                    java.lang.Process pr = rt.exec("raspivid -o - -w 920 -h 540 -t 9999999 |cvlc -vvv stream:///dev/stdin --sout '#rtp{sdp=rtsp://:8554/}' :demux=h264");
				
                }catch(IOException e){
                    e.printStackTrace();
                }
                AuthenticationPacket request = new AuthenticationPacket();
                System.out.println("Sends authentication");
                client.sendTCP(request);
            
        }
        
      //  public void disconnected(Connection c){ System.out.println("Client disconnected");}

        public void received(Connection c, Object o) {
            
                if (o instanceof HandshakePacket) {
                        if (((HandshakePacket) o).success) {
                                MessagePacket name = new MessagePacket();
                                name.message = "Camera";
                                client.sendTCP(name);
                        }
                }
                if (o instanceof MotorPacket) {
                        //turn motor
                }        
                
                if (o instanceof SettingsPacket) {
                        //modify camera settings
                }
        }

}
