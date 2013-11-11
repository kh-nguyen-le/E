package intercomClient;

import securityServer.Packet.*;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class IntercomListener extends Listener {
	
	public void connected(Connection c) {
		Packet0Message name = new Packet0Message();
		name.message = "Intercom";
		c.sendTCP(name);
	}

	public void received(Connection c, Object o) {
		if (o instanceof Packet5AudioStream) {
			//activate microphone
		}
	}
}
