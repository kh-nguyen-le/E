package securitySystem;

import com.esotericsoftware.kryonet.Client;
import securitySystem.Network.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;

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

        public void disconnected(Connection c){ System.out.println("Client disconnected"); System.exit(0);}
        
	public void received(Connection c, Object o) {           
            
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
                sleep(200);//gives thread time to check change in alarmOn
                if(alarmOn){  
                    try {
                        java.lang.Process p = rt.exec("aplay alarm.wav"); //play alarm.wav sound
                        System.out.println("AlertPacket received");
                        sleep(1000);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
           }catch(InterruptedException ex){
               ex.printStackTrace();
           } 

        }   
     }
}
