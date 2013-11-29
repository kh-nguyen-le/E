package securitySystem;

import com.esotericsoftware.kryonet.*;
import javax.swing.*;
import securitySystem.Network.*;

class ServerController implements java.awt.event.ActionListener
{
    private ServerView view;
    private Server server;
    public boolean alarm;
    
    ServerController(){
        
        view = new ServerView();
        view.addController(this);
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e)
    {
        Object o = e.getSource();
        
        
        if(o instanceof JButton) {
            JButton button = (JButton)o;
            String label = button.getToolTipText();
            if(label.equals("Toggle Left")|| label.equals("Toggle Right")) {
                Network.MotorPacket motor = new Network.MotorPacket();
                String camera = "Camera";
                //code to get camera name here
                if (label.equals("left")) motor.direction = false; else motor.direction = true;
                Connection[] list = server.getConnections();
                for (int i=0; i<list.length; i++){
                        if (list[i].toString().equals(camera)) list[i].sendTCP(motor);
                }
            }else if(label.equals("Zoom In")) {
                System.out.println("zoomin");
                
            }
            else if(label.equals("Zoom Out")) {
                System.out.println("zoomout");
            }
            else if(label.equals("Record")) {
                view.record();
            }
            else if(label.equals("Screen Capture")) {
                System.out.println("Screen Capture");
                
            }
            else if(label.equals("Switch Camera")) {
                view.switchCam();
            }
            else if(label.equals("Alarm Toggle")) {             
                AlertPacket ap = new AlertPacket();
                if(alarm){ 
                    ap.alarmOn = false; 
                    alarm = false;
                }else{
                    ap.alarmOn = true;
                    alarm = true;       
                }
                Connection[] list = server.getConnections();
                for (int i=0; i<list.length; i++){
                        if (list[i].toString().contains("Alarm")) list[i].sendTCP(ap);
                }
            }
            else if(label.equals("Settings")) {
                System.out.println("settings");
            }
            else if(label.equals("Storage")) {
                System.out.println("storage");
            }
            else if(label.equals("Help")) {
                System.out.println("Help");
            } 
        }
	}
	
	public void init(Server server, boolean alarm){
		this.server = server;
                this.alarm = alarm;
	}
}
