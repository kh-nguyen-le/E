package securitySystem;

import com.esotericsoftware.kryonet.Client;
import securitySystem.Network.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class AlarmListener extends Listener {
        private Client client;
        public void init(Client client) {
            this.client = client;
        }
	
	public void connected(Connection c) {
                System.out.println("Alarm Connected");
		AuthenticationPacket request = new AuthenticationPacket();
                System.out.println("Sending authentication");
		client.sendTCP(request);
	}

       // public void disconnected(Connection c){ System.out.println("Client disconnected");}        
        
	public void received(Connection c, Object o) {
            
           	if (o instanceof AuthenticationPacket){
			HandshakePacket handshake = new HandshakePacket();
			handshake.success = true;
			c.sendTCP(handshake);
		}
            
            
		if (o instanceof HandshakePacket) {
			if (((HandshakePacket) o).success) {
				MessagePacket name = new MessagePacket();
				name.message = "Alarm";
				client.sendTCP(name);
			}
		}
		if (o instanceof AlertPacket) {
			//sound alarm
		}
		if (o instanceof AudioStreamPacket) {
			//activate microphone
		}
		if (o instanceof SettingsPacket) {
			//modify alarm settings
		}
	}
}

