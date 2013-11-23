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
	
        public void init(Server server) {
		this.server = server;
                //Initialising parameters for Ozeki SMS Client
                host = "localhost";
                port = 9500;
                username = "admin";
                password = "ozekiTeddy";
	}
        
         public void paint(Graphics g) {
            g.drawImage(img, 0, 0, null);
        }

        public Dimension getPreferredSize() {
            if (img == null) {
                 return new Dimension(100,100);
            } else {
               return new Dimension(img.getWidth(null), img.getHeight(null));
           }
        } 
         
        public void connected(Connection c){
            try {
                c.setTimeout(0);
                c.setKeepAliveTCP(0);
                //Connect to Ozeki NG SMS Gateway and logging in.
                osc = new MyOzSmsClient(host, port);
                osc.login(username, password);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
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
		if (o instanceof SnapshotPacket) {
                    //This is what happens when the server receives the snapshot image
			if (((SnapshotPacket) o).alert) {
				//sets off alarm on alarm client
				Connection[] list = server.getConnections();
				AlertPacket alert = new AlertPacket();
				for (int i=0; i<list.length; i++){
					if (list[i].toString()=="Alarm") list[i].sendTCP(alert);
				}
			}
			//code to display image here
                       
                        JFrame f = new JFrame("Camera Snapshot");
                        f.addWindowListener(new WindowAdapter(){
                                public void windowClosing(WindowEvent e) {
                                    System.exit(0);
                                }
                            });

                        f.add(new LoadImage((BufferedImage)((SnapshotPacket)o).image));
                        f.pack();
                        f.setVisible(true);
                        
                        //Alert owner of intruder through text
                        if(osc.isLoggedIn()) {
                            try {	
                                osc.sendMessage("+16138781790", "Alert!");
                            } catch (UnsupportedEncodingException ex) {
                                ex.printStackTrace();
                            }
                        }
                        
		}
		if (o instanceof VideoStreamPacket){}
	}
}

class LoadImage extends Component{
    private BufferedImage img;
    
    public LoadImage(BufferedImage bImage){
        img = bImage;
        
    }
         public void paint(Graphics g) {
            g.drawImage(img, 0, 0, null);
        }

        public Dimension getPreferredSize() {
            if (img == null) {
                 return new Dimension(100,100);
            } else {
               return new Dimension(img.getWidth(null), img.getHeight(null));
           }
        } 

}