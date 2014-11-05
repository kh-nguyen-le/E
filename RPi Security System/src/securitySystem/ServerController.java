package securitySystem;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.esotericsoftware.kryonet.*;
import com.twilio.sdk.TwilioRestException;

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
import javax.swing.*;

import securitySystem.Network.*;

class ServerController implements java.awt.event.ActionListener
{
    private ServerView view;
    private LoginView login;
    private SettingsView setting;
    private Server server;
    private UserInformation user;
    private boolean alarm = false;
    private Session session;
    private String email,epass;
    
    ServerController(){
        login = new LoginView();
        login.addListener(this);
        user = new UserInformation(new ParseXML().getXMLUser());
        
        email="aeakai@yahoo.com";
        epass="yahooTeddy91";
        try {    
            //Connect to Ozeki NG SMS Gateway and logging in.
            //osc = new MyOzSmsClient(host, port);
            //osc.login(username, password);
        	//Use Twilio instead
        	
        	
            //Setting up SMTP server and email 
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.mail.yahoo.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class",
                            "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(email,epass);
                        }
                });
        
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public UserInformation getInfo(){
        return user;
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
                toggleAlarm();
            }else if(label.equals("Settings")) {                
                user = new UserInformation(new ParseXML().getXMLUser());
                System.out.println(user.getemail()+" "+ user.getphoneNumber());
                setting = new SettingsView(user);       
                setting.addListener(this);
            
            }else if(label.equals("Save")) {
                new ToXML(setting.getUser());
                setting.setVisible(false);
            }else if(label.equals("Storage")) {
                view.openDir(0);
            }else if(label.equals("Help")) {
                view.openHelp();
            }else if(label.equals("Login")){
                if(login.getUser().getname().equals(user.getname())&&login.getUser().getpassword().equals(user.getpassword())) {
                    view = new ServerView();                    
                    view.addController(this);
                    view.setUser(user);//In case the user information may ever need to be displayed
                    login.setVisible(false);
                    
                }else
                    JOptionPane.showMessageDialog(null,"The username or password you entered is incorrect.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }else if(label.equals("Cancel")){
                System.exit(0);
            } 
      }else {
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
            }else if(label.equals("Zoom In")) {
                System.out.println("Zooming in...");
            }else if(label.equals("Zoom Out")) {
                System.out.println("Zooming out...");
            }else if(label.equals("Screen Capture")) {
                view.snapshot();
            }else if(label.equals("Record Video")) {
                view.record();
            }else if(label.equals("Close")) {
                System.exit(0);
            }else if(label.equals("Video Recorded")) {
                view.openDir(2);
            }else if(label.equals("Picture Captured")) {
                view.openDir(1);
            }else if(label.equals("Settings")) {
                user = new UserInformation(new ParseXML().getXMLUser());
               System.out.println(user.getemail()+" "+user.getphoneNumber());
                setting = new SettingsView(user);
            }else if(label.equals("Update")) {
                System.out.println("updating...");
            }else if(label.equals("Help")) {
                view.openHelp();
            }else if(label.equals("About")) {
                System.out.println("about");
            }else if(label.equals("Simulate Intrusion")){
            	System.out.println("Simulating Intrusion");
            	intrusion();
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
    
    public String getSnapshotPath(){
        return view.getSnapshotStoragePath();
    }
    
    public void init(Server server){
        this.server = server;
    }
    //runs after motion detected
    public void intrusion(){
        System.out.println("Intrusion snapsot request");
    	view.snapshot();
    	this.alarm = false;
    	toggleAlarm();
    	sendEmail();
    	try {
			TwilioText.sendTextMessage(user.getphoneNumber(), "Intruder! Check your email for image of intruder.");
		} catch (TwilioRestException e) {
			// TODO Auto-generated catch block
			System.err.println("Text Message did not send.\n");
			e.printStackTrace();
		}
    }
    
    private void sendEmail() {
    	 try{
             BodyPart msgBodyPart = new MimeBodyPart();
             Message message = new MimeMessage(session);
             message.setFrom(new InternetAddress(email));
             message.setRecipients(Message.RecipientType.TO,
                             InternetAddress.parse(getInfo().getemail()));
             message.setSubject("Intruder Alert!");

             msgBodyPart.setText("You have an unexpected visitor\nSee Intruder in attached image\n");

             Multipart mp = new MimeMultipart();
             mp.addBodyPart(msgBodyPart);
             msgBodyPart = new MimeBodyPart();                                
             String filename = getSnapshotPath();
             DataSource source = new FileDataSource(filename);
             msgBodyPart.setDataHandler(new DataHandler(source));
             msgBodyPart.setFileName(filename);
             mp.addBodyPart(msgBodyPart);
             message.setContent(mp);

             Transport.send(message);

             System.out.println("Email sent");
             
         } catch (MessagingException ex) {
             Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
         }
	}

	public void toggleAlarm(){
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
    
}
