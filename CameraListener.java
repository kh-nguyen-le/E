package securitySystem;

import com.esotericsoftware.kryonet.Client;
import securitySystem.Network.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class CameraListener extends Listener {
    
        private Client client;
        
        
        public void init(Client client) {
            this.client = client;
        }
        
	public void connected(Connection c) {
                System.out.println("Camera Connected");
		AuthenticationPacket request = new AuthenticationPacket();
		System.out.println("Sends authentication");
                client.sendTCP(request);
	}
        
      //  public void disconnected(Connection c){ System.out.println("Client disconnected");}

	public void received(Connection c, Object o) {
              	//if (o instanceof AuthenticationPacket){
		//	HandshakePacket handshake = new HandshakePacket();
		//	handshake.success = true;
		//	c.sendTCP(handshake);
		//}
		
                if (o instanceof HandshakePacket) {
			if (((HandshakePacket) o).success) {
				MessagePacket name = new MessagePacket();
				name.message = "Camera";
				client.sendTCP(name);
			}
		}
		if (o instanceof MotorPacket) {
			//turn motor
		}
		if (o instanceof SnapshotPacket) {
                    try {
                        //snapshot request from server; camera sends image with alert off
                        
                        System.out.println("Send image taken to server");
                        //Taking a picture with the  PI_CAMERA with the use of 'raspistill' command line command
                        Runtime rt = Runtime.getRuntime();
                        Process pr = rt.exec("raspistill -v -o snapshot1.png");
                        //Picture stored
                        ((SnapshotPacket)o).image = ImageIO.read(new File("snapshot1.png"));
                        //Alert not set because the snapshot was requested by user manually, not due to intrusion
                        ((SnapshotPacket)o).alert = false;
                        client.sendTCP((SnapshotPacket)o);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
		}
                
                if(o instanceof MotionDetectedPacket){
                    //Motion detector sends meesage to Camera notifying of intrusion
                    //Camera takes picture and sets alert on
                    System.out.println("Motion detected");
                        try {
                        //snapshot request from server; camera sends image with alert off
                        SnapshotPacket sp = new SnapshotPacket();
                            
                        System.out.println("Send image taken to server");
                        //Taking a picture with the  PI_CAMERA with the use of 'raspistill' command line command
                        Runtime rt = Runtime.getRuntime();
                        Process pr = rt.exec("raspistill -v -o snapshot1.png");
                        //Picture stored
                        sp.image = ImageIO.read(new File("snapshot1.png"));
                        //Alert set because the snapshot was required due to intrusion
                        sp.alert = true;
                        client.sendTCP((SnapshotPacket)o);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                
		if (o instanceof VideoStreamPacket) {
			//stream request from server
		}
		if (o instanceof SettingsPacket) {
			//modify camera settings
		}
	}

}
