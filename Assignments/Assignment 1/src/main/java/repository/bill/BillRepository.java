package repository.bill;

import model.Account;
import model.Bill;

import java.util.List;

public interface BillRepository {
    public boolean addBill(Bill bill);

    public List<Bill> getBillsForAccount(Account account);


    void deleteAll();
}
