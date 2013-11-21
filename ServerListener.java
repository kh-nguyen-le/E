package securitySystem;


//import java.awt.Image;
import securitySystem.Network.*;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class ServerListener extends Listener {
	private Server server;
	public void init(Server server) {
		this.server = server;
	}
        
        public void connected(Connection c){
            c.setTimeout(0);
            c.setKeepAliveTCP(0);
        }

	public void received(Connection c, Object o) {
		if (o instanceof AuthenticationPacket){
			HandshakePacket handshake = new HandshakePacket();
			handshake.success = true;
			c.sendTCP(handshake);
		}
		if (o instanceof MessagePacket) {
			//sets the name of each client connection on first connect
			String name = ((MessagePacket) o).message;
			//if (name=="Camera") name + cameraid //for multiple cameras
			c.setName(name);
		}
		if (o instanceof SnapshotPacket) {
			if (((SnapshotPacket) o).alert) {
				//sets off alarm on alarm client
				Connection[] list = server.getConnections();
				AlertPacket alert = new AlertPacket();
				for (int i=0; i<list.length; i++){
					if (list[i].toString()=="Alarm") list[i].sendTCP(alert);
				}
			}
			//code to display image here
			//Image image = ((SnapshotPacket) o).image;
		}
		if (o instanceof VideoStreamPacket){
			
		}
		
	}
}
