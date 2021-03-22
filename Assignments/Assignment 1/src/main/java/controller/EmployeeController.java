package controller;

import launcher.ComponentFactory;
import model.Account;
import model.Activity;
import model.Bill;
import model.Client;
import model.builder.AccountBuilder;
import model.builder.ActivityBuilder;
import model.builder.BillBuilder;
import model.builder.ClientBuilder;
import model.validation.Notification;
import security.SecurityContext;
import service.account.AccountService;
import service.activity.ActivityService;
import service.bill.BillService;
import service.client.ClientService;
import service.user.AuthenticationService;
import view.EmployeeView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

public class EmployeeController implements Controller{

    private final EmployeeView employeeView;
    private final ClientService clientService;
    private final AccountService accountService;
    private final BillService billService;
    private final AuthenticationService authenticationService;
    private final ActivityService activityService;

    public EmployeeController(EmployeeView employeeView, ClientService clientService, AccountService accountService, BillService billService, AuthenticationService authenticationService, ActivityService activityService) {
        this.employeeView = employeeView;
        this.clientService = clientService;
        this.accountService = accountService;
        this.billService = billService;
        this.authenticationService = authenticationService;
        this.activityService = activityService;

        employeeView.setLogoutButtonListener(new LogoutButtonListener());
        employeeView.setInsertClientButtonListener(new InsertClientButtonListener());
        employeeView.setUpdateClientButtonListener(new UpdateClientButtonListener());
        employeeView.setDeleteClientButtonListener(new DeleteClientButtonListener());
        employeeView.setViewAccountsButtonListener(new OpenAccountViewButtonListener());
        employeeView.setInsertAccountButtonListener(new InsertAccountButtonListener());
        employeeView.setUpdateAccountButtonListener(new UpdateAccountButtonListener());
        employeeView.setDeleteAccountButtonListener(new DeleteAccountButtonListener());
        employeeView.setProcessBillButtonListener(new ProcessBillButtonListener());
        employeeView.setShowBillsButtonListener(new ShowBillsButtonLister());
        employeeView.setShowTransferWindowListener(new ShowTransferDialogButtonListener());
        employeeView.setTransferActionListener(new TransferButtonListener());
    }

    @Override
    public void makeActive() {
        getClients();
        employeeView.setVisible(true);
    }

    private void getClients() {
        List<Client> clients = clientService.getAll();
        employeeView.updateClientTable(clients);
    }

    private boolean getAccounts() {
        Client client = employeeView.getSelectedClient();
        if(client == null) {
            JOptionPane.showMessageDialog(employeeView.getContentPane(), "No client selected!");
            return false;
        }
        else
        {
            List<Account> accounts = accountService.getClientAccounts(client);
            employeeView.updateAccountTable(accounts);
        }
        return true;
    }


    @Override
    public void hide() {
        employeeView.setVisible(false);
    }

