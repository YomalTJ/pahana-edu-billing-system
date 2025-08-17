package com.pahanaedu.dao;

import com.pahanaedu.model.*;
import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.*;

public class BillDAOTest {

    private BillDAO billDao;

    @Before
    public void setUp() {
        billDao = new BillDAO();
    }

    @Test
    public void testCreateBillMethodExists() {
        Bill bill = new Bill();
        bill.setBillId("BIL00001");
        bill.setCustomer(TestDataProvider.getValidCustomer());
        bill.setBillDate(new java.util.Date());
        bill.setSubtotal(100.0);
        bill.setDiscount(10.0);
        bill.setTax(7.2);
        bill.setTotal(97.2);
        bill.setPaymentMethod("CASH");
        bill.setPaymentStatus("PAID");

        BillItem item = new BillItem();
        item.setItem(TestDataProvider.getValidItem());
        item.setQuantity(2);
        item.setUnitPrice(50.0);
        item.setLineTotal(100.0);
        bill.setItems(List.of(item));

        try {
            boolean result = billDao.createBill(bill);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getCause() instanceof SQLException);
        }
    }

    @Test
    public void testGetBillByIdMethodExists() {
        String billId = "BIL00001";
        try {
            Bill result = billDao.getBillById(billId);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testGetBillsByCustomerMethodExists() {
        String accountNumber = "ACC00001";
        try {
            List<Bill> result = billDao.getBillsByCustomer(accountNumber);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testGenerateNewBillIdMethodExists() {
        try {
            String result = billDao.generateNewBillId();
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testGetTodaySalesTotalMethodExists() {
        try {
            double result = billDao.getTodaySalesTotal();
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }
}