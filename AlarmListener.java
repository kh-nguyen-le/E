package securitySystem;

import com.esotericsoftware.kryonet.Client;
import securitySystem.Network.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlarmListener extends Listener {
        private Client client;
        private AlarmThread at;
        
        public void init(Client client) {
            this.client = client;
            at = new AlarmThread();
            at.start();
        }
	
	public void connected(Connection c) {
                System.out.println("Alarm Connected");
		AuthenticationPacket request = new AuthenticationPacket();
                System.out.println("Sending authentication");
		client.sendTCP(request);
	}

        public void disconnected(Connection c){ System.out.println("Client disconnected");}        
        
	public void received(Connection c, Object o) {
            
          /* 	if (o instanceof AuthenticationPacket){
			HandshakePacket handshake = new HandshakePacket();
			handshake.success = true;
			c.sendTCP(handshake);
		}
            */
            
		if (o instanceof HandshakePacket) {
			if (((HandshakePacket) o).success) {
				MessagePacket name = new MessagePacket();
				name.message = "Alarm";
				client.sendTCP(name);
			}
		}
		if (o instanceof AlertPacket) {
                    AlertPacket ap = (AlertPacket)o;
                    if(ap.alarmOn){
                        ap.alarmOn = true;
                        AlarmThread.alarmOn = true;
                       
                    }else{
                        ap.alarmOn = false;
                        AlarmThread.alarmOn = false;
                    }
                    
			
		}

		if (o instanceof SettingsPacket) {
			//modify alarm settings
		}
	}
}

class AlarmThread extends Thread{
    public static boolean alarmOn;
    
    public AlarmThread(){
        alarmOn = false;
    }
    
    public synchronized void run(){
        System.out.println("Running alarm thread");
        java.lang.Runtime rt = java.lang.Runtime.getRuntime(); //get runtime
        while(true){
           try{ 
                sleep(200);
                if(alarmOn){
                    for (int i=0;i<5;i+=1){   
                        System.out.println("Ring!");
                        //java.lang.Process p = rt.exec("aplay alarm.wav"); //play alarm.wav sound
                        sleep(500);
                    }
                    //alarmOn=false;
                }
           }catch(InterruptedException ex){
               ex.printStackTrace();
           } 

        }   
     }
}
/*
System commands in Java.
java.lang.Runtime rt = java.lang.Runtime.getRuntime();
java.lang.Process p = rt.exec("command here");






*/






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
