package model.validation;

import model.Account;
import model.Client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountValidator {

    private static final int IDENTIFICATION_NUMBER_LENGTH = 10;


    public List<String> getErrors() {
        return errors;
    }

    private final List<String> errors;

    private final Account account;

    public AccountValidator(Account account) {
        this.account = account;
        errors = new ArrayList<>();
    }

    public boolean validate() {
        validateIdentificationNumber(account.getNumber());
        validateSum(account.getMoney());
        validateDate(account.getCreationDate());
        return errors.isEmpty();
    }

    private void validateSum(Double sum) {
        if(sum < 0)
            errors.add("Account cannot hold a negative sum!");
    }

    private void validateDate(Date date) {
        if(date == null)
            errors.add("Date cannot be null!");
    }

    private void validateIdentificationNumber(String number) {
        if(number.length() != IDENTIFICATION_NUMBER_LENGTH)
            errors.add("Account id number must be of length " + IDENTIFICATION_NUMBER_LENGTH + "!");

        if(containsNonDigit(number))
            errors.add("Account id number must contain only digits!");
    }

    private boolean containsNonDigit(String s) {
        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return true;
                }
            }
        }
        return false;
    }

}
