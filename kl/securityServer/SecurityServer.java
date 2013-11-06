package securityServer;

import java.io.IOException;

import securityServer.Packet.*;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

public class SecurityServer {

	private Server server;
	private static int tcpPort, udpPort = 27960;
	
	public SecurityServer() throws IOException {
		server = new Server();
		registerPackets();
		server.bind(tcpPort,udpPort);
		ServerListener listener = new ServerListener();
		listener.init(server);
		server.addListener(listener);
		server.start();
	}
	
	private void registerPackets() {
		Kryo kryo = server.getKryo();
		kryo.register(Packet0Message.class);
		kryo.register(Packet1Alert.class);
		kryo.register(Packet2Camera.class);
		kryo.register(Packet3Snapshot.class);
		kryo.register(Packet4VideoStream.class);
		kryo.register(Packet5AudioStream.class);
	}
	
	public static void main(String[] args) {

		try {
			new SecurityServer();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
