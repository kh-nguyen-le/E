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
	private GpioController gpio; //used to control motor: to be implement
        private StreamingThread st;
        
        public void init(Client client, GpioController gpio) {
            this.client = client;
	    this.gpio = gpio;
        }
        
        public void connected(Connection c) {
                st = new StreamingThread();
                st.start();
                System.out.println("Camera Connected");
                AuthenticationPacket request = new AuthenticationPacket();
                System.out.println("Sends authentication");
                client.sendTCP(request);            
        }
        
        public void disconnected(Connection c){ System.out.println("Client disconnected"); st.interrupt(); System.exit(0);}

        public void received(Connection c, Object o) {
            
                if (o instanceof HandshakePacket) {
                        if (((HandshakePacket) o).success) {
                                MessagePacket name = new MessagePacket();
                                name.message = "Camera";
                                client.sendTCP(name);                                
                        }
                        AlertPacket ap = new AlertPacket();
                        ap.alarmOn = true;
                        System.out.println("Sending alert to server");
                        client.sendTCP(ap);
                        
                }
                
                if (o instanceof MotorPacket) {
                        //turn motor to be implemented
                }        
        }

}

class StreamingThread extends Thread
{
    public StreamingThread(){}
    
    public void run(){
                //try {
                        //Process p = Runtime.getRuntime().exec("python stream.py");
                        //Process p = Runtime.getRuntime().exec(new String[]{"sh", "-c","raspivid -o - -w 920 -h 540 -t 0 |cvlc -vvv stream:///dev/stdin --sout '#rtp{sdp=rtsp://:8554/}' :demux=h264"});//"python stream.py");
                        //} catch (IOException e) {
                                //e.printStackTrace();
                        //}
    }
}