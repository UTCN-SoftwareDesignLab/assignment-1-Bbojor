package view;

import model.Activity;
import model.User;
import model.builder.UserBuilder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;

public class AdminView extends JFrame {

    private JPanel centerPanel;
    private JPanel tableButtonPane;

    private JLabel lbUsername;
    private JLabel lbPassword;
    private JTextField tfUsername;
    private JTextField tfPassword;

    private JButton btnOpenEmployeeInsertDialog;
    private JButton btnOpenEmployeeUpdateDialog;
    private JButton btnInsertEmployee;
    private JButton btnUpdateEmployee;
    private JButton btnDeleteEmployee;
    private JButton btnLogout;
    private JButton btnViewAllActivity;
    private JButton btnViewEmployeeActivity;

    private JTable employeeTable;
    private JScrollPane tbPane;

    public AdminView() throws HeadlessException {
        setSize(750, 500);
        setLocationRelativeTo(null);
        initializeFields();
        centerPanel.setLayout(new BoxLayout(centerPanel, Y_AXIS));
        setLayout(new BorderLayout());

        tableButtonPane.setLayout(new BoxLayout(tableButtonPane, X_AXIS));
        tableButtonPane.add(btnOpenEmployeeInsertDialog);
        tableButtonPane.add(btnOpenEmployeeUpdateDialog);
        tableButtonPane.add(btnDeleteEmployee);
        tableButtonPane.add(btnViewAllActivity);
        tableButtonPane.add(btnViewEmployeeActivity);

        centerPanel.add(tbPane);
        centerPanel.add(tableButtonPane);

        add(centerPanel, BorderLayout.CENTER);
        add(btnLogout, BorderLayout.PAGE_END);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initializeFields() {

        btnLogout = new JButton("Logout");
        centerPanel = new JPanel();
        tbPane = new JScrollPane(employeeTable);
        tableButtonPane = new JPanel();

        btnOpenEmployeeInsertDialog = new JButton("Add Employee");
        btnOpenEmployeeInsertDialog.addActionListener(e -> createUserFormPopUp(btnInsertEmployee));

        btnOpenEmployeeUpdateDialog = new JButton("Update Employee");
        btnOpenEmployeeUpdateDialog.addActionListener(e -> createUserFormPopUp(btnUpdateEmployee));

        lbUsername = new JLabel("Username");
        lbPassword = new JLabel("Password");
        tfUsername = new JTextField();
        tfPassword = new JTextField();

        btnInsertEmployee = new JButton("Insert new employee");
        btnUpdateEmployee = new JButton("Update employee data");
        btnDeleteEmployee = new JButton("Delete employee");
        btnViewAllActivity = new JButton("View all activity");
        btnViewEmployeeActivity = new JButton("View user activity");
    }

    public void updateEmployeeTable(List<User> users)
    {
        String[] columns = {"ID", "USERNAME"};
        String[][] data = users.stream()
                .map(user -> new String[]{user.getId().toString(), user.getUsername()}) //display only the id and username
                .toArray(String[][]::new); //collect result to array since that is what JTable wants

        employeeTable = new JTable(data, columns);
        employeeTable.setDefaultEditor(Object.class, null); //make table uneditable
        tbPane = new JScrollPane(employeeTable);
        centerPanel.removeAll();
        centerPanel.add(tbPane);
        centerPanel.add(tableButtonPane);
        centerPanel.revalidate();
    }

    private void createUserFormPopUp(JButton actionButton)
    {
        JFrame userForm = new JFrame();
        userForm.setSize(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, Y_AXIS));
        panel.setBorder(new EmptyBorder(20,20,20, 20));

        panel.add(lbUsername);
        panel.add(tfUsername);
        panel.add(lbPassword);
        panel.add(tfPassword);
        panel.add(actionButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> userForm.dispatchEvent(new WindowEvent(userForm, WindowEvent.WINDOW_CLOSING)));

        panel.add(cancelButton);
        userForm.add(panel);

        userForm.setVisible(true);
    }

    public void createActivityWindow(List<Activity> activities)
    {
        String[] columns = {"ID", "USER", "DATE", "TYPE" ,"ARGUMENTS"};

        String[][] data = activities.stream()
                .map(activity -> new String[]{activity.getId().toString(), activity.getUser().toString(), activity.getDate().toString(), activity.getType(), activity.getArguments()}) //display only the id and username
                .toArray(String[][]::new); //collect result to array since that is what JTable wants

        JTable activityTable = new JTable(data, columns);
        activityTable.setDefaultEditor(Object.class, null); //make table uneditable
        JScrollPane tablePane = new JScrollPane(activityTable);

        JFrame activityFrame = new JFrame();
        activityFrame.setLayout(new BoxLayout(activityFrame.getContentPane(), X_AXIS));
        activityFrame.add(tablePane);
        activityFrame.setSize(750, 300);
        activityFrame.setVisible(true);
    }

    public User getSelectedEmployee() {
        int rowIndex = employeeTable.getSelectedRow();
        if(rowIndex < 0)
            return null;

        Long id =  Long.parseLong((String) employeeTable.getValueAt(rowIndex, 0));
        String username = (String) employeeTable.getValueAt(rowIndex, 1);

        return new UserBuilder().setId(id).setUsername(username).build();
    }

    public String getUsername()
    {
        return tfUsername.getText();
    }

    public String getPassword()
    {
        return tfPassword.getText();
    }
    
    public void setLogoutButtonListener(ActionListener logoutButtonListener) {
        btnLogout.addActionListener(logoutButtonListener);
    }

    public void setInsertButtonListener(ActionListener logoutButtonListener) {
        btnInsertEmployee.addActionListener(logoutButtonListener);
    }

    public void setDeleteButtonListener(ActionListener logoutButtonListener) {
        btnDeleteEmployee.addActionListener(logoutButtonListener);
    }

    public void setUpdateButtonListener(ActionListener logoutButtonListener) {
        btnUpdateEmployee.addActionListener(logoutButtonListener);
    }

    public void setAllActivityButtonListener(ActionListener logoutButtonListener) {
        btnViewAllActivity.addActionListener(logoutButtonListener);
    }

    public void setEmployeeActivityButtonListener(ActionListener logoutButtonListener) {
        btnViewEmployeeActivity.addActionListener(logoutButtonListener);
    }

}