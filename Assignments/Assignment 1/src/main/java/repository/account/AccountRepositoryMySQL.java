package repository.account;

import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import model.validation.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.ACCOUNT;

public class AccountRepositoryMySQL implements AccountRepository{

    private final Connection connection;

    public AccountRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Account> getAll() {
        List<Account> clientAccounts = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "Select * from `" + ACCOUNT  + "`;";
            ResultSet accountResultSet = statement.executeQuery(fetchUserSql);
            while (accountResultSet.next()) {
                Account account = new AccountBuilder()
                        .setClientId(accountResultSet.getLong("client_id"))
                        .setCreationDate(accountResultSet.getDate("created"))
                        .setNumber(accountResultSet.getString("number"))
                        .setType(accountResultSet.getString("type"))
                        .setMoney(accountResultSet.getDouble("money"))
                        .setId(accountResultSet.getLong("id"))
                        .build();
                clientAccounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientAccounts;
    }

    @Override
    public Notification<Account> getAccountByNumber(String accountNumber) {
        Notification<Account> findByIdNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "Select * from `" + ACCOUNT + "` where `number`=\"" + accountNumber + "\";";
            ResultSet accountResultSet = statement.executeQuery(fetchUserSql);
            if (accountResultSet.next()) {
                Account account = new AccountBuilder()
                        .setId(accountResultSet.getLong("id"))
                        .setNumber(accountResultSet.getString("number"))
                        .setType(accountResultSet.getString("type"))
                        .setMoney(accountResultSet.getDouble("money"))
                        .setCreationDate(accountResultSet.getDate("created"))
                        .setClientId(accountResultSet.getLong("client_id"))
                        .build();
                findByIdNotification.setResult(account);
                return findByIdNotification;
            } else {
                findByIdNotification.addError("Invalid account id!");
                return findByIdNotification;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            findByIdNotification.addError("Something is wrong with the Database");
        }
        return findByIdNotification;
    }

    @Override
    public void deleteAll() {
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "DELETE from `" + ACCOUNT  + "`;";
            statement.executeUpdate(fetchUserSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Account> getClientAccounts(Client client) {
        List<Account> clientAccounts = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "Select * from `" + ACCOUNT + "` where `client_id`=" + client.getId() + ";";
            ResultSet accountResultSet = statement.executeQuery(fetchUserSql);
            while (accountResultSet.next()) {
                Account account = new AccountBuilder()
                        .setClientId(client.getId())
                        .setCreationDate(accountResultSet.getDate("created"))
                        .setNumber(accountResultSet.getString("number"))
                        .setType(accountResultSet.getString("type"))
                        .setMoney(accountResultSet.getDouble("money"))
                        .setId(accountResultSet.getLong("id"))
                        .build();
                clientAccounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientAccounts;
    }

    @Override
    public boolean createAccount(Account account) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO " + ACCOUNT  + " values (null, ?, ?, ?, ?, ?);");
            insertUserStatement.setString(1, account.getNumber());
            insertUserStatement.setString(2, account.getType());
            insertUserStatement.setDouble(3, account.getMoney());
            insertUserStatement.setDate(4, new java.sql.Date(account.getCreationDate().getTime()));
            insertUserStatement.setLong(5, account.getClientId());
            insertUserStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Notification<Account> getAccountById(Long id) {
        Notification<Account> findByIdNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "Select * from `" + ACCOUNT + "` where `id`=" + id + ";";
            ResultSet accountResultSet = statement.executeQuery(fetchUserSql);
            if (accountResultSet.next()) {
                Account account = new AccountBuilder()
                        .setId(id)
                        .setNumber(accountResultSet.getString("number"))
                        .setType(accountResultSet.getString("type"))
                        .setMoney(accountResultSet.getDouble("money"))
                        .setCreationDate(accountResultSet.getDate("created"))
                        .setClientId(accountResultSet.getLong("client_id"))
                        .build();
                findByIdNotification.setResult(account);
                return findByIdNotification;
            } else {
                findByIdNotification.addError("Invalid account id!");
                return findByIdNotification;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            findByIdNotification.addError("Something is wrong with the Database");
        }
        return findByIdNotification;
    }

    @Override
    public boolean updateAccount(Account account) {
        try {
            PreparedStatement insertClientStatement = connection
                    .prepareStatement("UPDATE " + ACCOUNT + " set number= ? , type=?, money=?, created=?, client_id=? WHERE id = ?;");
            insertClientStatement.setString(1, account.getNumber());
            insertClientStatement.setString(2, account.getType());
            insertClientStatement.setDouble(3, account.getMoney());
            insertClientStatement.setDate(4, new java.sql.Date(account.getCreationDate().getTime()));
            insertClientStatement.setLong(5, account.getClientId());
            insertClientStatement.setLong(6, account.getId());
            insertClientStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAccount(Account account) {
        try {
            PreparedStatement deleteClientStatement = connection
                    .prepareStatement("DELETE from " + ACCOUNT + " WHERE id = ?;");

            deleteClientStatement.setLong(1, account.getId());
            deleteClientStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
