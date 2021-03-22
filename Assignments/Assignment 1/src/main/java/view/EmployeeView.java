package view;

import model.Account;
import model.Bill;
import model.Client;
import model.builder.AccountBuilder;
import model.builder.ClientBuilder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;

/**
 * Might want to prepare some eyebleach after this
 */

public class EmployeeView extends JFrame {
    private JPanel clientViewPanel;
    private JPanel tableButtonPane;
    private JPanel accountViewPanel;

    private JLabel lbName;
    private JLabel lbIdCardNumber;
    private JLabel lbPersonalNumericalCode;
    private JLabel lbAddress;

    private JLabel lbAccountNumber;
    private JLabel lbAccountType;
    private JLabel lbAccountSum;
    private JLabel lbBillSum;

    private JTextField tfName;
    private JTextField tfIdCardNumber;
    private JTextField tfPersonalNumericalCode;
    private JTextField tfAddress;
    private JTextField tfBillSum;

    private JTextField tfAccountNumber;
    private JTextField tfAccountSum;
    private JComboBox<String> cbAccountTypes;

    private JButton btnOpenClientInsertDialog;
    private JButton btnOpenClientUpdateDialog;
    private JButton btnInsertClient;
    private JButton btnUpdateClient;
    private JButton btnDeleteClient;
    private JButton btnLogout;
    private JButton btnViewClientAccounts;
    private JButton btnOpenAccountInsertDialog;
    private JButton btnOpenAccountUpdateDialog;
    private JButton btnInsertAccount;
    private JButton btnUpdateAccount;
    private JButton btnDeleteAccount;
    private JButton btnProcessBill;
    private JButton btnShowAccountBills;

    private JButton btnTransfer;

    private JTable clientTable;
    private JTable accountTable;
    private JScrollPane clientTablePane;
    private JScrollPane accountTablePane;

    private JFrame accountWindow;
    private final JButton btnOpenTransferDialog;
    private JComboBox<String> cbAccountNumberFrom;
    private JComboBox<String> cbAccountNumbersTo;
    private JTextField tfTransferSum;

    public EmployeeView() throws HeadlessException {
        setSize(750, 500);
        setLocationRelativeTo(null);
        initializeFields();
        clientViewPanel.setLayout(new BoxLayout(clientViewPanel, Y_AXIS));
        setLayout(new BorderLayout());

        accountWindow = new JFrame();
        accountWindow.setLayout(new BoxLayout(accountWindow.getContentPane(), X_AXIS));
        accountWindow.add(accountViewPanel);
        accountWindow.setSize(1000, 300);
        accountWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);

        tableButtonPane.setLayout(new BoxLayout(tableButtonPane, X_AXIS));
        tableButtonPane.add(btnOpenClientInsertDialog);
        tableButtonPane.add(btnOpenClientUpdateDialog);
        tableButtonPane.add(btnDeleteClient);
        tableButtonPane.add(btnViewClientAccounts);
        btnOpenTransferDialog = new JButton("Transfer Money");
        tableButtonPane.add(btnOpenTransferDialog);

        clientViewPanel.add(clientTablePane);
        clientViewPanel.add(tableButtonPane);

