package service.bill;

import model.Account;
import model.Bill;
import model.validation.Notification;

import java.util.List;

public interface BillService {

    public Notification<Boolean> processBill(Bill bill);

    public List<Bill> getBillsForAccount(Account account);

    public void deleteAll();
}
