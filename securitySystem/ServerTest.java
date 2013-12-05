package securitySystem;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import static junit.framework.Assert.assertNull;
import junit.framework.TestCase;
import securitySystem.Network.*;

//This class tests that SecurityServer responds properly when receiving packets
public class ServerTest extends TestCase{
    private static Object cObj, aObj;
    private static Client ac, cc;
    
    public static void main(String [] args) throws IOException, InterruptedException{
        
        ac = new Client();
        cc = new Client();
        Network.register(ac);
        Network.register(cc);

        
        ac.addListener(new Listener(){
            public void connected(Connection c){
                System.out.println("Connected");
            }
            
            public void received(Connection c, Object o){
  
                if(o instanceof HandshakePacket){
                    aObj = (HandshakePacket)o;
                    
                }
                
                if(o instanceof AlertPacket){
                    aObj = (AlertPacket)o;
                }                   
                               
                if(o instanceof MotorPacket){
                    aObj = (MotorPacket)o;
                }      
            }
        });
        
        cc.addListener(new Listener(){
            public void connected(Connection c){
                System.out.println("Connected");
            }
            
            public void received(Connection c, Object o){
                if(o instanceof HandshakePacket){
                    cObj = (HandshakePacket)o;
                }
                                                
                if(o instanceof MotorPacket){
                    cObj = (MotorPacket)o;
                }
            
                if(o instanceof AlertPacket){
                    cObj = (AlertPacket)o;
                }                   

            }
        });
        
        SecurityServer ss = new SecurityServer();
        ac.start();
        cc.start();
        Thread.sleep(10000);//allows time to fill login credentials in order to avoid null ServerView
        try{
            ac.connect(60000, "localhost", Network.port, Network.port);
            cc.connect(60000, "localhost", Network.port, Network.port);

            ac.setTimeout(0);
            ac.setKeepAliveTCP(0);
            ac.setKeepAliveUDP(0);
                        
            cc.setTimeout(0);
            cc.setKeepAliveTCP(0);
            cc.setKeepAliveUDP(0);
        }catch(IOException e){
            e.printStackTrace();
        }
        cObj = null;
        aObj = null;
        testAuthentication();
        testMessage();
        testAlert();
        cc.stop();
        ac.stop();
        System.exit(0);
    }
    
    //test part 1 of handshaking
    public static void testAuthentication() throws InterruptedException{
        AuthenticationPacket ap = new AuthenticationPacket();
        ac.sendTCP(ap);
        cc.sendTCP(ap);
        Thread.sleep(1000);
        //expects server to handshake with clients
        assertTrue(aObj instanceof HandshakePacket && cObj instanceof HandshakePacket);
        aObj = null;
        cObj = null;
    }

    //test clients being named
    public static void testMessage() throws InterruptedException{
        MessagePacket alarm = new MessagePacket();
        MessagePacket camera = new MessagePacket();
        alarm.message = "Alarm";
        camera.message = "Camera";
        ac.sendTCP(alarm);
        cc.sendTCP(camera);
        Thread.sleep(5000);
        //expects no packets from servers
        assertNull(aObj);
        assertNull(cObj); 
        aObj = null;
        cObj = null;
    }
    
    //simulates motion detected with camera sending alert packet
    public static void testAlert() throws IOException, InterruptedException{
    	//test invalid packet:expect no response from server
    	AlertPacket ap = new AlertPacket();
        ap.alarmOn = false;
        cc.sendTCP(ap);
        Thread.sleep(5000);
        assertNull(aObj);
        assertNull(cObj);
        //test proper packet: expect only alarm client to receive alert
        ap.alarmOn = true;
        cc.sendTCP(ap);
        Thread.sleep(5000);
        assertNull(cObj);
        assertTrue(aObj instanceof AlertPacket);
     }
    
}
