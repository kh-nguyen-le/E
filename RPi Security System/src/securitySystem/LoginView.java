package securitySystem;

import java.awt.*;
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
        //JFrame frame = new JFrame("Welcome to Login Page");
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        JPanel usernamePanel = new JPanel();
        JPanel passwordPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        usernamePanel.setLayout(new FlowLayout());
        passwordPanel.setLayout(new FlowLayout());
        buttonPanel.setLayout(new FlowLayout());

        usernameLabel = new JLabel("Username:");
        usernameText = new JTextField(10);
        usernameText.setFont(new Font("Serif", Font.PLAIN, 20));
        passwordLabel = new JLabel("Password:");
        passwordText = new JPasswordField(10);
        passwordText.setFont(new Font("Serif", Font.PLAIN, 20));
        loginButton = new JButton("Login");
        loginButton.setToolTipText("Login");
        cancelButton = new JButton("Cancel");
        cancelButton.setToolTipText("Cancel");

        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameText);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordText);
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        contentPane.add(usernamePanel);
        contentPane.add(passwordPanel);
        contentPane.add(buttonPanel);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(280,150);
        this.setResizable(false);
        this.getRootPane().setDefaultButton(loginButton);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    
    public void addListener(ServerController controller){
        loginButton.addActionListener(controller);
        cancelButton.addActionListener(controller);
    }
    
    
    public UserInformation getUser(){
        user = new UserInformation();
        user.setname(usernameText.getText());
        user.setpassword(passwordText.getText());
        return user;
    }
}
