package securityServer;

//import java.awt.Image;

import securityServer.Packet.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ServerListener extends Listener {
	private Server server;
	public void init(Server server) {
		this.server = server;
	}

	public void received(Connection c, Object o) {
		if (o instanceof Packet0Message) {
			//sets the name of each client connection on first connect
			String name = ((Packet0Message) o).message;
			c.setName(name);
		}
		if (o instanceof Packet3Snapshot) {
			//sets off alarm on intercom
			Connection[] list = server.getConnections();
			Packet1Alert alert = new Packet1Alert();
			for (int i=0; i<list.length; i++){
				if (list[i].toString()=="Intercom") list[i].sendTCP(alert);
			}
			
			//code to send image here
			//Image image = ((Packet3Snapshot) o).image;
		}
		if (o instanceof Packet4VideoStream){
			
		}
		
	}
}
