/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package securitySystem;

import com.esotericsoftware.kryonet.Server;
import junit.framework.*;

import securitySystem.Network.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.Assert.*;
import securitySystem.ServerListener;
        
public class DummyServer extends TestCase{
    private static Server server;
    private static Object obj = null;
    
    public static void main(String[] args) throws InterruptedException{
        server = new Server();
        Network.register(server);
        try {
            server.bind(Network.port, Network.port);
        } catch (IOException ex) {
            Logger.getLogger(DummyServer.class.getName()).log(Level.SEVERE, null, ex);
        }
	
	server.addListener(new Listener(){
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
                
                if(o instanceof VideoStreamPacket){
                    obj = (VideoStreamPacket)o;
                }
                
                if(o instanceof SettingsPacket){
                    obj = (SettingsPacket)o;
                }
                
                if(o instanceof SnapshotPacket){
                    obj = (SnapshotPacket)o;
                }
            }
        });
        server.start();
        Thread.sleep(30000);
        obj = null;
        testHandshake();
        testSnapshot();
        testAlert();
    }
    
    public static void testMessage(){}
    
    public static void testAuthentication(){
    }
            
    public static void testHandshake() throws InterruptedException{
        HandshakePacket hf = new HandshakePacket();
        HandshakePacket ht = new HandshakePacket();
        hf.success = false;
        ht.success = true;
        server.sendToAllTCP(hf);
        Thread.sleep(5000);
        assertNull(obj);
        server.sendToAllTCP(ht);
        Thread.sleep(5000);
        assertTrue(obj instanceof MessagePacket);
        obj = null;
    }

    public static void testAudioStream(){}
    public static void testVideoStream(){}
    public static void testMotor(){}
    public static void testSettings(){}
    public static void testSnapshot() throws InterruptedException{
        SnapshotPacket snapshot = new SnapshotPacket();
        Connection[] list = server.getConnections();
        for (int i=0; i<list.length; i++){
                if (list[i].toString().contains("Camera")) list[i].sendTCP(snapshot);
        }
        Thread.sleep(5000);
        assertTrue(obj instanceof SnapshotPacket);
        Image image = ((SnapshotPacket)obj).image;
        boolean alert = ((SnapshotPacket)obj).alert;
        
        assertEquals(alert, false);
        assertNotNull(image);
        obj = null;
    }
    public static void testAlert() throws InterruptedException{
        AlertPacket alert = new AlertPacket();
        Connection[] list = server.getConnections();
        for (int i=0; i<list.length; i++){
                if (list[i].toString().contains("Alarm")) list[i].sendTCP(alert);
        }
        Thread.sleep(5000);
        assertNull(obj);
        obj = null;
        
    }

}
