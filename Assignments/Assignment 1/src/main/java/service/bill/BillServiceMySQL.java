package service.bill;

import model.Account;
import model.Bill;
import model.validation.BillValidator;
import model.validation.Notification;
import repository.account.AccountRepository;
import repository.bill.BillRepository;

import java.util.List;

public class BillServiceMySQL implements BillService {

    private final BillRepository billRepository;
    private final AccountRepository accountRepository;

    public BillServiceMySQL(BillRepository billRepository, AccountRepository accountRepository) {
        this.billRepository = billRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Notification<Boolean> processBill(Bill bill) {
        Notification<Boolean> result = new Notification<>();

        BillValidator billValidator = new BillValidator(bill);
        boolean isBillValid = billValidator.validate();

        if(!isBillValid) {
            billValidator.getErrors().forEach(result::addError);
            return result;
        }

        Notification<Account> accountNotification = accountRepository.getAccountById(bill.getAccountId());
        if(accountNotification.hasErrors()) {
            result.addError(accountNotification.getFormattedErrors());
            return result;
        }

        Account payerAccount = accountNotification.getResult();
        if(payerAccount.getMoney() < bill.getSum()) {
            result.addError("Account does not have enough money to process the bill!");
            return result;
        }

        payerAccount.setMoney(payerAccount.getMoney() - bill.getSum());

        result.setResult(accountRepository.updateAccount(payerAccount) && billRepository.addBill(bill));

        return result;
    }

    @Override
    public List<Bill> getBillsForAccount(Account account) {
        return billRepository.getBillsForAccount(account);
    }

    @Override
    public void deleteAll() {
        billRepository.deleteAll();
    }
}
