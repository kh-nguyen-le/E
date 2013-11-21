package securitySystem;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;

public class CameraClient {
	private Client client;
	private static java.net.InetAddress gateway;
	
	public CameraClient() {
		client = new Client();
		Network.register(client);
		CameraListener listener = new CameraListener();
                listener.init(client);
		client.addListener(listener);
		do {
			gateway =client.discoverHost(Network.port, 60000);
		}while (gateway==null);
                client.start();
		
		try {
			client.connect(60000, gateway, Network.port, Network.port);
                        client.setTimeout(0);
                        client.setKeepAliveTCP(0);
                        client.setKeepAliveUDP(0);
                        

		} catch (IOException e) {
			e.printStackTrace();
			client.stop();
		}
                while(!client.isConnected()){
                    try {
                        client.reconnect(60000);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
	}
	
	public static void main(String[] args) throws InterruptedException {
            new CameraClient();
            while(true){
                     Thread.sleep(999999);
            }
      }
}