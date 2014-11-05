package securitySystem;


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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFrame;

public class ServerListener extends Listener {
		private Server server;
        private String host, username, password,email,epass;
        private int port;
        private ServerController controller;
        private int cameraID = 0;
        private String cameraIP;
        private Session session;
        
        public void init(Server server, ServerController sc) {
            this.server = server;
            controller = sc;
            TwilioText.init(); //Initialise texting tool, Twilio
	}
                
        public void connected(Connection c){
        	//turn off timeout for each client
            c.setTimeout(0);
            c.setKeepAliveTCP(0);
        }

        public void disconnected(Connection c){
            String name = c.toString();
            if(name.contains("Camera")){
                //Extracting the disconnected camera's ID and setting media player blank
                String[] stringArray = name.split(" ");   
                int id = Integer.parseInt(stringArray[1]);
                if(cameraID != id){
                    Connection[] list = server.getConnections();
                    
                    switch(id){
                        case 3:
                            for (int i=0; i<list.length; i++){
                                if (list[i].toString().contains("4")) {
                                    list[i].setName("Camera 3");
                                    controller.setPath3(list[i].getRemoteAddressTCP().getHostName());
                                }
                            }
                        case 2:
                            for (int i=0; i<list.length; i++){
                                if (list[i].toString().contains("3")) {
                                    list[i].setName("Camera 2");
                                    controller.setPath2(list[i].getRemoteAddressTCP().getHostName());
                                }
                            }
                        case 1:
                            for (int i=0; i<list.length; i++){
                                if (list[i].toString().contains("2")) {
                                    list[i].setName("Camera 1");
                                    controller.setPath1(list[i].getRemoteAddressTCP().getHostName());
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
                controller.setBlank(cameraID);
                cameraID--;
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
			if (name.equals("Camera")){
                cameraID++;
                name = name +" "+Integer.toString(cameraID); //for multiple cameras  
                cameraIP = c.getRemoteAddressTCP().getHostName();
                switch(cameraID){
                    case 1:
                        controller.setPath1(cameraIP);
                        break;

                    case 2:
                        controller.setPath2(cameraIP);
                        break;

                    case 3:
                        controller.setPath3(cameraIP);
                        break;

                    case 4:
                        controller.setPath4(cameraIP);
                        break;

                    default:
                        System.out.println("4 cameras already connected");
                        break;
                }                       
            }
            c.setName(name);
		}
                
		if (o instanceof AlertPacket) {
            //This is what happens when the server receives the snapshot image
			System.out.println("AlertPacket received");
            if(((AlertPacket) o).alarmOn){
				//Sets off alarm on alarm client
                System.out.println("Alarm on!");
				Connection[] list = server.getConnections();
				for (int i=0; i<list.length; i++){
					if (list[i].toString().contains("Alarm")) list[i].sendTCP(o);
				}
                controller.intrusion(); //Informs View of intrusion
                //Alert owner of intruder through SMS message and picture sent to email
	            System.out.println(controller.getInfo().toString());
			}
		}
    }
}

