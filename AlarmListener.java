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
        while(true){
           try{ 
                sleep(200);
                if(alarmOn){
                    for (int i=0;i<5;i+=1){   
                        System.out.println("Ring!");
                        sleep(500);
                    }
                    alarmOn=false;
                }
           }catch(InterruptedException ex){
               ex.printStackTrace();
           } 

        }   
     }
}