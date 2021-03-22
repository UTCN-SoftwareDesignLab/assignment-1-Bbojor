package model;

import java.util.Date;

public class Account {

    public static class AccountTypes {
        public static String SAVINGS = "savings";
        public static String CHECKING = "checking";
        public static String DEPOSIT = "deposit";
        public static String RETIREMENT = "retirement";

        public static String[] allTypes = {SAVINGS, CHECKING, DEPOSIT, RETIREMENT};
    }

    private Long id;
    private String number;
    private String type;
    private Double money;
    private Date creationDate;
    private Long clientId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return String.join(", ", number, type, money.toString(), creationDate.toString(), clientId.toString());
    }
}
