package service.bill;

import database.DBConnectionFactory;
import model.Account;
import model.Bill;
import model.Client;
import model.builder.AccountBuilder;
import model.builder.BillBuilder;
import model.builder.ClientBuilder;
import model.validation.Notification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.bill.BillRepository;
import repository.bill.BillRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import service.account.AccountService;
import service.account.AccountServiceMySQL;
import service.client.ClientService;
import service.client.ClientServiceMySQL;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BillServiceMySQLTest {

    private static AccountService accountService;
    private static ClientService clientService;
    private static BillService billService;
    private static Client client;
    private static Account account;

    @BeforeAll
    static void setup() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        AccountRepository accountRepository = new AccountRepositoryMySQL(connection);
        accountService = new AccountServiceMySQL(accountRepository);
        ClientRepository clientRepository = new ClientRepositoryMySQL(connection);
        clientService = new ClientServiceMySQL(clientRepository);
        BillRepository billRepository = new BillRepositoryMySQL(connection);
        billService = new BillServiceMySQL(billRepository, accountRepository);

        clientService.deleteAll();
        client = new ClientBuilder()
                .setPersonalNumericalCode("1234567890123")
                .setIdCardNumber("123456")
                .setAddress("Some address idk")
                .setName("Client McClientFace")
                .build();
        clientService.addClient(client);

        client = clientService.getAll().get(0);

        account = new AccountBuilder()
                .setType(Account.AccountTypes.SAVINGS)
                .setClientId(client.getId())
                .setCreationDate(new Date())
                .setMoney((double) 200)
                .setNumber("1234567890")
                .build();

        accountService.deleteAll();
        accountService.createAccountForClient(client, account);

        account = accountRepository.getAll().get(0);
    }

    @BeforeEach
    void clear() {
        billService.deleteAll();
    }

    @Test
    void processBill() {
        account.setMoney((double)200);
        accountService.updateAccount(account);

        Bill b = new BillBuilder()
                .setAccountId(account.getId())
                .setDate(new Date())
                .setSum((double)10)
                .build();

        billService.processBill(b);
        billService.processBill(b);
        billService.processBill(b);

        List<Bill> bills = billService.getBillsForAccount(account);
        assertEquals(3, bills.size());

        account = accountService.getAll().get(0);
        assertEquals(170, account.getMoney());

        b.setSum((double)2000);
        Notification<Boolean> not = billService.processBill(b);
        assertTrue(not.hasErrors());

    }


    @Test
    void deleteAll() {

        Bill b = new BillBuilder()
                .setAccountId(account.getId())
                .setDate(new Date())
                .setSum((double)10)
                .build();

        billService.processBill(b);
        billService.processBill(b);
        billService.processBill(b);

        List<Bill> bills = billService.getBillsForAccount(account);
        assertEquals(3, bills.size());

        billService.deleteAll();
        bills = billService.getBillsForAccount(account);
        assertEquals(0, bills.size());
    }
}