package repository.account;

import model.Account;
import model.Client;
import model.validation.Notification;

import java.util.List;

public interface AccountRepository {

    public List<Account> getClientAccounts(Client client);

    public boolean createAccount(Account account);

    public Notification<Account> getAccountById(Long id);

    public boolean updateAccount(Account account);

    public boolean deleteAccount(Account account);

    public List<Account> getAll();

    public Notification<Account> getAccountByNumber(String accountNumber);

    void deleteAll();
}
