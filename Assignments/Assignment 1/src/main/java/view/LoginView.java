package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

import static javax.swing.BoxLayout.Y_AXIS;

/**
 * Created by Alex on 18/03/2017.
 */
public class LoginView extends JFrame {

    private JPanel loginPanel;
    private JLabel lbUsername;
    private JLabel lbPassword;
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JButton btnLogin;

    public LoginView() throws HeadlessException {
        setSize(300, 300);
        setLocationRelativeTo(null);
        initializeFields();
        loginPanel.setLayout(new BoxLayout(loginPanel, Y_AXIS));
        loginPanel.setBorder(new EmptyBorder(20,20,20, 20));
        loginPanel.add(lbUsername);
        loginPanel.add(tfUsername);
        loginPanel.add(lbPassword);
        loginPanel.add(tfPassword);
        loginPanel.add(btnLogin);
        add(loginPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initializeFields() {
        tfUsername = new JTextField();
        tfPassword = new JPasswordField();
        lbUsername = new JLabel("Username:");
        lbPassword = new JLabel("Password:");
        btnLogin = new JButton("Login");
        loginPanel = new JPanel();

    }

    public String getUsername() {
        return tfUsername.getText();
    }

    public String getPassword() { return new String(tfPassword.getPassword()); }

    public void setLoginButtonListener(ActionListener loginButtonListener) {
        btnLogin.addActionListener(loginButtonListener);
    }

}
