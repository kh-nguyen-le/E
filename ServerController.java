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
        String label;
        Object o = e.getSource();
        if(o instanceof JButton) {
            JButton button = (JButton)o;
            label = button.getToolTipText();
            if(label.equals("Toggle Left")|| label.equals("Toggle Right")) {
                Network.MotorPacket motor = new Network.MotorPacket();
                String camera = "Camera";
                //code to get camera name here
                if (label.equals("Toggle Left")) motor.direction = false; else motor.direction = true;
                Connection[] list = server.getConnections();
                for (int i=0; i<list.length; i++){
                        if (list[i].toString().equals(camera)) list[i].sendTCP(motor);
                }
            }else if(label.equals("Zoom In")) {
                System.out.println("Zooming in...");
                
            }
            else if(label.equals("Zoom Out")) {
                System.out.println("Zooming out...");
            }
            else if(label.equals("Record")) {
                System.out.println("test");
                view.record();
            }
            else if(label.equals("Screen Capture")) {
                view.snapshot();
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
                view.openDir(0);
            }
            else if(label.equals("Help")) {
                view.openHelp();
            } 
        }
        else {
            JMenuItem item = (JMenuItem)o;
            label = item.getText();
            if(label.equals("Toggle Left")|| label.equals("Toggle Right")) {
                Network.MotorPacket motor = new Network.MotorPacket();
                String camera = "Camera";
                //code to get camera name here
                if (label.equals("Toggle Left")) motor.direction = false; else motor.direction = true;
                Connection[] list = server.getConnections();
                for (int i=0; i<list.length; i++){
                        if (list[i].toString().equals(camera)) list[i].sendTCP(motor);
                }
            }
            else if(label.equals("Zoom In")) {
                System.out.println("Zooming in...");
            }
            else if(label.equals("Zoom Out")) {
                System.out.println("Zooming out...");
            }
            else if(label.equals("Screen Capture")) {
                view.snapshot();
            }
            else if(label.equals("Record Video")) {
                view.record();
            }
            else if(label.equals("Close")) {
                System.exit(0);
            }
            else if(label.equals("Video Recorded")) {
                view.openDir(2);
            }
            else if(label.equals("Picture Captured")) {
                view.openDir(1);
            }
            else if(label.equals("Settings")) {
                System.out.println("settings");
            }
            else if(label.equals("Update")) {
                System.out.println("updating...");
            }
            else if(label.equals("Help")) {
                view.openHelp();
            }
            else if(label.equals("About")) {
                System.out.println("about");
            }
        }
    }
    
    public void setPath1(String ip){
        view.setMediaPath1(ip);
        view.updateMedia();
    }
    
    public void setPath2(String ip){
        view.setMediaPath2(ip);
        view.updateMedia();
    }

    public void setPath3(String ip){
        view.setMediaPath3(ip);
        view.updateMedia();
    }

    public void setPath4(String ip){
        view.setMediaPath4(ip);
        view.updateMedia();
    }
    
    public void setBlank(int pathNo){
        view.setBlank(pathNo);
    }
    
    public void init(Server server, boolean alarm){
        this.server = server;
        this.alarm = alarm;
    }
}
