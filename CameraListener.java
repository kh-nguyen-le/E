package securitySystem;

import com.esotericsoftware.kryonet.Client;
import securitySystem.Network.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class CameraListener extends Listener {
        private Client client;
        public void init(Client client) {
            this.client = client;
        }
        
	public void connected(Connection c) {
                System.out.println("Camera Connected");
		AuthenticationPacket request = new AuthenticationPacket();
		System.out.println("Sends authentication");
                client.sendTCP(request);
	}
        
      //  public void disconnected(Connection c){ System.out.println("Client disconnected");}

	public void received(Connection c, Object o) {
              	//if (o instanceof AuthenticationPacket){
		//	HandshakePacket handshake = new HandshakePacket();
		//	handshake.success = true;
		//	c.sendTCP(handshake);
		//}
		
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
		if (o instanceof SnapshotPacket) {
			//snapshot request from server; camera sends image with alert off
		}
		if (o instanceof VideoStreamPacket) {
			//stream request from server
		}
		if (o instanceof SettingsPacket) {
			//modify camera settings
		}
	}

}
