import javax.swing.Icon;
import javax.swing.*;

class Controller implements java.awt.event.ActionListener
{
    View view;
    
    public void actionPerformed(java.awt.event.ActionEvent e)
    {
        Object o = e.getSource();
        
        if(o instanceof JButton) {
            JButton button = (JButton)o;
            if(button.getActionCommand().equals("1")) {
                System.out.println("left");
            }
            else if(button.getActionCommand().equals("2")) {
                System.out.println("right");
            }
            else if(button.getActionCommand().equals("3")) {
                System.out.println("zoomin");
            }
            else if(button.getActionCommand().equals("4")) {
                System.out.println("zoomout");
            }
            else if(button.getActionCommand().equals("5")) {
                System.out.println("record");
            }
            else if(button.getActionCommand().equals("Screen Capture")) {
                System.out.println("Screen Capture");
            }
            else if(button.getActionCommand().equals("Switch Camera")) {
                view.switchCam();
            }
            else if(button.getActionCommand().equals("Alarm On")) {
                System.out.println("on");
            }
            else if(button.getActionCommand().equals("Alarm Off")) {
                System.out.println("off");
            }
            else if(button.getActionCommand().equals("Add Camera")) {
                System.out.println("add");
            }
            else if(button.getActionCommand().equals("Remove Camera")) {
                System.out.println("remove");
            } 
        }
	}
	
	public void addView(View view){
		this.view = view;
	}
}
