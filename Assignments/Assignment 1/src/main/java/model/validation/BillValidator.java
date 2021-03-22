package model.validation;

import model.Bill;
import java.util.ArrayList;
import java.util.List;

public class BillValidator {

    public List<String> getErrors() {
        return errors;
    }

    private final List<String> errors;

    private final Bill bill;

    public BillValidator(Bill bill) {
        this.bill = bill;
        errors = new ArrayList<>();
    }

    public boolean validate() {
        validateSum(bill.getSum());
        return errors.isEmpty();
    }

    private void validateSum(Double sum) {
        if (sum < 0)
            errors.add("Cannot create a bill for a zero or negative amount!");
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