package securitySystem;

import java.io.IOException;
import java.awt.event.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import java.util.Scanner;
import securitySystem.Network.*;

public class SecurityServer {

	private Server server;
	public SecurityServer() throws IOException {
	
        server = new Server();
                
        //Registering Packets
		Network.register(server);
		server.bind(Network.port, Network.port);
                
        //Set up Server Controller for GUI
        ServerController sc = new ServerController();
        
        //Set up Server listener
        ServerListener listener = new ServerListener();
        
        //Controller is passed to listener for setting camera paths/taking snapshot
        sc.init(server);
        listener.init(server,sc);
        server.addListener(listener);
        server.start();
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
