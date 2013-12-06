package securitySystem;

import java.io.IOException;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AlarmClient {
	private Client client;
	private static java.net.InetAddress gateway;
	
	public AlarmClient()  {
		client = new Client();
		Network.register(client);
		AlarmListener listener = new AlarmListener();
                listener.init(client);
		client.addListener(listener);
		
		do {
                gateway =client.discoverHost(Network.port, 5000);
		}while (gateway==null);	
                client.start();
		
		try {
			Log.set(Log.LEVEL_TRACE);
            client.connect(60000, gateway, Network.port, Network.port);
            client.setTimeout(0);
            client.setKeepAliveTCP(0);
            client.setKeepAliveUDP(0);
		}catch(IOException e){
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
	
	public static void main(String[] args) throws InterruptedException  {
		new AlarmClient();
         }
}
