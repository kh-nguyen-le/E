package alarmClient;

import securityServer.Packet.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class AlarmListener extends Listener {
	
	public void connected(Connection c) {
		Packet0Message name = new Packet0Message();
		name.message = "Alarm";
		c.sendTCP(name);
	}

	public void received(Connection c, Object o) {
		if (o instanceof Packet1Alert) {
			//sound alarm
		}
	}
}

