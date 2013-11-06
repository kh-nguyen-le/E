package intercomClient;

import intercomClient.Packet.Packet0Message;
import intercomClient.Packet.Packet1Alert;
import intercomClient.Packet.Packet5AudioStream;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class IntercomListener extends Listener {
	
	public void connected(Connection c) {
		Packet0Message name = new Packet0Message();
		name.message = "Intercom";
		c.sendTCP(name);
	}

	public void received(Connection c, Object o) {
		if (o instanceof Packet1Alert) {
			//sound alarm
		}
		if (o instanceof Packet5AudioStream) {
			//activate microphone
		}
	}
}
