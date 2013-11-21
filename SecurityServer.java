package securitySystem;

import java.io.IOException;
import java.awt.event.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import java.util.Scanner;
import securitySystem.Network.*;

public class SecurityServer implements ActionListener {

	private Server server;
        
	public SecurityServer() throws IOException {
		server = new Server();
                //Registering Packets
		Network.register(server);
		server.bind(Network.port, Network.port);
                //Set up Server listener
		ServerListener listener = new ServerListener();
		listener.init(server);
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

	//button listener
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if ("left".equals(action) | "right".equals(action)) {
			MotorPacket motor = new MotorPacket();
			String camera = "Camera";
			//code to get camera name here
			if (action=="left") motor.direction = false; else motor.direction = true;
			Connection[] list = server.getConnections();
			for (int i=0; i<list.length; i++){
				if (list[i].toString()==camera) list[i].sendTCP(motor);
			}
		}	
	}

}
