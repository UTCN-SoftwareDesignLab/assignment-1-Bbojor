package controller;

import database.Constants;
import launcher.ComponentFactory;
import model.Activity;
import model.User;
import model.validation.Notification;
import service.activity.ActivityService;
import service.user.AuthenticationService;
import service.user.UserService;
import view.AdminView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AdminController implements Controller {

    private final AdminView adminView;
    private final UserService userService;
    private final ActivityService activityService;
    private final AuthenticationService authenticationService;

    public AdminController(AdminView adminView, AuthenticationService authenticationService, UserService userService, ActivityService activityService) {
        this.adminView = adminView;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.activityService = activityService;
        adminView.setLogoutButtonListener(new AdminController.LogoutButtonListener());
        adminView.setInsertButtonListener(new InsertButtonListener());
        adminView.setDeleteButtonListener(new DeleteButtonListener());
        adminView.setUpdateButtonListener(new UpdateButtonListener());
        adminView.setAllActivityButtonListener(new AllActivityButtonListener());
        adminView.setEmployeeActivityButtonListener(new EmployeeActivityButtonListener());
    }

    @Override
    public void makeActive() {
        getUsers();
        this.adminView.setVisible(true);
    }

    @Override
    public void hide() {
        this.adminView.setVisible(false);
    }

    private void getUsers() {
        List<User> users = userService.findAll();
        List<User> employees = users.stream() //filter out non-employees
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getRole().equals(Constants.Roles.EMPLOYEE)))
                .collect(Collectors.toList());
        adminView.updateEmployeeTable(employees);
    }

    private class LogoutButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            authenticationService.logout();
            ComponentFactory.getInstance().getControllerSwitcher().switchController();
        }
    }

    private class InsertButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String username = adminView.getUsername();
            String password = adminView.getPassword();
            Notification<Boolean> registrationNotification = authenticationService.register(username, password, Collections.singletonList(Constants.Roles.EMPLOYEE));

            if (registrationNotification.hasErrors()) {
                JOptionPane.showMessageDialog(adminView.getContentPane(), registrationNotification.getFormattedErrors());
            }
            else {
                Boolean result = registrationNotification.getResult();
                if (!result) {
                    JOptionPane.showMessageDialog(adminView.getContentPane(), "Program encountered a database error!");
                }
                else
                    getUsers(); //re-fetch user list and update view
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            User userToDelete = adminView.getSelectedEmployee();
            int accept = JOptionPane.showConfirmDialog(adminView.getContentPane(), "Are you sure you want to delete user " + userToDelete.getUsername() + "?");

            if(accept != 0) //user cancelled
                return;

            Notification<Boolean> deleteNotification = userService.deleteUser(userToDelete);


            if (deleteNotification.hasErrors()) {
                JOptionPane.showMessageDialog(adminView.getContentPane(), deleteNotification.getFormattedErrors());
            }
            else {
                Boolean result = deleteNotification.getResult();
                if (!result) {
                    JOptionPane.showMessageDialog(adminView.getContentPane(), "Program encountered a database error!");
                } else {
                    getUsers();
                    JOptionPane.showMessageDialog(adminView.getContentPane(), "User successfully deleted.");
                }
            }
        }
    }

    private class UpdateButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            User userToUpdate = adminView.getSelectedEmployee();
            if(userToUpdate == null)
            {
                JOptionPane.showMessageDialog(adminView.getContentPane(), "No user selected!");
                return;
            }

            String username = adminView.getUsername();
            String password = adminView.getPassword();

            userToUpdate.setUsername(username);
            userToUpdate.setPassword(password);

            Notification<Boolean> registrationNotification = userService.updateUser(userToUpdate);

            if (registrationNotification.hasErrors()) {
                JOptionPane.showMessageDialog(adminView.getContentPane(), registrationNotification.getFormattedErrors());
            }
            else {
                Boolean result = registrationNotification.getResult();
                if (!result) {
                    JOptionPane.showMessageDialog(adminView.getContentPane(), "Program encountered a database error!");
                } else
                getUsers(); //re-fetch user list and update view
            }
        }
    }

    private class AllActivityButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            List<Activity> activities = activityService.getAll();
            adminView.createActivityWindow(activities);
        }
    }

    private class EmployeeActivityButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            User selectedUser = adminView.getSelectedEmployee();

            if(selectedUser == null)
            {
                JOptionPane.showMessageDialog(adminView.getContentPane(), "No user selected!");
                return;
            }

            List<Activity> activities = activityService.getUserActivity(selectedUser);
            adminView.createActivityWindow(activities);
        }
    }
}
