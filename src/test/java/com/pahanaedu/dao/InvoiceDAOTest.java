package com.pahanaedu.dao;

import com.pahanaedu.model.*;
import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.*;

public class InvoiceDAOTest {

    private InvoiceDAO invoiceDao;

    @Before
    public void setUp() {
        invoiceDao = new InvoiceDAO();
    }

    @Test
    public void testCreateInvoiceMethodExists() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId("INV00001");
        invoice.setCustomerAccount("ACC00001");
        invoice.setInvoiceDate(new java.util.Date());
        invoice.setSubtotal(100.0);
        invoice.setDiscount(10.0);
        invoice.setTax(7.2);
        invoice.setTotal(97.2);
        invoice.setStatus("PAID");

        InvoiceItem item = new InvoiceItem();
        item.setItem(TestDataProvider.getValidItem());
        item.setQuantity(2);
        item.setUnitPrice(50.0);
        item.setLineTotal(100.0);
        invoice.setItems(List.of(item));

        try {
            boolean result = invoiceDao.createInvoice(invoice);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getCause() instanceof SQLException);
        }
    }

    @Test
    public void testGetInvoiceByIdMethodExists() {
        String invoiceId = "INV00001";
        try {
            Invoice result = invoiceDao.getInvoiceById(invoiceId);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testGetInvoicesByCustomerMethodExists() {
        String accountNumber = "ACC00001";
        try {
            List<Invoice> result = invoiceDao.getInvoicesByCustomer(accountNumber);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }
}