        add(clientViewPanel, BorderLayout.CENTER);
        add(btnLogout, BorderLayout.PAGE_END);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initializeFields() {

        btnLogout = new JButton("Logout");
        clientViewPanel = new JPanel();
        accountViewPanel = new JPanel();
        clientTablePane = new JScrollPane(clientTable);
        tableButtonPane = new JPanel();

        btnOpenClientInsertDialog = new JButton("Add Client");
        btnOpenClientInsertDialog.addActionListener(e -> createClientFormPopUp(btnInsertClient));

        btnOpenClientUpdateDialog = new JButton("Update Client");
        btnOpenClientUpdateDialog.addActionListener(e -> createClientFormPopUp(btnUpdateClient));

        lbName = new JLabel("Name");
        lbPersonalNumericalCode = new JLabel("Personal Numerical Code");
        lbIdCardNumber = new JLabel("Identity Card Number");
        lbAddress = new JLabel("Address");
        lbAccountNumber = new JLabel("Account Number");
        lbAccountSum = new JLabel("Sum");
        lbAccountType = new JLabel("Account Type");
        lbBillSum = new JLabel("Bill Sum");

        tfName = new JTextField();
        tfIdCardNumber = new JTextField();
        tfPersonalNumericalCode = new JTextField();
        tfAddress = new JTextField();
        tfAccountNumber = new JTextField();
        tfAccountSum = new JTextField();
        cbAccountTypes = new JComboBox<String>(Account.AccountTypes.allTypes);
        tfBillSum = new JTextField();
        tfTransferSum = new JTextField();

        btnInsertClient = new JButton("Insert new client");
        btnUpdateClient = new JButton("Update client data");
        btnDeleteClient = new JButton("Delete client");
        btnViewClientAccounts = new JButton("Manage client accounts");
        btnShowAccountBills = new JButton("Show account bills");

        btnOpenAccountInsertDialog = new JButton("Create account for client");
        btnOpenAccountInsertDialog.addActionListener(e -> createAccountFormPopUp(btnInsertAccount));
        btnOpenAccountUpdateDialog = new JButton("Update account");
        btnOpenAccountUpdateDialog.addActionListener(e -> createAccountFormPopUp(btnUpdateAccount));

        btnInsertAccount = new JButton("Create account");
        btnDeleteAccount = new JButton("Delete account");
        btnUpdateAccount = new JButton("Update account");
        btnProcessBill = new JButton("Process bill");
        btnTransfer = new JButton("Transfer sum");
    }

    public void updateClientTable(java.util.List<Client> clients)
    {
        String[] columns = {"ID", "NAME", "ID CARD NUMBER", "PERSONAL NUMERICAL CODE", "ADDRESS"};
        String[][] data = clients.stream()
                .map(client -> new String[]{client.getId().toString(), client.getName(), client.getIdCardNumber(), client.getPersonalNumericalCode(), client.getAddress()})
                .toArray(String[][]::new); //collect result to array since that is what JTable wants

        clientTable = new JTable(data, columns);
        clientTable.setDefaultEditor(Object.class, null); //make table uneditable
        clientTablePane = new JScrollPane(clientTable);
        clientViewPanel.removeAll();
        clientViewPanel.add(clientTablePane);
        clientViewPanel.add(tableButtonPane);
        clientViewPanel.revalidate();
    }

    public void showBills(List<Bill> bills) {
        JFrame billWindow = new JFrame();
        billWindow.setSize(500, 300);
        String[] columns = {"ID", "SUM", "DATE", "ACCOUNT ID"};
        String[][] data = bills.stream()
                .map(bill -> new String[]{bill.getId().toString(), bill.getSum().toString(), bill.getDate().toString(), bill.getAccountId().toString()})
                .toArray(String[][]::new); //collect result to array since that is what JTable wants

        JTable billTable = new JTable(data, columns);
        JScrollPane pane = new JScrollPane(billTable);

        billWindow.add(pane);
        billWindow.setVisible(true);
    }

