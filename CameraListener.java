package securitySystem;

import securitySystem.Network.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class CameraListener extends Listener {
	public void connected(Connection c) {
		ConnectionRequestPacket request = new ConnectionRequestPacket();
		c.sendTCP(request);
	}

	public void received(Connection c, Object o) {
		if (o instanceof HandshakePacket) {
			if (((HandshakePacket) o).success) {
				MessagePacket name = new MessagePacket();
				name.message = "Camera";
				c.sendTCP(name);
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
