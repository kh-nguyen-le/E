package securitySystem;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;

public class CameraClient {
	private Client client;
	private static java.net.InetAddress gateway;
	
	public CameraClient() {
		client = new Client();
		Network.register(client);
		
		client.addListener(new CameraListener());
		
		do {
			gateway =client.discoverHost(Network.port, 60000);
		}while (gateway!=null);
		
		client.start();
		
		try {
			client.connect(60000, gateway, Network.port, Network.port);
		} catch (IOException e) {
			e.printStackTrace();
			client.stop();
		}
	}
	
	public static void main(String[] args) {
        new CameraClient();
		
	}
}