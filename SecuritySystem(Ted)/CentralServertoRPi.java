
package RPiSecurityServer;

import java.awt.Image;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.*;        
import javax.imageio.*;
import javax.swing.ImageIcon;
import java.math.*;

public class CentralServertoRPi {
    ServerSocket server = null;
    Socket alarmSocket = null;//Client Socket that communicates with PiFace containing the alarm
    Socket cameraSocket = null;//Client socket that communicates with Raspberry Pi hosting the camera
    byte[] camReq, aTrigger, cACK, aACK, camSnap; 
    String camReply;
    BufferedImage image;
    ImageIcon imageIcon;
    Image im;
    String host, username, password;
    int port;
    MyOzSmsClient osc;

    public CentralServertoRPi() throws IOException, InterruptedException{
        cACK = new byte[1024];
        aACK = new byte[1024];
        camReq = new byte[1024];
        camSnap = new byte[256000];
        host = "localhost";
        port = 9500;
        username = "admin";
        password = "ozekiTeddy";    


    }
    
    public void serverRun() throws IOException, InterruptedException{
        server = new ServerSocket(8080);
        String hostname = server.getInetAddress().getHostName();
        System.out.println("Waiting for clients at "+ hostname + "....");
        alarmSocket = server.accept();
        alarmSocket.setKeepAlive(true);
        cameraSocket = server.accept();
        cameraSocket.setKeepAlive(true);
        
        //Connect to Ozeki NG SMS Gateway and logging in.
        osc = new MyOzSmsClient(host, port);
        osc.login(username, password);
        
        //PrintWriter outToAlarm = new PrintWriter(alarmSocket.getOutputStream(), true);
        //BufferedReader inFromAlarm = new BufferedReader(new InputStreamReader(alarmSocket.getInputStream()));
        DataInputStream ain = new DataInputStream(alarmSocket.getInputStream());
        DataOutputStream aout = new DataOutputStream(alarmSocket.getOutputStream());

        System.out.println("Connected to Alarm Module: "+ alarmSocket.toString());
        aout.write("Connection to Alarm successful".getBytes());
      //  PrintWriter outToCamera = new PrintWriter(cameraSocket.getOutputStream(), true);
        //BufferedReader inFromCamera = new BufferedReader(new InputStreamReader(cameraSocket.getInputStream()));
        while(ain.read(aACK) != 1){
            System.out.println("Received "+ new String(aACK, "UTF-8")+" from Alarm");
            break;
        }
        
        DataInputStream din = new DataInputStream(cameraSocket.getInputStream());
        DataOutputStream dout = new DataOutputStream(cameraSocket.getOutputStream());
        System.out.println("Connected to Camera Module: "+ cameraSocket.toString());
        dout.write("Connectionto Camera successful".getBytes());
        
        while(din.read(cACK) != -1){
            System.out.println("Received "+ new String(cACK, "UTF-8")+" from Camera");
            break;
        }         
        
        
         while(true){
                    System.out.print("Enter an option(1, 2, 3 or 4): ");
                    Scanner s = new Scanner(System.in);
                    int option = s.nextInt();
                    System.out.print(Integer.toString(option) + "\n");
                    switch(option){                              
                        case 1:
                           //Simulate Intrusion
                           System.out.println("Intrusion simulation:\n1.Request snapshot\n2.Upon receipt, turn on the alarm\n");
                           /*outToCamera.println("Snapshot Request"); 
                            image = ImageIO.read(ImageIO.createImageInputStream(cameraSocket.getInputStream()));
                            System.out.println("Camera Snapshot: " + image.toString() + "\n");
                            outToCamera.println("Image " + image.toString() + " rcvd\n");*/
                            //Request snapshot
                            dout.write("Snapshot Request\n".getBytes());
                            imageIcon = new ImageIcon(camSnap);
                            im = imageIcon.getImage();
                            image = new BufferedImage(Math.abs(imageIcon.getIconWidth()*20), Math.abs(imageIcon.getIconHeight()*20), BufferedImage.TYPE_INT_RGB);
                            image.getGraphics().drawImage(im, 0, 0 , null);
                            dout.write(("Image "+ image.toString() +" rcvd\n").getBytes());
                            System.out.println("Setting off alarm\n");
                            aout.write("ALERT!".getBytes());
                            if(osc.isLoggedIn()) {
					
				osc.sendMessage("+16138781790", "Alert!");				
				
                            }
                            break;

                        case 2:
                            //request snapshot to display
                           System.out.println("Request snapshot to display");
                           dout.write("Snapshot Request\n".getBytes());
                           imageIcon = new ImageIcon(camSnap);
                           im = imageIcon.getImage();
                           image = new BufferedImage(Math.abs(imageIcon.getIconWidth()*20), Math.abs(imageIcon.getIconHeight()*20), BufferedImage.TYPE_INT_RGB);
                           image.getGraphics().drawImage(im, 0, 0 , null);
                           dout.write(("Image "+ image.toString() +" rcvd\n").getBytes());
                        //}
                            break;
                            
                        case 3:
                            //turn off alarm
                            System.out.println("Turn off alarm");
                            aout.write("OFF".getBytes());    
                            break;
                    
                        case 4:
                            //Turn on alarm
                            System.out.println("Turn on alarm");
                            aout.write("ALERT!".getBytes());
                            
                    }
                }
           }
        

    
    public static void main(String[] args) throws IOException, InterruptedException{
        CentralServertoRPi cs = new CentralServertoRPi();
        cs.serverRun();
    }
    
}
    