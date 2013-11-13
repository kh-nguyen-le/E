package securitySystem;

import securitySystem.Network.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class AlarmListener extends Listener {
	
	public void connected(Connection c) {
		ConnectionRequestPacket request = new ConnectionRequestPacket();
		c.sendTCP(request);
	}

	public void received(Connection c, Object o) {
		if (o instanceof HandshakePacket) {
			if (((HandshakePacket) o).success) {
				MessagePacket name = new MessagePacket();
				name.message = "Alarm";
				c.sendTCP(name);
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

