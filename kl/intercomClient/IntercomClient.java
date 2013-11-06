package intercomClient;

import java.io.IOException;

import intercomClient.Packet.*;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

public class IntercomClient {
	
	private Client client;
	private static String gateway = "10.0.0.1";
	private static int tcpPort, udpPort = 27960;
	
	public IntercomClient() {
		client = new Client();
		registerPackets();
		
		client.addListener(new IntercomListener());
		
		client.start();
		
		try {
			client.connect(60000, gateway, tcpPort, udpPort);
		} catch (IOException e) {
			e.printStackTrace();
			client.stop();
		}
	}
	
	private void registerPackets() {
		Kryo kryo = client.getKryo();
		kryo.register(Packet0Message.class);
		kryo.register(Packet1Alert.class);
		kryo.register(Packet2Camera.class);
		kryo.register(Packet3Snapshot.class);
		kryo.register(Packet4VideoStream.class);
		kryo.register(Packet5AudioStream.class);
	}
	public static void main(String[] args) {
		
		new IntercomClient();
		
	}

}