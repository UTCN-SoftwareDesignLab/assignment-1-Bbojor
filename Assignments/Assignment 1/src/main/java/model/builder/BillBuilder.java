package model.builder;

import model.Bill;
import java.util.Date;

public class BillBuilder {

    private Bill bill;

    public BillBuilder() {
        bill = new Bill();
    }

    public BillBuilder setDate(Date date) {
        bill.setDate(date);
        return this;
    }

    public BillBuilder setSum(Double sum) {
        bill.setSum(sum);
        return this;
    }

    public BillBuilder setId(Long id)
    {
        bill.setId(id);
        return this;
    }

    public BillBuilder setAccountId(Long id)
    {
        bill.setAccountId(id);
        return this;
    }


    public Bill build() {
        return bill;
    }

}

