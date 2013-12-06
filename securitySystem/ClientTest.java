package securitySystem;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import junit.framework.*;

import securitySystem.Network.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.Assert.*;
        
public class ClientTest extends TestCase{
    private static Server server;
    private static Object obj = null;
    
    public static void main(String[] args) throws InterruptedException{
        server = new Server();
        Network.register(server);
        try {
            server.bind(Network.port, Network.port);
        } catch (IOException ex) {
            Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
	
        server.addListener(new Listener(){//barebones server functions to isolate clients
            public void received(Connection c, Object o){
                if(o instanceof MessagePacket){
                   obj = (MessagePacket)o;
                   c.setName(((MessagePacket)o).message);
                   c.setTimeout(0);
                   c.setKeepAliveTCP(0);
                }
                
                if(o instanceof AlertPacket){
                    obj = (AlertPacket)o;
                }
                
                if(o instanceof MotorPacket){
                    obj = (MotorPacket)o;
                }
                
                if(o instanceof AuthenticationPacket){
                    obj = (AuthenticationPacket)o;
                    HandshakePacket hs = new HandshakePacket();
                    hs.success=true;
                    c.sendTCP(hs);
                }
                
                if(o instanceof HandshakePacket){
                    obj = (HandshakePacket)o;
                }
            }
        });
        server.start();
        //test clients handshaking
        CameraClient cc = new CameraClient();
        Thread.sleep(1000);//allow time for client to authenticate
        //expecting proper names from clients
        assertTrue(obj instanceof MessagePacket);
        assertTrue(((MessagePacket)obj).message.contains("Camera"));       
        obj = null;
        AlarmClient ac = new AlarmClient();
        Thread.sleep(1000);//allow time for client to authenticate
        assertTrue(obj instanceof MessagePacket);
        assertTrue(((MessagePacket)obj).message.contains("Alarm"));       
        obj = null;
        testHandshake();
        testAlert();
        server.stop();
    }
            
    public static void testHandshake() throws InterruptedException{
        //Testing server sending invalid HandshakePacket; expects clients not to respond
        HandshakePacket hf = new HandshakePacket();
        hf.success = false;
        Connection[] list = server.getConnections();
        for (int i=0; i<list.length; i++){
                if (list[i].toString().contains("Alarm")) list[i].sendTCP(hf);
        }
        Thread.sleep(1000);
        assertNull(obj);
        obj = null;
        for (int i=0; i<list.length; i++){
                if (list[i].toString().contains("Camera")) list[i].sendTCP(hf);
        }
        Thread.sleep(1000);
        assertNull(obj);
        obj = null;
    }

    public static void testMotor(){
    //Testing server sending MotorPacket
    }
    public static void testAlert() throws InterruptedException{
        //Testing server sending AlertPacket to alarm;
        //expects alarm to ring with no packets sent back
        AlertPacket alert = new AlertPacket();
        alert.alarmOn = true;
        Connection[] list = server.getConnections();
        for (int i=0; i<list.length; i++){
                if (list[i].toString().contains("Alarm")) list[i].sendTCP(alert);
        }
        Thread.sleep(1000);
        assertNull(obj);
        obj = null;
    }

}
