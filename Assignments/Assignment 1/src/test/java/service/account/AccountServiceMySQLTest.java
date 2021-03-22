package service.account;

import database.DBConnectionFactory;
import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import model.builder.ClientBuilder;
import model.validation.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import service.client.ClientService;
import service.client.ClientServiceMySQL;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

class AccountServiceMySQLTest {

    private static AccountService accountService;
    private static ClientService clientService;
    private static Client client;

    @BeforeAll
    static void setup() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        AccountRepository accountRepository = new AccountRepositoryMySQL(connection);
        accountService = new AccountServiceMySQL(accountRepository);
        ClientRepository clientRepository = new ClientRepositoryMySQL(connection);
        clientService = new ClientServiceMySQL(clientRepository);

        clientService.deleteAll();
        client = new ClientBuilder()
                .setPersonalNumericalCode("1234567890123")
                .setIdCardNumber("123456")
                .setAddress("Some address idk")
                .setName("Client McClientFace")
                .build();
        clientService.addClient(client);

        client = clientService.getAll().get(0);
    }

    @BeforeEach
    void clear() {
        accountService.deleteAll();
    }

    @Test
    void getClientAccounts() {
        int NO_ACCOUNTS = 10;

        for(int i = 0; i < NO_ACCOUNTS; i++) {

            Account account = new AccountBuilder()
                    .setType(Account.AccountTypes.SAVINGS)
                    .setClientId(client.getId())
                    .setCreationDate(new Date())
                    .setMoney((double) 200)
                    .setNumber("123456789" + i)
                    .build();

            accountService.createAccountForClient(client, account);
        }

        List<Account> accounts = accountService.getClientAccounts(client);

        Assertions.assertEquals(NO_ACCOUNTS, accounts.size());
    }

    @Test
    void updateAccount() {
        Account account = new AccountBuilder()
                .setType(Account.AccountTypes.SAVINGS)
                .setClientId(client.getId())
                .setCreationDate(new Date())
                .setMoney((double) 200)
                .setNumber("1234567890")
                .build();

        accountService.createAccountForClient(client, account);
        account = accountService.getAll().get(0);
        account.setType(Account.AccountTypes.CHECKING);
        account.setMoney((double)1000);
        account.setNumber("1234567891");
        Notification<Boolean> accountNot = accountService.updateAccount(account);
        Assertions.assertFalse(accountNot.hasErrors());
        Assertions.assertTrue(accountNot.getResult());

        account = accountService.getAll().get(0);

        Assertions.assertEquals((double)1000, account.getMoney());
        Assertions.assertEquals(Account.AccountTypes.CHECKING, account.getType());
        Assertions.assertEquals("1234567891", account.getNumber());

    }

    @Test
    void deleteAccount() {
        Account account = new AccountBuilder()
                .setType(Account.AccountTypes.SAVINGS)
                .setClientId(client.getId())
                .setCreationDate(new Date())
                .setMoney((double) 200)
                .setNumber("1234567890")
                .build();

        accountService.createAccountForClient(client, account);
        account = accountService.getAll().get(0);
        accountService.deleteAccount(account);

        List<Account> accounts = accountService.getClientAccounts(client);

        Assertions.assertEquals(0, accounts.size());
    }

    @Test
    void transferMoney() {
        Account account1 = new AccountBuilder()
                .setType(Account.AccountTypes.SAVINGS)
                .setClientId(client.getId())
                .setCreationDate(new Date())
                .setMoney((double) 200)
                .setNumber("1234567890")
                .build();

        Account account2 = new AccountBuilder()
                .setType(Account.AccountTypes.SAVINGS)
                .setClientId(client.getId())
                .setCreationDate(new Date())
                .setMoney((double) 200)
                .setNumber("1234567891")
                .build();

        accountService.createAccountForClient(client, account1);
        accountService.createAccountForClient(client, account2);

        account1 = accountService.getAll().get(0);
        account2 = accountService.getAll().get(1);

        accountService.transferMoney(account1, account2, (double)200);

        account1 = accountService.getAll().get(0);
        account2 = accountService.getAll().get(1);

        Assertions.assertEquals(0, account1.getMoney());
        Assertions.assertEquals(400, account2.getMoney());

        Notification<Boolean> not = accountService.transferMoney(account1, account2, (double)200);
        Assertions.assertTrue(not.hasErrors());
    }

    @Test
    void getAll() {
        int NO_ACCOUNTS = 10;

        for(int i = 0; i < NO_ACCOUNTS; i++) {

            Account account = new AccountBuilder()
                    .setType(Account.AccountTypes.SAVINGS)
                    .setClientId(client.getId())
                    .setCreationDate(new Date())
                    .setMoney((double) 200)
                    .setNumber("123456789" + i)
                    .build();

            accountService.createAccountForClient(client, account);
        }

        List<Account> accounts = accountService.getAll();

        Assertions.assertEquals(NO_ACCOUNTS, accounts.size());
    }

    @Test
    void getAccountByNumber() {

        int NO_ACCOUNTS = 10;

        for(int i = 0; i < NO_ACCOUNTS; i++) {

            Account account = new AccountBuilder()
                    .setType(Account.AccountTypes.SAVINGS)
                    .setClientId(client.getId())
                    .setCreationDate(new Date())
                    .setMoney((double) 200)
                    .setNumber("123456789" + i)
                    .build();

            accountService.createAccountForClient(client, account);
        }

        Notification<Account> accountNot = accountService.getAccountByNumber("1234567890");
        Assertions.assertFalse(accountNot.hasErrors());
        Assertions.assertNotNull(accountNot.getResult());

        accountNot = accountService.getAccountByNumber("1234564890");
        Assertions.assertTrue(accountNot.hasErrors());
    }

    @Test
    void deleteAll() {
        int NO_ACCOUNTS = 10;

        for(int i = 0; i < NO_ACCOUNTS; i++) {

            Account account = new AccountBuilder()
                    .setType(Account.AccountTypes.SAVINGS)
                    .setClientId(client.getId())
                    .setCreationDate(new Date())
                    .setMoney((double) 200)
                    .setNumber("123456789" + i)
                    .build();

            accountService.createAccountForClient(client, account);
        }

        List<Account> accounts = accountService.getAll();

        Assertions.assertEquals(NO_ACCOUNTS, accounts.size());

        accountService.deleteAll();

        accounts = accountService.getAll();

        Assertions.assertEquals(0, accounts.size());
    }
}