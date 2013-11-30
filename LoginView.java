package securitySystem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.*;

/**
 *
 * @author Teddy
 */
public class LoginView extends JFrame
{
         private JLabel usernameLabel, passwordLabel;
         private JTextField usernameText, passwordText;
         private JButton loginButton, cancelButton;
         private UserInformation user;
         
         
    public LoginView(){
         //new JFrame("Home Security System 1.0");
         Container contentPane = this.getContentPane();
         contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
         JPanel usernamePanel = new JPanel();
         JPanel passwordPanel = new JPanel();
         JPanel buttonPanel = new JPanel();
         
         usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
         passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
         buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
         
         usernameLabel = new JLabel("Username: ");
         usernameText = new JTextField();
         passwordLabel = new JLabel("Password: ");
         passwordText = new JPasswordField();
         loginButton = new JButton("Login");loginButton.setToolTipText("Login");
         cancelButton = new JButton("Cancel");cancelButton.setToolTipText("Cancel");

         usernamePanel.add(usernameLabel);
         usernamePanel.add(usernameText);
         passwordPanel.add(passwordLabel);
         passwordPanel.add(passwordText);
         buttonPanel.add(loginButton);
         buttonPanel.add(cancelButton);
         
         contentPane.add(usernamePanel, BorderLayout.NORTH);
         contentPane.add(passwordPanel, BorderLayout.NORTH);
         contentPane.add(buttonPanel, BorderLayout.SOUTH);
         
         
         this.pack();
         this.setVisible(true);
         
    }
    
    public void addListener(ServerController controller){
        loginButton.addActionListener(controller);
        cancelButton.addActionListener(controller);
    }
    
    public UserInformation getUser(){
        user = new UserInformation();
        user.setemail(usernameText.getText());
        user.setpassword(passwordText.getText());
        return user;
    }
}
