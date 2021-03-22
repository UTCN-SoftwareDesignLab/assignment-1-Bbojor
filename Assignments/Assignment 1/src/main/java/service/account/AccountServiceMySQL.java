package service.account;

import model.Account;
import model.Client;
import model.validation.AccountValidator;
import model.validation.Notification;
import repository.account.AccountRepository;

import java.util.List;

public class AccountServiceMySQL implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceMySQL(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> getClientAccounts(Client client) {
        return accountRepository.getClientAccounts(client);
    }

    @Override
    public Notification<Boolean> createAccountForClient(Client client, Account account) {
        Notification<Boolean> result = new Notification<>();

        AccountValidator accountValidator = new AccountValidator(account);
        boolean isAccountValid = accountValidator.validate();
        if(!isAccountValid) {
            accountValidator.getErrors().forEach(result::addError);
            return result;
        }
        account.setClientId(client.getId());
        result.setResult(accountRepository.createAccount(account));
        return result;
    }

    @Override
    public Notification<Boolean> updateAccount(Account account) {
        Notification<Boolean> result = new Notification<>();

        Notification<Account> oldAccountNotification = accountRepository.getAccountById(account.getId());
        if(oldAccountNotification.hasErrors()) {
            result.addError(oldAccountNotification.getFormattedErrors());
            return result;
        }

        Account oldAccount = oldAccountNotification.getResult();

        if(account.getType() == null || account.getType().isBlank())
            account.setType(oldAccount.getType());

        if(account.getNumber() == null || account.getNumber().isBlank())
            account.setNumber(oldAccount.getNumber());

        if(account.getCreationDate() == null)
            account.setCreationDate(oldAccount.getCreationDate());

        if(account.getMoney() == null )
            account.setMoney(oldAccount.getMoney());

        account.setClientId(oldAccount.getClientId());

        AccountValidator accountValidator = new AccountValidator(account);
        boolean isAccountValid = accountValidator.validate();
        if(!isAccountValid) {
            accountValidator.getErrors().forEach(result::addError);
            return result;
        }

        result.setResult(accountRepository.updateAccount(account));
        return result;
    }

    @Override
    public Notification<Boolean> deleteAccount(Account account) {
        Notification<Boolean> result = new Notification<>();
        result.setResult(accountRepository.deleteAccount(account));
        return result;
    }

    @Override
    public Notification<Boolean> transferMoney(Account from, Account to, Double sum) {
        Notification<Boolean> result = new Notification<>();

        if(from.getId().equals(to.getId())) {
            result.addError("Cannot transfer between the same account!");
            return result;
        }

        if(sum <= 0) {
            result.addError("Cannot transfer a zero or negative sum!");
            return result;
        }

        if(from.getMoney() < sum) {
            result.addError("Not enough money in account in order to complete the transfer!");
            return result;
        }

        from.setMoney(from.getMoney() - sum);
        to.setMoney(to.getMoney() + sum);
        result.setResult(accountRepository.updateAccount(from) && accountRepository.updateAccount(to));

        return result;
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.getAll();
    }

    @Override
    public Notification<Account> getAccountByNumber(String accountNumber) {
        return accountRepository.getAccountByNumber(accountNumber);
    }

    @Override
    public void deleteAll() {
        accountRepository.deleteAll();
    }
}
