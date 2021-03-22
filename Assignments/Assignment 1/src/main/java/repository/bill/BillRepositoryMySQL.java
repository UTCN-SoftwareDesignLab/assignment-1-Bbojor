package repository.bill;

import model.Account;
import model.Bill;
import model.builder.BillBuilder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.ACCOUNT;
import static database.Constants.Tables.BILL;

public class BillRepositoryMySQL implements BillRepository {

    private final Connection connection;

    public BillRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addBill(Bill bill) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO " + BILL  + " values (null, ?, ?, ?);");
            insertUserStatement.setDouble(1, bill.getSum());
            insertUserStatement.setDate(2, new java.sql.Date(bill.getDate().getTime()));
            insertUserStatement.setLong(3,bill.getAccountId());
            insertUserStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Bill> getBillsForAccount(Account account) {
        List<Bill> accountBills = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String fetchBillsSql = "Select * from `" + BILL + "` where `account_id`=" + account.getId() + ";";
            ResultSet billResultSet = statement.executeQuery(fetchBillsSql);
            while (billResultSet.next()) {
                Bill bill = new BillBuilder()
                        .setAccountId(account.getId())
                        .setDate(billResultSet.getDate("date"))
                        .setSum(billResultSet.getDouble("sum"))
                        .setId(billResultSet.getLong("id"))
                        .build();
                accountBills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountBills;
    }

    @Override
    public void deleteAll() {
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "DELETE from `" + BILL  + "`;";
            statement.executeUpdate(fetchUserSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
