package model.builder;

import model.Account;
import model.Client;

import java.util.Date;

public class AccountBuilder {
    private Account account;

    public AccountBuilder() {
        account = new Account();
    }

    public AccountBuilder setNumber(String number) {
        account.setNumber(number);
        return this;
    }

    public AccountBuilder setClientId(Long client) {
        account.setClientId(client);
        return this;
    }

    public AccountBuilder setType(String type) {
        account.setType(type);
        return this;
    }

    public AccountBuilder setCreationDate(Date creationDate) {
        account.setCreationDate(creationDate);
        return this;
    }

    public AccountBuilder setMoney(Double money) {
        account.setMoney(money);
        return this;
    }

    public AccountBuilder setId(Long id) {
        account.setId(id);
        return this;
    }

    public Account build() {
        return account;
    }
}