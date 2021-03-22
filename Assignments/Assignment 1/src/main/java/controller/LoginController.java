package controller;


import launcher.ComponentFactory;
import model.User;
import model.validation.Notification;
import security.SecurityContext;
import service.user.AuthenticationService;
import view.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Alex on 18/03/2017.
 */
public class LoginController implements Controller {
    private final LoginView loginView;
    private final AuthenticationService authenticationService;

    public LoginController(LoginView loginView, AuthenticationService authenticationService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        loginView.setLoginButtonListener(new LoginButtonListener());
    }

    @Override
    public void makeActive() {
        loginView.setVisible(true);
    }

    @Override
    public void hide() {
        loginView.setVisible(false);
    }

    private class LoginButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasErrors()) {
                JOptionPane.showMessageDialog(loginView.getContentPane(), loginNotification.getFormattedErrors());
            } else {
                SecurityContext.setCurrentUser(loginNotification.getResult());
                ComponentFactory.getInstance().getControllerSwitcher().switchController();
            }
        }
    }

}
