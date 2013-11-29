/**
 * Write a description of class View here.
 * 
 * @author Yuan Sun
 * @version v1.0
 * 
 * issues:
 * 1. have to activate the cam before running the program otherwise it wont show, can make a seperate thread for that.
 * 3. methods of determine whether the streaming is on.
 */
package securitySystem;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.*;

import uk.co.caprica.vlcj.*;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;


public class ServerView// implements java.util.Observer
{
    private Canvas canvas1;
    private Canvas canvas2;
    private Canvas canvas3;
    private Canvas canvas4;
    private EmbeddedMediaPlayer mPlayer1;
    private EmbeddedMediaPlayer mPlayer2;
    private EmbeddedMediaPlayer mPlayer3;
    private EmbeddedMediaPlayer mPlayer4;
    
    private String vlcPath = "C:\\Program Files\\VideoLAN\\VLC";
    private String mediaPath1 = "C:\\Users\\Teddy\\Pictures\\images1.png";//"rtsp://@192.168.0.12:8554/";
    private String mediaPath2 = "C:\\Users\\Teddy\\Pictures\\image.png";
    private String mediaPath3 = "C:\\Users\\Teddy\\Pictures\\images2.png";
    private String mediaPath4 = "C:\\Users\\Teddy\\Pictures\\images3.png";
    private String fileName;
    private int switchCount = 0;
    private int recordCount = 0;

    private JButton leftButton,rightButton,zoomInButton,zoomOutButton,recordButton,screenCapture,switchCam,alarmToggle,settings,storage, help;
    
