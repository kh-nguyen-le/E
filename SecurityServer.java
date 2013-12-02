package securitySystem;

import java.io.IOException;
import java.awt.event.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import java.util.Scanner;
import securitySystem.Network.*;

public class SecurityServer {//implements ActionListener {

	private Server server;
        private boolean alarm = false;
	public SecurityServer() throws IOException {
                
		server = new Server();
                //Registering Packets
		Network.register(server);
		server.bind(Network.port, Network.port);
                //Set up Server listener
                ServerController sc = new ServerController();		
                ServerListener listener = new ServerListener();
		listener.init(server, alarm);
		server.addListener(listener);
                server.start();
                sc.init(server, alarm);
	}
	
	
	public static void main(String[] args) {

		try {
			new SecurityServer();
                        Log.set(Log.LEVEL_TRACE);
		} catch (IOException e) {
			e.printStackTrace();
		}
                                     
	}
}
