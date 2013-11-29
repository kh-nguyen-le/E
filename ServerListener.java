package securitySystem;


//import java.awt.Image;
import securitySystem.Network.*;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class ServerListener extends Listener {
	private Server server;
        private BufferedImage img;
        private String host, username, password;
        private int port;
        private MyOzSmsClient osc;
	//private ServerController controller;
        private boolean alarm;
        
        public void init(Server server, boolean alarm) {
		this.server = server;
              //  this.controller = controller;
                this.alarm = alarm;
                //Initialising parameters for Ozeki SMS Client
              /*  host = "localhost";
                port = 9500;
                username = "admin";
                password = "ozekiTeddy";
                */
	}
                
        public void connected(Connection c){
            //try {
        
                c.setTimeout(0);
                c.setKeepAliveTCP(0);
                
                //Connect to Ozeki NG SMS Gateway and logging in.
                //osc = new MyOzSmsClient(host, port);
                //osc.login(username, password);
            //} catch (IOException ex) {
              //  ex.printStackTrace();
            //} catch (InterruptedException ex) {
              //  ex.printStackTrace();
            //}
        }

        public void disconnected(Connection c){
            if(c.toString().contains("Camera")){}
        }

        
	public void received(Connection c, Object o) {
		if (o instanceof AuthenticationPacket){
			HandshakePacket handshake = new HandshakePacket();
			handshake.success = true;
			c.sendTCP(handshake);
		}
		if (o instanceof MessagePacket) {
			//sets the name of each client connection on first connect
			String name = ((MessagePacket) o).message;
			//if (name=="Camera") name + cameraid //for multiple cameras
			c.setName(name);
		}
		if (o instanceof AlertPacket) {
                        //This is what happens when the server receives the snapshot image
			if (((AlertPacket) o).alarmOn) {
				//sets off alarm on alarm client
				Connection[] list = server.getConnections();
				for (int i=0; i<list.length; i++){
					if (list[i].toString().contains("Alarm")) list[i].sendTCP(o);
				}
                                alarm = true;
                        }		
                        
                        //Alert owner of intruder through text
              /*          if(osc.isLoggedIn()) {
                            try {	
                                osc.sendMessage("+16138781790", "Alert!");
                            } catch (UnsupportedEncodingException ex) {
                                ex.printStackTrace();
                            }
                        }*/
                        
		}
        }
}