    public ServerView()
    {
        JFrame frame = new JFrame("Home Security System");
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new FlowLayout());
        JPanel largeCamPanel = new JPanel();
        JPanel smallCamPanel = new JPanel(new FlowLayout());
        JPanel camPanel = new JPanel();
        camPanel.setLayout(new BoxLayout(camPanel, BoxLayout.Y_AXIS));
        JPanel buttonPanel = new JPanel(new GridLayout(3,2,5,5));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
		//Setting up the menu bars
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        JMenuBar menu = new JMenuBar();
        frame.setJMenuBar(menu);
        JMenu control = new JMenu("Control");
        menu.add(control);
        JMenu history = new JMenu("History");
        menu.add(history);
        JMenu admin = new JMenu("Admin");
        menu.add(admin);
        JMenu help = new JMenu("Help");
        menu.add(help);
        //Setting up the menu items in control bar
        JMenuItem left = new JMenuItem("Toggle Left");
        control.add(left);
        JMenuItem right = new JMenuItem("Toggle Right");
        control.add(right);
        JMenuItem zoomIn = new JMenuItem("Zoom In");
        control.add(zoomIn);
        JMenuItem zoomOut = new JMenuItem("Zoom Out");
        control.add(zoomOut);
        JMenuItem capture = new JMenuItem("Screen Capture");
        control.add(capture);
        JMenuItem record = new JMenuItem("Record Video");
        control.add(record);
        JMenuItem close = new JMenuItem("Close");
        control.add(close);
        //Setting up the menu items in history bar
        JMenuItem video = new JMenuItem("Video Recorded");
        history.add(video);
        JMenuItem pic = new JMenuItem("Picture Captured");
        history.add(pic);
        //Setting up the menu items in admin bar
        JMenuItem setContact = new JMenuItem("Set Contact Info");
        admin.add(setContact);
        JMenuItem setPW = new JMenuItem("Set Password");
        admin.add(setPW);
        JMenuItem addCam = new JMenuItem("Add Camera");
        admin.add(addCam);
        //Setting up the menu items in help bar
        JMenuItem update = new JMenuItem("Update");
        help.add(update);
        JMenuItem doc = new JMenuItem("Help");
        help.add(doc);
        JMenuItem about = new JMenuItem("About");
        help.add(about);
        //------------------------------------------------------------
		
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcPath);
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

        canvas1 = new Canvas();
        canvas1.setPreferredSize(new Dimension(450, 300));
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas1);
        mPlayer1 = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mPlayer1.setVideoSurface(videoSurface);

        canvas2 = new Canvas();
        canvas2.setPreferredSize(new Dimension(147, 100));
        videoSurface = mediaPlayerFactory.newVideoSurface(canvas2);
        mPlayer2 = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mPlayer2.setVideoSurface(videoSurface);
        
        canvas3 = new Canvas();
        canvas3.setPreferredSize(new Dimension(146, 100));
        videoSurface = mediaPlayerFactory.newVideoSurface(canvas3);
        mPlayer3 = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mPlayer3.setVideoSurface(videoSurface);
        
        canvas4 = new Canvas();
        canvas4.setPreferredSize(new Dimension(147, 100));
        videoSurface = mediaPlayerFactory.newVideoSurface(canvas4);
        mPlayer4 = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mPlayer4.setVideoSurface(videoSurface);

        largeCamPanel.add(canvas1);
        smallCamPanel.add(canvas2);
        smallCamPanel.add(canvas3);
        smallCamPanel.add(canvas4);
        camPanel.add(largeCamPanel);
        camPanel.add(smallCamPanel);
        contentPane.add(camPanel);
        
        //--------------------date---------------------
        String date = new SimpleDateFormat("hh:mm:ss").format(new Date());
        final JTextField time = new JTextField(date);
        time.setEditable(false);
        time.setFont(new Font("Serif", Font.PLAIN, 50));
        time.setBorder(BorderFactory.createEmptyBorder(0,43,0,0));
        panel.add(time);
        class time extends Thread {
            public void run() {
                while(true) {
                    String newDate = new SimpleDateFormat("hh:mm:ss").format(new Date());
                    time.setText(newDate);
                    try {
                        sleep(1000);
                    }catch(Exception e) {
                        System.out.println(e);
                    }
                }
            }
        }
        time t = new time();
        t.start();
        
        //--------------------control-------------------
        leftButton = new JButton(new ImageIcon("left.png"));
        leftButton.setToolTipText("Toggle Left");
        leftButton.setPreferredSize(new Dimension(50,50));
        c.insets = new Insets(10,10,10,10);
        c.gridx = 2;
        c.gridy = 1;
        controlPanel.add(leftButton,c);
        rightButton = new JButton(new ImageIcon("right.png"));
        rightButton.setToolTipText("Toggle Right");
        rightButton.setPreferredSize(new Dimension(50,50));
        c.gridx = 0;
        c.gridy = 1;
        controlPanel.add(rightButton,c);
        zoomInButton = new JButton(new ImageIcon("zoomin.png"));
        zoomInButton.setToolTipText("Zoom In");
        zoomInButton.setPreferredSize(new Dimension(50,50));
        c.gridx = 1;
        c.gridy = 0;
        controlPanel.add(zoomInButton,c);
        zoomOutButton = new JButton(new ImageIcon("zoomout.png"));
        zoomOutButton.setToolTipText("Zoom Out");
        zoomOutButton.setPreferredSize(new Dimension(50,50));
        c.gridx = 1;
        c.gridy = 2;
        controlPanel.add(zoomOutButton,c);
        recordButton = new JButton(new ImageIcon("record.png"));
        recordButton.setToolTipText("Record");
        recordButton.setPreferredSize(new Dimension(50,50));
        c.gridx = 1;
        c.gridy = 1;
        controlPanel.add(recordButton,c);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        panel.add(controlPanel);
        
        //----------------------button-------------------
        screenCapture = new JButton("Screen Capture");
		screenCapture.setToolTipText("Screen Capture");
        buttonPanel.add(screenCapture);
        switchCam = new JButton("Switch Camera");
		switchCam.setToolTipText("Switch Camera");
        buttonPanel.add(switchCam);
        alarmToggle = new JButton("Alarm Toggle");
		alarmToggle.setToolTipText("Alarm Toggle");
        buttonPanel.add(alarmToggle);
        settings = new JButton("Settings");
		settings.setToolTipText("Settings");
        buttonPanel.add(settings);
        storage = new JButton("Storage");
		storage.setToolTipText("Storage");
        buttonPanel.add(storage);
        help = new JButton("Help");
		help.setToolTipText("Help");
        buttonPanel.add(help);
        panel.add(Box.createRigidArea(new Dimension(0,25)));
        panel.add(buttonPanel);
        
        contentPane.add(panel);
        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(750,480);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
        File dir = new File(System.getProperty("user.home"), "Video Records");
        //File dir = new File("C:\\GroupE");
        dir.mkdirs();
        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        fileName = dir.getAbsolutePath() + "/Capture-" + df.format(new Date()) + ".mpg";
        
        
        mPlayer1.playMedia(mediaPath1);
        mPlayer2.playMedia(mediaPath2);
        mPlayer3.playMedia(mediaPath3);
        mPlayer4.playMedia(mediaPath4);
        
        
        //----------------------open directory------------------------
        /*try {
            Desktop.getDesktop().open(dir);
        } catch(Exception e) {
            System.out.println(e);
        }*/
    }
    
    public void setMediaPath1(String ip){
        mediaPath1 = "rtsp://@" + ip +":8554/";
    }
    
    public void setMediaPath2(String ip){
        mediaPath2 = "rtsp://@" + ip +":8554/";
    }
    
    public void setMediaPath3(String ip){
        mediaPath3 = "rtsp://@" + ip +":8554/";
    }

    public void setMediaPath4(String ip){
        mediaPath4 = "rtsp://@" + ip +":8554/";
    }
    
    public void addController(ServerController controller)
    {
        leftButton.addActionListener(controller);
        rightButton.addActionListener(controller);
        zoomInButton.addActionListener(controller);
        zoomOutButton.addActionListener(controller);
        recordButton.addActionListener(controller);
        screenCapture.addActionListener(controller);
        switchCam.addActionListener(controller);
        alarmToggle.addActionListener(controller);
        settings.addActionListener(controller);
        storage.addActionListener(controller);
        help.addActionListener(controller);
    }
    
    //public void update(Observable obs, Object obj)
    //{ }
    
    public void updateMedia(){
        mPlayer1.playMedia(mediaPath1); 
        mPlayer2.playMedia(mediaPath2);
        mPlayer3.playMedia(mediaPath3);
        mPlayer4.playMedia(mediaPath4);
        
    }
        
    public void record(){	
        if(recordCount == 0) {
            switch(switchCount){
				case 0:
					mPlayer1.playMedia(mediaPath1,":sout=#transcode{vcodec=mpgv,vb=4094,scale=1,acodec=mpg,ab=128,channels=2,samplerate=44100}:duplicate{dst=file{dst=" + fileName + "},dst=display}");
					recordCount = 1;
					break;
				
				case 1:
					mPlayer1.playMedia(mediaPath4,":sout=#transcode{vcodec=mpgv,vb=4094,scale=1,acodec=mpg,ab=128,channels=2,samplerate=44100}:duplicate{dst=file{dst=" + fileName + "},dst=display}");
					recordCount = 1;
					break;
					
				case 2:
					mPlayer1.playMedia(mediaPath4,":sout=#transcode{vcodec=mpgv,vb=4094,scale=1,acodec=mpg,ab=128,channels=2,samplerate=44100}:duplicate{dst=file{dst=" + fileName + "},dst=display}");
					recordCount = 1;
					break;
				
				case 3:
					mPlayer1.playMedia(mediaPath4,":sout=#transcode{vcodec=mpgv,vb=4094,scale=1,acodec=mpg,ab=128,channels=2,samplerate=44100}:duplicate{dst=file{dst=" + fileName + "},dst=display}");
					recordCount = 1;
					break;
				
				default:
					break;
			}			
        }else {
            switch(switchCount){
				case 0:
					mPlayer1.playMedia(mediaPath1);
					recordCount = 0;
					break;
				
				case 1:
					mPlayer1.playMedia(mediaPath4);
					recordCount = 0;
					break;
					
				case 2:
					mPlayer1.playMedia(mediaPath3);
					recordCount = 0;
					break;
				
				case 3:
					mPlayer1.playMedia(mediaPath2);
					recordCount = 0;
					break;
				
				default:
					break;

            }		
        }
    }
    
    public void switchCam(){
		switch(switchCount) {
			case 0:
				mPlayer1.playMedia(mediaPath4);
				mPlayer2.playMedia(mediaPath1);
				mPlayer3.playMedia(mediaPath2);
				mPlayer4.playMedia(mediaPath3);
				switchCount = 1;
				break;
			
			case 1: 
				mPlayer1.playMedia(mediaPath3);
				mPlayer2.playMedia(mediaPath4);
				mPlayer3.playMedia(mediaPath1);
				mPlayer4.playMedia(mediaPath2);
				switchCount = 2;
				break;
				
			case 2:
				mPlayer1.playMedia(mediaPath2);
				mPlayer2.playMedia(mediaPath3);
				mPlayer3.playMedia(mediaPath4);
				mPlayer4.playMedia(mediaPath1);
				switchCount = 3;
				break;
				
			case 3:
				mPlayer1.playMedia(mediaPath1);
				mPlayer2.playMedia(mediaPath2);
				mPlayer3.playMedia(mediaPath3);
				mPlayer4.playMedia(mediaPath4);
				switchCount = 0;
				break;
				
			default:
				break;
		}	
        }
}
