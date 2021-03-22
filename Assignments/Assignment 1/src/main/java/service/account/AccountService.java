package service.account;

import model.Account;
import model.Client;
import model.validation.Notification;

import java.util.List;

public interface AccountService {

    public List<Account> getClientAccounts(Client client);

    public Notification<Boolean> createAccountForClient(Client client, Account account);

    public Notification<Boolean> updateAccount(Account account);

    public Notification<Boolean> deleteAccount(Account account);

    public Notification<Boolean> transferMoney(Account from, Account to, Double sum);

    public List<Account> getAll();

    public Notification<Account> getAccountByNumber(String accountFromNumber);

    public void deleteAll();
}
