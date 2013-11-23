/**
 * Write a description of class View here.
 * 
 * @author Yuan Sun
 * @version v1.0
 * 
 * issues:
 * 1. have to activate the cam before running the program otherwise it wont show, can make a seperate thread for that.
 */
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


public class View
{
    
    private EmbeddedMediaPlayerComponent usr1;
    private EmbeddedMediaPlayerComponent usr2;
    private EmbeddedMediaPlayerComponent usr3;
    private EmbeddedMediaPlayerComponent usr4;
    private Canvas canvas1;
    private Canvas canvas2;
    private Canvas canvas3;
    private Canvas canvas4;
    private EmbeddedMediaPlayer mPlayer1;
    private EmbeddedMediaPlayer mPlayer2;
    private EmbeddedMediaPlayer mPlayer3;
    private EmbeddedMediaPlayer mPlayer4;
    
    private String vlcPath = "C:\\Program Files\\VideoLAN\\VLC";
    private String mediaPath1 = "rtsp://@192.168.0.14:8554/";
    private String options = ":sout=#standard{mux=ts,access=file,dst=c:\\capture.avi}";
    private String mediaPath2 = "1.mp4";
    private String mediaPath3 = "2.mp4";
    private String mediaPath4 = "blank.bmp";
    
    public View()
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

        usr1 = new EmbeddedMediaPlayerComponent();
        canvas1 = new Canvas();
        canvas1.setPreferredSize(new Dimension(450, 300));
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas1);
        mPlayer1 = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mPlayer1.setVideoSurface(videoSurface);
        
        usr2 = new EmbeddedMediaPlayerComponent();
        canvas2 = new Canvas();
        canvas2.setPreferredSize(new Dimension(147, 100));
        videoSurface = mediaPlayerFactory.newVideoSurface(canvas2);
        mPlayer2 = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mPlayer2.setVideoSurface(videoSurface);
        
        usr3 = new EmbeddedMediaPlayerComponent();
        canvas3 = new Canvas();
        canvas3.setPreferredSize(new Dimension(146, 100));
        videoSurface = mediaPlayerFactory.newVideoSurface(canvas3);
        mPlayer3 = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mPlayer3.setVideoSurface(videoSurface);
        
        usr4 = new EmbeddedMediaPlayerComponent();
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
        JButton leftButton = new JButton(new ImageIcon("left.png"));
        leftButton.setPreferredSize(new Dimension(50,50));
        c.insets = new Insets(10,10,10,10);
        c.gridx = 2;
        c.gridy = 1;
        controlPanel.add(leftButton,c);
        JButton rightButton = new JButton(new ImageIcon("right.png"));
        rightButton.setPreferredSize(new Dimension(50,50));
        c.gridx = 0;
        c.gridy = 1;
        controlPanel.add(rightButton,c);
        JButton zoomInButton = new JButton(new ImageIcon("zoomin.png"));
        zoomInButton.setPreferredSize(new Dimension(50,50));
        c.gridx = 1;
        c.gridy = 0;
        controlPanel.add(zoomInButton,c);
        JButton zoomOutButton = new JButton(new ImageIcon("zoomout.png"));
        zoomOutButton.setPreferredSize(new Dimension(50,50));
        c.gridx = 1;
        c.gridy = 2;
        controlPanel.add(zoomOutButton,c);
        JButton recordButton = new JButton(new ImageIcon("record.png"));
        recordButton.setPreferredSize(new Dimension(50,50));
        c.gridx = 1;
        c.gridy = 1;
        controlPanel.add(recordButton,c);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        panel.add(controlPanel);
        
        //----------------------button-------------------
        JButton screenCapture = new JButton("Screen Capture");
        buttonPanel.add(screenCapture);
        JButton switchCam = new JButton("Switch Camera");
        buttonPanel.add(switchCam);
        JButton alarmOn = new JButton("Alarm On");
        buttonPanel.add(alarmOn);
        JButton alarmOff = new JButton("Alarm Off");
        buttonPanel.add(alarmOff);
        JButton addCamera = new JButton("Add Camera");
        buttonPanel.add(addCamera);
        JButton removeCamera = new JButton("Remove Camera");
        buttonPanel.add(removeCamera);
        panel.add(Box.createRigidArea(new Dimension(0,25)));
        panel.add(buttonPanel);
        
        contentPane.add(panel);
        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(750,480);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
        File dir = new File(System.getProperty("user.home"), "Videos");
        dir.mkdirs();
        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String fileName = dir.getAbsolutePath() + "/Capture-" + df.format(new Date()) + ".mpg";
        
        System.out.println(fileName);
        
        //fileName = "C:/test.mpg";
        
        mPlayer1.playMedia(mediaPath1,":sout=#transcode{vcodec=mpgv,vb=4094,scale=1,acodec=mpg,ab=128,channels=2,samplerate=44100}:duplicate{dst=file{dst=" + fileName + "},dst=display}");
        mPlayer2.playMedia(mediaPath2);
        mPlayer3.playMedia(mediaPath3);
        mPlayer4.playMedia(mediaPath4);
        
        try {
            Thread.sleep(10000);
            mPlayer2.playMedia(mediaPath3);
            mPlayer3.playMedia(mediaPath2);
        }catch(Exception e) {
            System.out.println(e);
        }
        
        //mPlayer1.stop();
        //mPlayer1.release();
        
        
        //----------------------open directory------------------------
        /*try {
            Desktop.getDesktop().open(new File("C:\\"));
        } catch(Exception e) {
            System.out.println(e);
        }*/
    }
    
}