    public void updateAccountTable(List<Account> accounts)
    {
        String[] columns = {"ID", "IDENTIFICATION NUMBER", "CLIENT ID","TYPE" ,"AMOUNT", "CREATED"};

        String[][] data = accounts.stream()
                .map(account -> new String[]{account.getId().toString(), account.getNumber(), account.getClientId().toString(),  account.getType(), account.getMoney().toString(), account.getCreationDate().toString()})
                .toArray(String[][]::new); //collect result to array since that is what JTable wants

        accountTable = new JTable(data, columns);
        accountTable.setDefaultEditor(Object.class, null); //make table uneditable


        accountViewPanel.setLayout(new BoxLayout(accountViewPanel, Y_AXIS));

        accountTablePane = new JScrollPane(accountTable);
        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, X_AXIS));

        buttonPanel.add(btnOpenAccountInsertDialog);
        buttonPanel.add(btnOpenAccountUpdateDialog);
        buttonPanel.add(btnDeleteAccount);
        JButton showBillMenu = new JButton("Process Bill");
        showBillMenu.addActionListener(e -> createProcessBillPopUp());
        buttonPanel.add(showBillMenu);
        buttonPanel.add(btnShowAccountBills);

        accountViewPanel.removeAll();
        accountViewPanel.add(accountTablePane);
        accountViewPanel.add(buttonPanel);
        accountViewPanel.revalidate();
    }

    public void showTransferWindow(List<Account> accounts) {

        JFrame accountForm = new JFrame();
        accountForm.setSize(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, Y_AXIS));
        panel.setBorder(new EmptyBorder(20,20,20, 20));

        String[] accountNumbers =  accounts.stream().map(Account::getNumber).toArray(String[]::new);
        cbAccountNumberFrom = new JComboBox<String>(accountNumbers);
        cbAccountNumbersTo = new JComboBox<String>(accountNumbers);

        JLabel lbFromAccount = new JLabel("Account to transfer from");
        panel.add(lbFromAccount);
        panel.add(cbAccountNumberFrom);
        JLabel lbToAccount = new JLabel("Account to transfer to");
        panel.add(lbToAccount);
        panel.add(cbAccountNumbersTo);
        JLabel lbTransferSum = new JLabel("Sum to transfer");
        panel.add(lbTransferSum);
        panel.add(tfTransferSum);

        panel.add(btnTransfer);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> accountForm.dispatchEvent(new WindowEvent(accountForm, WindowEvent.WINDOW_CLOSING)));

        panel.add(cancelButton);
        accountForm.add(panel);

        accountForm.setVisible(true);

    }

    private void createClientFormPopUp(JButton actionButton)
    {
        JFrame clientForm = new JFrame();
        clientForm.setSize(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, Y_AXIS));
        panel.setBorder(new EmptyBorder(20,20,20, 20));

        panel.add(lbName);
        panel.add(tfName);
        panel.add(lbIdCardNumber);
        panel.add(tfIdCardNumber);
        panel.add(lbPersonalNumericalCode);
        panel.add(tfPersonalNumericalCode);
        panel.add(lbAddress);
        panel.add(tfAddress);
        panel.add(actionButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> clientForm.dispatchEvent(new WindowEvent(clientForm, WindowEvent.WINDOW_CLOSING)));

        panel.add(cancelButton);
        clientForm.add(panel);

        clientForm.setVisible(true);
    }

    private void createAccountFormPopUp(JButton actionButton)
    {
        JFrame accountForm = new JFrame();
        accountForm.setSize(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, Y_AXIS));
        panel.setBorder(new EmptyBorder(20,20,20, 20));

        panel.add(lbAccountNumber);
        panel.add(tfAccountNumber);
        panel.add(lbAccountSum);
        panel.add(tfAccountSum);
        panel.add(lbAccountType);
        panel.add(cbAccountTypes);

        panel.add(actionButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> accountForm.dispatchEvent(new WindowEvent(accountForm, WindowEvent.WINDOW_CLOSING)));

        panel.add(cancelButton);
        accountForm.add(panel);

        accountForm.setVisible(true);
    }

    public void showAccountWindow()
    {
        accountWindow.setVisible(true);
    }

    private void createProcessBillPopUp()
    {
        JFrame billForm = new JFrame();
        billForm.setSize(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, Y_AXIS));
        panel.setBorder(new EmptyBorder(20,20,20, 20));

        panel.add(lbBillSum);
        panel.add(tfBillSum);

        panel.add(btnProcessBill);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> billForm.dispatchEvent(new WindowEvent(billForm, WindowEvent.WINDOW_CLOSING)));

        panel.add(cancelButton);
        billForm.add(panel);

        billForm.setVisible(true);
    }


    public Client getSelectedClient() {
        int rowIndex = clientTable.getSelectedRow();
        if(rowIndex < 0)
            return null;

        Long id =  Long.parseLong((String) clientTable.getValueAt(rowIndex, 0));
        String name = (String) clientTable.getValueAt(rowIndex, 1);
        String idCard =  (String) clientTable.getValueAt(rowIndex, 2);
        String pnc =  (String) clientTable.getValueAt(rowIndex, 3);
        String address =  (String) clientTable.getValueAt(rowIndex, 4);

        return new ClientBuilder().setId(id).setName(name).setIdCardNumber(idCard).setPersonalNumericalCode(pnc).setAddress(address).build();
    }

    public Account getSelectedAccount() {
        int rowIndex = accountTable.getSelectedRow();
        if(rowIndex < 0)
            return null;

        Long id =  Long.parseLong((String) accountTable.getValueAt(rowIndex, 0));
        String number = (String) accountTable.getValueAt(rowIndex, 1);
        Long clientId =  Long.parseLong((String)accountTable.getValueAt(rowIndex, 2));
        String type =  (String) accountTable.getValueAt(rowIndex, 3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Double money = Double.parseDouble((String) accountTable.getValueAt(rowIndex, 4));

        Date creationDate;
        try {
            creationDate = simpleDateFormat.parse((String) accountTable.getValueAt(rowIndex, 5));
        } catch (ParseException e) {
            creationDate = new Date();
        }

        return new AccountBuilder()
                .setId(id)
                .setNumber(number)
                .setType(type)
                .setClientId(clientId)
                .setMoney(money)
                .setCreationDate(creationDate)
                .build();
    }

    public String getClientName()
    {
        return tfName.getText();
    }

    public String getIdCardNumber()
    {
        return tfIdCardNumber.getText();
    }

    public String getAccountNumber() {
        return tfAccountNumber.getText();
    }

    public String getAccountSum() {
        return tfAccountSum.getText();
    }

    public String getAccountType() {
        return (String)cbAccountTypes.getSelectedItem();
    }

    public String getBillSum() {
        return tfBillSum.getText();
    }

    public String getPersonalNumericalCode()
    {
        return tfPersonalNumericalCode.getText();
    }

    public String getAddress()
    {
        return tfAddress.getText();
    }

    public String getTransferFrom() { return (String) cbAccountNumberFrom.getSelectedItem(); }

    public String getTransferTo() { return (String) cbAccountNumbersTo.getSelectedItem(); }

    public String getTransferSum() { return  tfTransferSum.getText(); }

    public void setLogoutButtonListener(ActionListener actionListener) {
        btnLogout.addActionListener(actionListener);
    }

    public void setInsertClientButtonListener(ActionListener actionListener) {
        btnInsertClient.addActionListener(actionListener);
    }

    public void setDeleteClientButtonListener(ActionListener actionListener) {
        btnDeleteClient.addActionListener(actionListener);
    }

    public void setUpdateClientButtonListener(ActionListener actionListener) {
        btnUpdateClient.addActionListener(actionListener);
    }

    public void setViewAccountsButtonListener(ActionListener actionListener) {
        btnViewClientAccounts.addActionListener(actionListener);
    }

    public void setInsertAccountButtonListener (ActionListener actionListener) {
        btnInsertAccount.addActionListener(actionListener);
    }

    public void setUpdateAccountButtonListener (ActionListener actionListener) {
        btnUpdateAccount.addActionListener(actionListener);
    }

    public void setDeleteAccountButtonListener (ActionListener actionListener) {
        btnDeleteAccount.addActionListener(actionListener);
    }

    public void setProcessBillButtonListener(ActionListener actionListener) {
        btnProcessBill.addActionListener(actionListener);
    }

    public void setShowBillsButtonListener(ActionListener actionListener) {
        btnShowAccountBills.addActionListener(actionListener);
    }

    public void setShowTransferWindowListener(ActionListener actionListener) {
        btnOpenTransferDialog.addActionListener(actionListener);
    }

    public void setTransferActionListener(ActionListener actionListener) {
        btnTransfer.addActionListener(actionListener);
    }

}