    private class LogoutButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            authenticationService.logout();
            ComponentFactory.getInstance().getControllerSwitcher().switchController();
        }
    }

    private class InsertClientButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = employeeView.getClientName();
            String personalNumericalCode = employeeView.getPersonalNumericalCode();
            String idCardNumber = employeeView.getIdCardNumber();
            String address = employeeView.getAddress();

            Client clientToAdd = new ClientBuilder()
                    .setName(name)
                    .setIdCardNumber(idCardNumber)
                    .setPersonalNumericalCode(personalNumericalCode)
                    .setAddress(address)
                    .build();

            Notification<Boolean> insertNotification =  clientService.addClient(clientToAdd);

            if(insertNotification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), insertNotification.getFormattedErrors());
            }
            else {
                Boolean result = insertNotification.getResult();
                if(!result) {
                    JOptionPane.showMessageDialog(employeeView.getContentPane(), "Program encountered a database error!");
                }
                else {
                    getClients();
                    Activity clientInsertion = new ActivityBuilder()
                        .setType(Activity.ActivityTypes.ADD_CLIENT)
                        .setUser(SecurityContext.getCurrentUser().getUsername())
                        .setDate(new Date())
                        .setArguments(clientToAdd.toString())
                        .build();

                    activityService.addActivity(clientInsertion);
                }
            }
        }
    }

    private class UpdateClientButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            Client updatedClient = employeeView.getSelectedClient();
            if(updatedClient == null)
            {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), "No client selected!");
                return;
            }

            String name = employeeView.getClientName();
            String personalNumericalCode = employeeView.getPersonalNumericalCode();
            String idCardNumber = employeeView.getIdCardNumber();
            String address = employeeView.getAddress();

            updatedClient.setName(name);
            updatedClient.setIdCardNumber(idCardNumber);
            updatedClient.setPersonalNumericalCode(personalNumericalCode);
            updatedClient.setAddress(address);

            Notification<Boolean> updateNotification =  clientService.updateClient(updatedClient);

            if(updateNotification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), updateNotification.getFormattedErrors());
            }
            else {
                Boolean result = updateNotification.getResult();
                if(!result) {
                    JOptionPane.showMessageDialog(employeeView.getContentPane(), "Program encountered a database error!");
                }
                else {
                    getClients();
                    Activity clientUpdate = new ActivityBuilder()
                            .setType(Activity.ActivityTypes.UPDATE_CLIENT)
                            .setUser(SecurityContext.getCurrentUser().getUsername())
                            .setDate(new Date())
                            .setArguments(updatedClient.toString())
                            .build();

                    activityService.addActivity(clientUpdate);
                }
            }
        }
    }

    private class DeleteClientButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            Client clientToDelete = employeeView.getSelectedClient();

            if(clientToDelete == null)
            {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), "No client selected!");
                return;
            }

            int accept = JOptionPane.showConfirmDialog(employeeView.getContentPane(), "Are you sure you want to delete client " + clientToDelete.getName() + "?");

            if(accept != 0) //user cancelled
                return;

            Notification<Boolean> deleteNotification = clientService.deleteClient(clientToDelete);

            if (deleteNotification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), deleteNotification.getFormattedErrors());
            }
            else {
                Boolean result = deleteNotification.getResult();
                if (!result) {
                    JOptionPane.showMessageDialog(employeeView.getContentPane(), "Program encountered a database error!");
                } else {
                    getClients();
                    JOptionPane.showMessageDialog(employeeView.getContentPane(), "Client successfully deleted.");

                    Activity clientDeletion = new ActivityBuilder()
                            .setType(Activity.ActivityTypes.DELETE_CLIENT)
                            .setUser(SecurityContext.getCurrentUser().getUsername())
                            .setDate(new Date())
                            .setArguments(clientToDelete.toString())
                            .build();

                    activityService.addActivity(clientDeletion);
                }
            }
        }

    }

    private class OpenAccountViewButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(getAccounts())
                employeeView.showAccountWindow();
        }
    }

    private class InsertAccountButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Client client = employeeView.getSelectedClient();
            if(client == null)
            {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), "No client selected!");
                return;
            }

            String accountNumber = employeeView.getAccountNumber();
            String sumString = employeeView.getAccountSum();
            Double sum;
            try {
                 sum = Double.parseDouble(sumString);
            }
            catch(NumberFormatException ex)
            {
                sum = (double) -1;
            }
            String type = employeeView.getAccountType();

            Account account = new AccountBuilder()
                    .setClientId(client.getId())
                    .setCreationDate(new Date())
                    .setNumber(accountNumber)
                    .setMoney(sum)
                    .setType(type)
                    .build();

            Notification<Boolean> insertNotification = accountService.createAccountForClient(client, account);
            if(insertNotification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), insertNotification.getFormattedErrors());
            }
            else {
                Boolean result = insertNotification.getResult();
                if(!result) {
                    JOptionPane.showMessageDialog(employeeView.getContentPane(), "The program encountered a database error!");
                }
                else {
                    getAccounts();
                    Activity accountCreation = new ActivityBuilder()
                            .setType(Activity.ActivityTypes.ADD_ACCOUNT)
                            .setUser(SecurityContext.getCurrentUser().getUsername())
                            .setDate(new Date())
                            .setArguments(account.toString())
                            .build();

                    activityService.addActivity(accountCreation);
                }
            }
        }
    }

    private class UpdateAccountButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            Account updatedAccount = employeeView.getSelectedAccount();
            if(updatedAccount == null)
            {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), "No account selected!");
                return;
            }

            String accountNumber = employeeView.getAccountNumber();
            String accountSumString = employeeView.getAccountSum();
            Double accountSum;
            try {
                accountSum = Double.parseDouble(accountSumString);
            }
            catch(NumberFormatException ex)
            {
                accountSum = (double) -1;
            }

            String accountType = employeeView.getAccountType();

            updatedAccount.setMoney(accountSum);
            updatedAccount.setNumber(accountNumber);
            updatedAccount.setType(accountType);

            Notification<Boolean> updateNotification =  accountService.updateAccount(updatedAccount);

            if(updateNotification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), updateNotification.getFormattedErrors());
            }
            else {
                Boolean result = updateNotification.getResult();
                if(!result) {
                    JOptionPane.showMessageDialog(employeeView.getContentPane(), "Program encountered a database error!");
                }
                else {
                    getAccounts();
                    Activity accountUpdate = new ActivityBuilder()
                            .setType(Activity.ActivityTypes.UPDATE_ACCOUNT)
                            .setUser(SecurityContext.getCurrentUser().getUsername())
                            .setDate(new Date())
                            .setArguments(updatedAccount.toString())
                            .build();

                    activityService.addActivity(accountUpdate);
                }
            }
        }
    }

    private class DeleteAccountButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            Account accountToDelete = employeeView.getSelectedAccount();

            if(accountToDelete == null)
            {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), "No account selected!");
                return;
            }

            int accept = JOptionPane.showConfirmDialog(employeeView.getContentPane(), "Are you sure you want to delete selected account user?");

            if(accept != 0) //user cancelled
                return;

            Notification<Boolean> deleteNotification = accountService.deleteAccount(accountToDelete);

            if (deleteNotification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), deleteNotification.getFormattedErrors());
            }
            else {
                Boolean result = deleteNotification.getResult();
                if (!result) {
                    JOptionPane.showMessageDialog(employeeView.getContentPane(), "Program encountered a database error!");
                } else {
                    getAccounts();
                    JOptionPane.showMessageDialog(employeeView.getContentPane(), "Account successfully deleted.");

                    Activity accountDeletion = new ActivityBuilder()
                            .setType(Activity.ActivityTypes.DELETE_ACCOUNT)
                            .setUser(SecurityContext.getCurrentUser().getUsername())
                            .setDate(new Date())
                            .setArguments(accountToDelete.toString())
                            .build();

                    activityService.addActivity(accountDeletion);
                }
            }
        }

    }

    private class ProcessBillButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Account account = employeeView.getSelectedAccount();
            if(account == null) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), "No account selected!");
                return;
            }

            String billSumString = employeeView.getBillSum();
            Double billSum;
            try{
                billSum = Double.parseDouble(billSumString);
            }
            catch(NumberFormatException ex) {
                billSum = (double)-1;
            }

            Bill bill = new BillBuilder()
                    .setDate(new Date())
                    .setAccountId(account.getId())
                    .setSum(billSum)
                    .build();

            Notification<Boolean> billNotification = billService.processBill(bill);

            if(billNotification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), billNotification.getFormattedErrors());
            }
            else {
                Boolean result = billNotification.getResult();
                if(!result) {
                    JOptionPane.showMessageDialog(employeeView.getContentPane(), "Program encountered a database error!");
                }
                else {
                    getAccounts();

                    Activity billProcessing = new ActivityBuilder()
                            .setType(Activity.ActivityTypes.PROCESS_BILL)
                            .setUser(SecurityContext.getCurrentUser().getUsername())
                            .setDate(new Date())
                            .setArguments(bill.toString())
                            .build();

                    activityService.addActivity(billProcessing);
                }
            }

        }

    }

    private class ShowBillsButtonLister implements  ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Account account = employeeView.getSelectedAccount();
            if(account == null)
            {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), "No account selected!");
                return;
            }

            employeeView.showBills(billService.getBillsForAccount(account));
        }
    }

    private class ShowTransferDialogButtonListener implements  ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            List<Account> allAccounts = accountService.getAll();
            employeeView.showTransferWindow(allAccounts);
        }
    }

    private class TransferButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String accountFromNumber = employeeView.getTransferFrom();
            String accountToNumber = employeeView.getTransferTo();

            String transferSumString = employeeView.getTransferSum();
            Double transferSum;
            try {
                transferSum = Double.parseDouble(transferSumString);
            }
            catch(NumberFormatException ex) {
                transferSum = (double) - 1;
            }

            Account accountFrom, accountTo;

            Notification<Account> accountFromNotification = accountService.getAccountByNumber(accountFromNumber);
            if(accountFromNotification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), accountFromNotification.getFormattedErrors());
                return;
            }
            else { accountFrom = accountFromNotification.getResult(); }

            Notification<Account> accountToNotification = accountService.getAccountByNumber(accountToNumber);
            if(accountToNotification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), accountToNotification.getFormattedErrors());
                return;
            }
            else { accountTo = accountToNotification.getResult(); }

            Notification<Boolean> transferNotification = accountService.transferMoney(accountFrom, accountTo, transferSum);

            if(transferNotification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), transferNotification.getFormattedErrors());
            }
            else {
                Boolean result = transferNotification.getResult();
                if(!result) {
                    JOptionPane.showMessageDialog(employeeView.getContentPane(), "Program encountered a database error!");
                }
                else {
                    Activity billProcessing = new ActivityBuilder()
                            .setType(Activity.ActivityTypes.TRANSFER)
                            .setUser(SecurityContext.getCurrentUser().getUsername())
                            .setDate(new Date())
                            .setArguments(String.join(", ", accountFromNumber, accountToNumber, transferSumString))
                            .build();

                    activityService.addActivity(billProcessing);
                }
            }
        }
    }

}
