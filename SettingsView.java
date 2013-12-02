package securitySystem;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.*;

/**
 * After pressing the Settings button
 * @author Teddy
 */
public class SettingsView extends JFrame{
    

         private JLabel emailLabel, phoneLabel;
         private JTextField emailText, phoneText;
         private JButton saveButton, cancelButton;
         private UserInformation user;
         
         
    public SettingsView(UserInformation ui){
         //new JFrame("Home Security System 1.0");
        
         Container contentPane = this.getContentPane();
         contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
         JPanel emailPanel = new JPanel();
         JPanel phonePanel = new JPanel();
         JPanel buttonPanel = new JPanel();
         
         emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.X_AXIS));
         phonePanel.setLayout(new BoxLayout(phonePanel, BoxLayout.X_AXIS));
         buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
         
         user = ui;
         emailLabel = new JLabel("Email: ");
         emailText = new JTextField();
         emailText.setText(ui.getemail());
         phoneLabel = new JLabel("Phone: ");
         phoneText = new JTextField();
         phoneText.setText(ui.getphoneNumber());
         saveButton = new JButton("Save");saveButton.setToolTipText("Save");
         cancelButton = new JButton("Cancel");cancelButton.setToolTipText("Cancel");

         emailPanel.add(emailLabel);
         emailPanel.add(emailText);
         phonePanel.add(phoneLabel);
         phonePanel.add(phoneText);
         buttonPanel.add(saveButton);
         buttonPanel.add(cancelButton);
         
         contentPane.add(emailPanel, BorderLayout.NORTH);
         contentPane.add(phonePanel, BorderLayout.NORTH);
         contentPane.add(buttonPanel, BorderLayout.SOUTH);
         
        
         this.pack();
         this.setVisible(true);
         this.setLocationRelativeTo(null);
    }
    
    public void addListener(ServerController controller){
        saveButton.addActionListener(controller);
        cancelButton.addActionListener(controller);
    }
    
    public UserInformation getUser(){
        user.setemail(emailText.getText());
        user.setphoneNumber(phoneText.getText());
        return user;
    }
    
}