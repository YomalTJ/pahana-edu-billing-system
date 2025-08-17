package com.pahanaedu.model;

import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import java.util.Arrays;
import static org.junit.Assert.*;

public class BillingModelTest {

    @Test
    public void testBillModel() {
        Bill bill = new Bill();
        Customer customer = TestDataProvider.getValidCustomer();

        // Test setters and getters
        bill.setBillId("BIL00001");
        bill.setCustomer(customer);
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
        bill.setItems(Arrays.asList(item));

        assertEquals("BIL00001", bill.getBillId());
        assertEquals(customer, bill.getCustomer());
        assertNotNull(bill.getBillDate());
        assertEquals(100.0, bill.getSubtotal(), 0.001);
        assertEquals(10.0, bill.getDiscount(), 0.001);
        assertEquals(7.2, bill.getTax(), 0.001);
        assertEquals(97.2, bill.getTotal(), 0.001);
        assertEquals("CASH", bill.getPaymentMethod());
        assertEquals("PAID", bill.getPaymentStatus());
        assertEquals(1, bill.getItems().size());
        assertEquals(100.0, bill.getItems().get(0).getLineTotal(), 0.001);
    }

    @Test
    public void testInvoiceModel() {
        Invoice invoice = new Invoice();
        Customer customer = TestDataProvider.getValidCustomer();

        // Test setters and getters
        invoice.setInvoiceId("INV00001");
        invoice.setCustomerAccount(customer.getAccountNumber());
        invoice.setCustomer(customer);
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
        invoice.setItems(Arrays.asList(item));

        assertEquals("INV00001", invoice.getInvoiceId());
        assertEquals(customer.getAccountNumber(), invoice.getCustomerAccount());
        assertEquals(customer, invoice.getCustomer());
        assertNotNull(invoice.getInvoiceDate());
        assertEquals(100.0, invoice.getSubtotal(), 0.001);
        assertEquals(10.0, invoice.getDiscount(), 0.001);
        assertEquals(7.2, invoice.getTax(), 0.001);
        assertEquals(97.2, invoice.getTotal(), 0.001);
        assertEquals("PAID", invoice.getStatus());
        assertEquals(1, invoice.getItems().size());
        assertEquals(100.0, invoice.getItems().get(0).getLineTotal(), 0.001);
    }

    @Test
    public void testBillItemModel() {
        BillItem item = new BillItem();
        Item product = TestDataProvider.getValidItem();

        item.setBillItemId(1);
        item.setItem(product);
        item.setQuantity(2);
        item.setUnitPrice(50.0);
        item.setLineTotal(100.0);

        assertEquals(1, item.getBillItemId());
        assertEquals(product, item.getItem());
        assertEquals(2, item.getQuantity());
        assertEquals(50.0, item.getUnitPrice(), 0.001);
        assertEquals(100.0, item.getLineTotal(), 0.001);
    }

    @Test
    public void testInvoiceItemModel() {
        InvoiceItem item = new InvoiceItem();
        Item product = TestDataProvider.getValidItem();

        item.setInvoiceItemId(1);
        item.setInvoiceId("INV00001");
        item.setItemId(product.getItemId());
        item.setItem(product);
        item.setQuantity(2);
        item.setUnitPrice(50.0);
        item.setLineTotal(100.0);

        assertEquals(1, item.getInvoiceItemId());
        assertEquals("INV00001", item.getInvoiceId());
        assertEquals(product.getItemId(), item.getItemId());
        assertEquals(product, item.getItem());
        assertEquals(2, item.getQuantity());
        assertEquals(50.0, item.getUnitPrice(), 0.001);
        assertEquals(100.0, item.getLineTotal(), 0.001);
    }
}