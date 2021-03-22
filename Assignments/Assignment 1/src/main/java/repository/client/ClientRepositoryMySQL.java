package repository.client;

import model.Client;
import model.builder.ClientBuilder;
import model.validation.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.ACCOUNT;
import static database.Constants.Tables.CLIENT;

public class ClientRepositoryMySQL implements  ClientRepository {

    private final Connection connection;

    public ClientRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Client> getAll() {
        List<Client> result = new ArrayList<>();
        try {
            Statement getAllClientsStatement = connection.createStatement();
            ResultSet usersResultSet = getAllClientsStatement.executeQuery("Select * from " + CLIENT);
            while(usersResultSet.next()) {
                Client client = new ClientBuilder()
                        .setName(usersResultSet.getString("name"))
                        .setAddress(usersResultSet.getString("address"))
                        .setIdCardNumber(usersResultSet.getString("id_card_number"))
                        .setPersonalNumericalCode(usersResultSet.getString("personal_numerical_code"))
                        .setId(usersResultSet.getLong("id"))
                        .build();
                result.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean addClient(Client client) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO " + CLIENT  + " values (null, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, client.getName());
            insertUserStatement.setString(2, client.getAddress());
            insertUserStatement.setString(3, client.getIdCardNumber());
            insertUserStatement.setString(4, client.getPersonalNumericalCode());
            insertUserStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Notification<Client> getClientById(Long id) {
        Notification<Client> findByIdNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "Select * from `" + CLIENT + "` where `id`=" + id + ";";
            ResultSet clientResultSet = statement.executeQuery(fetchUserSql);
            if (clientResultSet.next()) {
                Client client = new ClientBuilder()
                        .setId(id)
                        .setName(clientResultSet.getString("name"))
                        .setAddress(clientResultSet.getString("address"))
                        .setIdCardNumber(clientResultSet.getString("id_card_number"))
                        .setPersonalNumericalCode(clientResultSet.getString("personal_numerical_code"))
                        .build();
                findByIdNotification.setResult(client);
                return findByIdNotification;
            } else {
                findByIdNotification.addError("Invalid client id!");
                return findByIdNotification;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            findByIdNotification.addError("Something is wrong with the Database");
        }
        return findByIdNotification;
    }

    @Override
    public boolean updateClient(Client client) {
        try {
            PreparedStatement insertClientStatement = connection
                    .prepareStatement("UPDATE " + CLIENT + " set name= ? , address=?, id_card_number=?, personal_numerical_code=? WHERE id = ?;");
            insertClientStatement.setString(1, client.getName());
            insertClientStatement.setString(2, client.getAddress());
            insertClientStatement.setString(3, client.getIdCardNumber());
            insertClientStatement.setString(4, client.getPersonalNumericalCode());
            insertClientStatement.setLong(5, client.getId());
            insertClientStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteClient(Client client) {
        try {
            PreparedStatement deleteClientStatement = connection
                    .prepareStatement("DELETE from " + CLIENT + " WHERE id = ?;");

            deleteClientStatement.setLong(1, client.getId());
            deleteClientStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteAll() {
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "DELETE from `" + CLIENT  + "`;";
            statement.executeUpdate(fetchUserSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
