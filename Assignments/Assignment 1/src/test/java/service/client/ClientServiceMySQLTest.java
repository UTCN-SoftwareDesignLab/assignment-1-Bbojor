package service.client;

import database.DBConnectionFactory;
import model.Client;
import model.builder.ClientBuilder;
import model.validation.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;

import java.sql.Connection;
import java.util.List;

class ClientServiceMySQLTest {

    private static ClientService clientService;

    @BeforeAll
    static void setup() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        ClientRepository clientRepository = new ClientRepositoryMySQL(connection);
        clientService = new ClientServiceMySQL(clientRepository);
    }

    @BeforeEach
    void clean() {
        clientService.deleteAll();
    }


    @Test
    void getAll() {

        int NO_CLIENTS = 10;

        for(int i = 0; i < NO_CLIENTS; i++) {

            Client client = new ClientBuilder()
                                .setPersonalNumericalCode("1234567890123")
                                .setIdCardNumber("123456")
                                .setAddress("Some address idk")
                                .setName("Client McClientFace")
                                .build();
            clientService.addClient(client);
        }

        List<Client> clients = clientService.getAll();

        Assertions.assertEquals(NO_CLIENTS, clients.size());
    }

    @Test
    void addClient() {

        Client client = new ClientBuilder()
                .setPersonalNumericalCode("1234567890123")
                .setIdCardNumber("123456")
                .setAddress("Some address idk")
                .setName("Client McClientFace")
                .build();

        Notification<Boolean> addNotification = clientService.addClient(client);
        Assertions.assertFalse(addNotification.hasErrors());

        List<Client> clients = clientService.getAll();
        Assertions.assertEquals(1, clients.size());

         client = new ClientBuilder()
                .setPersonalNumericalCode("1234567890123")
                .setAddress("Some address idk")
                .setName("Client McClientFace")
                .build();

        addNotification = clientService.addClient(client);
        Assertions.assertTrue(addNotification.hasErrors());

        client =new ClientBuilder()
                .setPersonalNumericalCode("1234567890123")
                .setIdCardNumber("123456")
                .setAddress("Some address idk")
                .build();

        addNotification = clientService.addClient(client);
        Assertions.assertTrue(addNotification.hasErrors());

        client = new ClientBuilder()
                .setPersonalNumericalCode("1234567890123")
                .setIdCardNumber("123456")
                .setName("Client McClientFace")
                .build();

        addNotification = clientService.addClient(client);
        Assertions.assertTrue(addNotification.hasErrors());

        client = new ClientBuilder()
                .setIdCardNumber("123456")
                .setAddress("Some address idk")
                .setName("Client McClientFace")
                .build();

        addNotification = clientService.addClient(client);
        Assertions.assertTrue(addNotification.hasErrors());
    }

    @Test
    void updateClient() {

        Client client = new ClientBuilder()
                .setPersonalNumericalCode("1234567890123")
                .setIdCardNumber("123456")
                .setAddress("Some address idk")
                .setName("Client McClientFace")
                .build();

        Notification<Boolean> addNotification = clientService.addClient(client);
        Assertions.assertFalse(addNotification.hasErrors());

        List<Client> clients = clientService.getAll();
        Assertions.assertEquals(1, clients.size());

        client = clients.get(0);

        client.setName("New client Name");
        client.setAddress("New client address");
        client.setPersonalNumericalCode("0987654321321");
        client.setIdCardNumber("111111");

        Notification<Boolean> updateNotification = clientService.updateClient(client);
        Assertions.assertFalse(updateNotification.hasErrors());
        client = clientService.getAll().get(0);

        Assertions.assertEquals("New client Name", client.getName());
        Assertions.assertEquals("New client address", client.getAddress());
        Assertions.assertEquals("0987654321321", client.getPersonalNumericalCode());
        Assertions.assertEquals("111111", client.getIdCardNumber());

    }

    @Test
    void deleteClient() {

        Client client = new ClientBuilder()
                .setPersonalNumericalCode("1234567890123")
                .setIdCardNumber("123456")
                .setAddress("Some address idk")
                .setName("Client McClientFace")
                .build();

        Notification<Boolean> addNotification = clientService.addClient(client);
        Assertions.assertFalse(addNotification.hasErrors());

        List<Client> clients = clientService.getAll();
        Assertions.assertEquals(1, clients.size());

        clientService.deleteClient(clients.get(0));

        clients = clientService.getAll();
        Assertions.assertEquals(0, clients.size());
    }

    @Test
    void deleteAll() {
        int NO_CLIENTS = 10;

        for(int i = 0; i < NO_CLIENTS; i++) {

            Client client = new ClientBuilder()
                    .setPersonalNumericalCode("1234567890123")
                    .setIdCardNumber("123456")
                    .setAddress("Some address idk")
                    .setName("Client McClientFace")
                    .build();
            clientService.addClient(client);
        }

        List<Client> clients = clientService.getAll();

        Assertions.assertEquals(NO_CLIENTS, clients.size());

        clientService.deleteAll();

        clients = clientService.getAll();

        Assertions.assertEquals(0, clients.size());
    }
}