package model.validation;

import model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientValidator {

    private static final int PERSONAL_NUMERIC_CODE_LENGTH = 13;
    private static final int CARD_NUMBER_LENGTH = 6;
    private static final int MINIMUM_NAME_LENGTH = 4;

    public List<String> getErrors() {
        return errors;
    }

    private final List<String> errors;

    private final Client client;

    public ClientValidator(Client client) {
        this.client = client;
        errors = new ArrayList<>();
    }

    public boolean validate() {
        validateName(client.getName());
        validatePersonalNumericalCode(client.getPersonalNumericalCode());
        validateAddress(client.getAddress());
        validateCardNumber(client.getIdCardNumber());
        return errors.isEmpty();
    }

    private void validateAddress(String address) {
        if (address == null || address.isBlank()) {
            errors.add("Address cannot be blank!");
        }
    }

    private void validateCardNumber(String cardNumber) {
        if (cardNumber == null|| cardNumber.length() != CARD_NUMBER_LENGTH) {
            errors.add("Card number must have a length of " + CARD_NUMBER_LENGTH + "!");
        }

        if (containsNonDigit(cardNumber)) {
            errors.add("Card number must contain only digits!");
        }
    }

    public void validateName(String name) {
        if (name == null || name.length() < MINIMUM_NAME_LENGTH) {
            errors.add("Name is too short!");
        }
    }

    public void validatePersonalNumericalCode(String personalNumericalCode) {
        if (personalNumericalCode == null || personalNumericalCode.length() != PERSONAL_NUMERIC_CODE_LENGTH) {
            errors.add("Personal numerical code must have a length of " + PERSONAL_NUMERIC_CODE_LENGTH + "!");
        }

        if (containsNonDigit(personalNumericalCode)) {
            errors.add("Personal numerical code must contain only digits!");
        }
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
