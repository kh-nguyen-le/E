/**
 * Write a description of class View here.
 * 
 * @author Yuan Sun
 * @version v1.0
 * 
 * issues:
 * 1. have to activate the cam before running the program otherwise it wont show, can make a separate thread for that.
 * 2. for updateMedia, do we need the swithCount and recordCount to determine which media path to play?
 * 3. checkBlank still has problems.
 * 4. setMediaPath, can they using switch instead of making one for each media path?
 */
package securitySystem;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.*;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class ServerView
{
    private final EmbeddedMediaPlayer mPlayer1,mPlayer2,mPlayer3,mPlayer4,hiddenMPlayer;
    private final JButton leftButton,rightButton,zoomInButton,zoomOutButton,recordButton,screenCapture,switchCam,alarmToggle,settings,storage,help;
    private final JMenuItem left,right,zoomIn,zoomOut,capture,record,close,video,pic,setting,update,doc,about,simulateIntrusion;
    private final File dir,picDir,videoDir;
    
    private final String vlcPath = "C:\\Program Files\\VideoLAN\\VLC";
    private String mediaPath1 = "blank.bmp";
    private String mediaPath2 = "blank.bmp";//"1.mp4";
    private String mediaPath3 = "blank.bmp";//"2.mp4";
    private String mediaPath4 = "blank.bmp";//"3.mp4";
    private String fileName;
    private int switchCount = 0; //Tracks how many times switch was press; 4 unique positions: wraps to 0 after 3
    private int recordCount = 0; //recording main camera on/off
    private UserInformation user;
    
    public ServerView()
    {
        
        JFrame frame = new JFrame("Home Security System 1.0");
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
        JMenu helpMenu = new JMenu("Help");
        menu.add(helpMenu);
        //Setting up the menu items in control bar
        left = new JMenuItem("Toggle Left");
        control.add(left);
        right = new JMenuItem("Toggle Right");
        control.add(right);
        zoomIn = new JMenuItem("Zoom In");
        control.add(zoomIn);
        zoomOut = new JMenuItem("Zoom Out");
        control.add(zoomOut);
        capture = new JMenuItem("Screen Capture");
        control.add(capture);
        record = new JMenuItem("Record Video");
        control.add(record);
        close = new JMenuItem("Close");
        control.add(close);
        //Setting up the menu items in history bar
        video = new JMenuItem("Video Recorded");
        history.add(video);
        pic = new JMenuItem("Picture Captured");
        history.add(pic);
        //Setting up the menu items in admin bar
        setting = new JMenuItem("Settings");
        admin.add(setting);
        //Setting up the menu items in help bar
        update = new JMenuItem("Update");
        helpMenu.add(update);
        doc = new JMenuItem("Help");
        helpMenu.add(doc);
        about = new JMenuItem("About");
        helpMenu.add(about);
        //add functionality to simulate intrusion in help menu
        simulateIntrusion = new JMenuItem("Simulate Intrusion");
        helpMenu.add(simulateIntrusion);
        
        //------------------------------------------------------------
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcPath);
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

        Canvas canvas1 = new Canvas();
        canvas1.setPreferredSize(new Dimension(450, 300));
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas1);
        mPlayer1 = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mPlayer1.setVideoSurface(videoSurface);

        Canvas canvas2 = new Canvas();
        canvas2.setPreferredSize(new Dimension(147, 100));
        videoSurface = mediaPlayerFactory.newVideoSurface(canvas2);
        mPlayer2 = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mPlayer2.setVideoSurface(videoSurface);
        
        Canvas canvas3 = new Canvas();
        canvas3.setPreferredSize(new Dimension(146, 100));
        videoSurface = mediaPlayerFactory.newVideoSurface(canvas3);
        mPlayer3 = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mPlayer3.setVideoSurface(videoSurface);
        
        Canvas canvas4 = new Canvas();
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
        
        //------------------hidden vlc players---------
        Canvas hiddenCanvas = new Canvas();
        hiddenCanvas.setPreferredSize(new Dimension(450, 300));
        videoSurface = mediaPlayerFactory.newVideoSurface(hiddenCanvas);
        hiddenMPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        hiddenMPlayer.setVideoSurface(videoSurface);
        
        JPanel hiddenPanel = new JPanel();
        hiddenPanel.add(hiddenCanvas);
        hiddenPanel.setVisible(false);
        contentPane.add(hiddenPanel);
        
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
        c.gridx = 0;
        c.gridy = 1;
        controlPanel.add(leftButton,c);
        rightButton = new JButton(new ImageIcon("right.png"));
        rightButton.setToolTipText("Toggle Right");
        rightButton.setPreferredSize(new Dimension(50,50));
        c.gridx = 2;
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
        
        dir = new File(System.getProperty("user.home"), "Storage");
        dir.mkdir();
        videoDir = new File(dir, "Video Recorded");
        videoDir.mkdir();
        picDir = new File(dir, "Screen Captured");
        picDir.mkdir();
        
        hiddenMPlayer.playMedia(mediaPath1);
        mPlayer1.playMedia(mediaPath1);
        mPlayer2.playMedia(mediaPath2);
        mPlayer3.playMedia(mediaPath3);
        mPlayer4.playMedia(mediaPath4);
    }

    //Returns the user information
    public UserInformation getInfo(){
        return user;
    }
    
    public void setUser(UserInformation ui){
        user = ui;
    }
 
    //Store the IP for the camera connected, would eventually be assigned
    //to a media player.     
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
        //buttons
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
        //menu items
        left.addActionListener(controller);
        right.addActionListener(controller);
        zoomIn.addActionListener(controller);
        zoomOut.addActionListener(controller);
        capture.addActionListener(controller);
        record.addActionListener(controller);
        close.addActionListener(controller);
        video.addActionListener(controller);
        pic.addActionListener(controller);
        setting.addActionListener(controller);
        update.addActionListener(controller);
        doc.addActionListener(controller);
        about.addActionListener(controller);
        simulateIntrusion.addActionListener(controller);
    }
    
    //In the case of any changes in the media path, the media player is 
    //updated
    public void updateMedia(){
    	recordCount = 0;
    	switch(switchCount) {
                case 0:
                    hiddenMPlayer.playMedia(mediaPath1);
                    mPlayer1.playMedia(mediaPath1);
                    mPlayer2.playMedia(mediaPath2);
                    mPlayer3.playMedia(mediaPath3);
                    mPlayer4.playMedia(mediaPath4);
                    break;
                case 1:
                    hiddenMPlayer.playMedia(mediaPath4);
                    mPlayer1.playMedia(mediaPath4);
                    mPlayer2.playMedia(mediaPath1);
                    mPlayer3.playMedia(mediaPath2);
                    mPlayer4.playMedia(mediaPath3);
                    break;
                case 2: 
                    hiddenMPlayer.playMedia(mediaPath3);
                    mPlayer1.playMedia(mediaPath3);
                    mPlayer2.playMedia(mediaPath4);
                    mPlayer3.playMedia(mediaPath1);
                    mPlayer4.playMedia(mediaPath2);
                    break;
                case 3:
                    hiddenMPlayer.playMedia(mediaPath2);
                    mPlayer1.playMedia(mediaPath2);
                    mPlayer2.playMedia(mediaPath3);
                    mPlayer3.playMedia(mediaPath4);
                    mPlayer4.playMedia(mediaPath1);
                    break;
                default:
                	break;
    	}
    }
    
    //This checks the status of the media player, i.e. whether it is playing or not
    public void checkBlank()
    {
        if(!mPlayer1.isPlaying()) {
            setBlank(1);
            mPlayer1.playMedia(mediaPath1);
        }
        if(!mPlayer2.isPlaying()) {
            setBlank(2);
            mPlayer2.playMedia(mediaPath2);
        }
        if(!mPlayer1.isPlaying()) {
            setBlank(3);
            mPlayer3.playMedia(mediaPath3);
        }
        if(!mPlayer4.isPlaying()) {
            setBlank(4);
            mPlayer4.playMedia(mediaPath4);
        }
    }
    
    //Stops the defined media player(int number) from displaying video stream 
    //and blanks the screen
    public void setBlank(int number){
        switch(number){
            case 1:
                mediaPath1 = "blank.bmp"; 
                break;
            case 2:
                mediaPath2 = "blank.bmp";
                break;
            case 3:
                mediaPath3 = "blank.bmp";
                break;
            case 4:
                mediaPath4 = "blank.bmp";
                break;
                    
        }
    }
          
    //Records the video stream in the main player
    public void record(){
        generateFileName(false);//to generate video path
        if(recordCount == 0) {
            switch(switchCount){
            	case 0:
                    mPlayer1.playMedia(mediaPath1,":sout=#transcode{vcodec=mpgv,vb=4094,scale=1,acodec=mpg,ab=128,channels=2,samplerate=44100}:duplicate{dst=file{dst=" + fileName + "},dst=display}");
                    hiddenMPlayer.playMedia(mediaPath1);
                    recordCount = 1;
                    break;			
            	case 1:
                    mPlayer1.playMedia(mediaPath4,":sout=#transcode{vcodec=mpgv,vb=4094,scale=1,acodec=mpg,ab=128,channels=2,samplerate=44100}:duplicate{dst=file{dst=" + fileName + "},dst=display}");
                    hiddenMPlayer.playMedia(mediaPath4);
                    recordCount = 1;
                    break;				
            	case 2:
                    mPlayer1.playMedia(mediaPath3,":sout=#transcode{vcodec=mpgv,vb=4094,scale=1,acodec=mpg,ab=128,channels=2,samplerate=44100}:duplicate{dst=file{dst=" + fileName + "},dst=display}");
                    hiddenMPlayer.playMedia(mediaPath3);
                    recordCount = 1;
                    break;				
            	case 3:
                    mPlayer1.playMedia(mediaPath2,":sout=#transcode{vcodec=mpgv,vb=4094,scale=1,acodec=mpg,ab=128,channels=2,samplerate=44100}:duplicate{dst=file{dst=" + fileName + "},dst=display}");
                    hiddenMPlayer.playMedia(mediaPath2);
                    recordCount = 1;
                    break;		
            	default:
                    break;
            }			
        }else {
        		switch(switchCount){
                	case 0:
                		mPlayer1.playMedia(mediaPath1);
                		hiddenMPlayer.playMedia(mediaPath1);
                		recordCount = 0;
                		break;
                	case 1:
                		mPlayer1.playMedia(mediaPath4);
                		hiddenMPlayer.playMedia(mediaPath4);
                		recordCount = 0;
                		break;
                	case 2:
                		mPlayer1.playMedia(mediaPath3);
                		hiddenMPlayer.playMedia(mediaPath3);
                		recordCount = 0;
                		break;
                	case 3:
                		mPlayer1.playMedia(mediaPath2);
                		hiddenMPlayer.playMedia(mediaPath2);
                		recordCount = 0;
                		break;
                	default:
                		break;
        		}
        }
    }
    
    //Rotates camera position counterclockwise
    public void switchCam(){
        recordCount = 0;
        switch(switchCount) {
            case 0:
                hiddenMPlayer.playMedia(mediaPath4);
                mPlayer1.playMedia(mediaPath4);
                mPlayer2.playMedia(mediaPath1);
                mPlayer3.playMedia(mediaPath2);
                mPlayer4.playMedia(mediaPath3);
                switchCount = 1;
                break;
            case 1: 
                hiddenMPlayer.playMedia(mediaPath3);
                mPlayer1.playMedia(mediaPath3);
                mPlayer2.playMedia(mediaPath4);
                mPlayer3.playMedia(mediaPath1);
                mPlayer4.playMedia(mediaPath2);
                switchCount = 2;
                break;
            case 2:
                hiddenMPlayer.playMedia(mediaPath2);
                mPlayer1.playMedia(mediaPath2);
                mPlayer2.playMedia(mediaPath3);
                mPlayer3.playMedia(mediaPath4);
                mPlayer4.playMedia(mediaPath1);
                switchCount = 3;
                break;
            case 3:
                hiddenMPlayer.playMedia(mediaPath1);
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
    
    
    public void generateFileName(boolean snap)
    {
        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        if(!snap)
            fileName = videoDir.getAbsolutePath() + "/Capture-" + df.format(new Date()) + ".mpg";
        else
           fileName =  picDir.getAbsolutePath()+"/Snapshot-"+df.format(new Date())+".png";
       
    }
    
    //Takes a snapshot image of the main camera. 
    public void snapshot()
    {
        System.out.println("Taking snapshot");
        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        if(recordCount == 0) {
            mPlayer1.getSnapshot();
            mPlayer1.saveSnapshot(new File(getSnapshotStoragePath()));
        }
        else {
            hiddenMPlayer.getSnapshot();
            hiddenMPlayer.saveSnapshot(new File(getSnapshotStoragePath()));
        }
    }
    
    public void openDir(int choice)
    {
        File temp = dir;
        switch(choice) {
            case 0:
                temp = this.dir;
                break;
            case 1:
                temp = picDir;
                break;
            case 2:
                temp = videoDir;
                break;
            default:
                break;
        }
        try {
            Desktop.getDesktop().open(temp);
        } catch(IOException e) {
            System.out.println(e);
        }
    }
    
    //Open README.pdf which shows the instructions for running the program
    public void openHelp()
    {        
        try {
            File myFile = new File("Readme.pdf");
            Desktop.getDesktop().open(myFile);
        }catch(IOException e) {
            System.out.println(e);
        }
    }
    
    public String getSnapshotStoragePath(){
        generateFileName(true);//to generate snapshot image path
        return fileName;
    }
}
