package RPiSecurityServer;


import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.*;        
import javax.imageio.*;


public class CentralServer {

    ServerSocket server = null;
    Socket alarmSocket = null;//Client Socket that communicates with PiFace containing the alarm
    Socket cameraSocket = null;//Client socket that communicates with Raspberry Pi hosting the camera
    String camReq,  aTrigger, cACK, aACK; 
    BufferedImage image;
    String host, username, password;
    int port;
    MyOzSmsClient osc;
    
    public CentralServer(){
        host = "localhost";
        port = 9500;
        username = "admin";
        password = "ozekiTeddy";  
    }
    
    public void serverRun() throws InterruptedException{        
            try {
                server = new ServerSocket(8080);
                String hostname = server.getInetAddress().getHostName();
                System.out.println("Waiting for clients at "+ hostname + "....");
                cameraSocket = server.accept();
                alarmSocket = server.accept();
                cameraSocket.setKeepAlive(true);
                alarmSocket.setKeepAlive(true);
                
                /////////////////////////////////////////////////////
                //Connect to Ozeki NG SMS Gateway and logging in.
                osc = new MyOzSmsClient(host, port);
                osc.login(username, password);
                ////////////////////////////////////////////////////
                
                
                PrintWriter outToAlarm = new PrintWriter(alarmSocket.getOutputStream(), true);
                BufferedReader inFromAlarm = new BufferedReader(new InputStreamReader(alarmSocket.getInputStream()));
                System.out.println("Connected to Alarm Module: "+ alarmSocket.toString());
                outToAlarm.println("Connection successful\n");
                PrintWriter outToCamera = new PrintWriter(cameraSocket.getOutputStream(), true);
                BufferedReader inFromCamera = new BufferedReader(new InputStreamReader(cameraSocket.getInputStream()));
                DataInputStream din = new DataInputStream(cameraSocket.getInputStream());
                DataOutputStream dout = new DataOutputStream(cameraSocket.getOutputStream());
                System.out.println("Connected to Camera Module: "+ cameraSocket.toString());
                outToCamera.println("Connection successful\n");//'\n' is not translated at the receiving end
                

                
                while((aACK = inFromAlarm.readLine()) != null){
                    System.out.println(aACK);
                    if(aACK.equalsIgnoreCase("Alarm: Received ACK")){
                        System.out.println("ACK received from Alarm");
                        break;
                    }
                }                
                
                while((cACK = inFromCamera.readLine())!= null){
                        System.out.println(cACK);
                    if(cACK.equalsIgnoreCase("Camera: Received ACK")){
                        System.out.println("ACK received from Camera");
                        break;
                    }
               }
                

                while(true){
                    System.out.print("Enter an option(1,2 or 3): ");
                    Scanner s = new Scanner(System.in);
                    int option = s.nextInt();
                    System.out.print(Integer.toString(option) + "\n");
                    switch(option){                              
                        case 1:
                           //Simulate Intrusion
                           System.out.println("Intrusion simulation:\n1.Request snapshot\n2.Upon receipt, turn on the alarm\n");
                           outToCamera.println("Snapshot Request"); 
                           /*while((camReq = inFromCamera.readLine())!= null){
                               if(camReq.contains("img")){
                                    System.out.println("Camera Snapshot: "+ camReq+"\n");
                                    outToCamera.println("Image " + camReq + " rcvd\n");
                                    outToAlarm.println("ALERT!");
                                    break;
                                   
                               }
                               
                              }*/
                            image = ImageIO.read(ImageIO.createImageInputStream(cameraSocket.getInputStream()));
                            System.out.println("Camera Snapshot: " + image.toString() + "\n");
                            outToCamera.println("Image " + image.toString() + " rcvd\n");
                            outToAlarm.println("ALERT!");
                            if(osc.isLoggedIn()) {
					
				osc.sendMessage("+16138781790", "Alert!");				
				
                            }
                            break;

                        case 2:
                            //request snapshot to display
                           System.out.println("Request snapshot to display");
                           outToCamera.println("Snapshot Request"); 
                           while((camReq = inFromCamera.readLine())!= null){
                               if(camReq.contains("img")){
                                    System.out.println("Camera Snapshot: "+ camReq + "\n");
                                    outToCamera.println("Image "+ camReq +" rcvd\n");
                                    break;
                               }
                           }
                            break;
                            
                        case 3:
                            //turn off alarm
                            System.out.println("Turn off alarm");
                            outToAlarm.println("OFF");    
                            AlarmClient.alarmOn = false;
                            break;
                    
                    }
                }
           } catch (IOException ex) {
                Logger.getLogger(CentralServer.class.getName()).log(Level.SEVERE, null, ex);
           }
    }
    public static void main(String[] args) throws InterruptedException{
        CentralServer central  = new CentralServer();
        central.serverRun();
    }
        
}
