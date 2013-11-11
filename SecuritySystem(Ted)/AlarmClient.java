/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RPiSecurityServer;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlarmClient {
    
    Socket toServer;
    int port;
    static boolean alarmOn;
    BufferedReader br;
    PrintWriter pw;
        
    public AlarmClient(){
        InputStream in = null;
        try {
            this.alarmOn = false;
            port = 8080;
            toServer = new Socket("anearcan", port);
            OutputStream out = toServer.getOutputStream();
            pw = new PrintWriter(out, true);
            in = toServer.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            br = new BufferedReader(isr);
        } catch (IOException ex) {
            Logger.getLogger(AlarmClient.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } 
    }
    
    public void runSocketClient() throws IOException{
        String testMsg;
        //Connect to hostname 
;
        
        while(true){
            while((testMsg = br.readLine())!= null){
                System.out.println(testMsg);
                if(testMsg.equalsIgnoreCase("connection successful")){
                    System.out.println("Connection to anearcan successful");
                    pw.println("Alarm: Received ACK");
                }
                
                if(testMsg.equalsIgnoreCase("ALERT!")){
                    AlarmClient.alarmOn = true;
                    soundAlarm();
                }
                
                if(testMsg.equalsIgnoreCase("OFF")){
                    System.out.println("Turning off alarm...");
                    AlarmClient.alarmOn = false;      
                }
             }
       }
    }
    
    public synchronized void soundAlarm() throws IOException{
           String msg;
          // synchronized(this){
            //allow for the value of alarm to be changed at all times
               while(AlarmClient.alarmOn){
                try {
                    System.out.println("RING\n");
                    Thread.sleep(500);
                    if((msg = br.readLine())!= null){
                        System.out.println("Received: " + msg);
                        if(msg.equalsIgnoreCase("OFF")){
                            System.out.println("Turning off alarm...");
                            AlarmClient.alarmOn = false;      
                        }
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(AlarmClient.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println("sleep interrupted");
                    ex.printStackTrace();
                }
             }
           //}
    }
    
    public static void main(String[] args) throws MalformedURLException, IOException{
        AlarmClient client  = new AlarmClient();
        client.runSocketClient();
    }
}
