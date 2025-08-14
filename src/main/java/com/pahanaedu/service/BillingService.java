package com.pahanaedu.service;

import com.pahanaedu.model.*;
import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.dao.InvoiceDAO;
import com.pahanaedu.dao.ItemDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BillingService {
    private InvoiceDAO invoiceDao;
    private ItemDAO itemDao;
    private CustomerDAO customerDao;

    public BillingService() {
        try {
            this.invoiceDao = new InvoiceDAO();
            this.itemDao = new ItemDAO();
            this.customerDao = new CustomerDAO();
        } catch (Exception e) {
            System.err.println("Error initializing BillingService: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Invoice createInvoice(String customerAccount, List<InvoiceItem> items) {
        if (customerAccount == null || customerAccount.trim().isEmpty()) {
            System.err.println("Customer account is null or empty");
            return null;
        }

        if (items == null || items.isEmpty()) {
            System.err.println("Items list is null or empty");
            return null;
        }

        try {
            // Get customer to apply discount
            Customer customer = customerDao.getCustomerByAccountNumber(customerAccount);
            if (customer == null) {
                System.err.println("Customer not found for account: " + customerAccount);
                return null;
            }

            // Calculate totals
            double subtotal = calculateSubtotal(items);
            double discount = calculateDiscount(subtotal, customer.getCustomerType());
            double tax = calculateTax(subtotal - discount);
            double total = subtotal - discount + tax;

            // Create invoice
            Invoice invoice = new Invoice();
            invoice.setInvoiceId(generateInvoiceId());
            invoice.setCustomerAccount(customerAccount);
            invoice.setCustomer(customer);
            invoice.setInvoiceDate(new Date());
            invoice.setSubtotal(subtotal);
            invoice.setDiscount(discount);
            invoice.setTax(tax);
            invoice.setTotal(total);
            invoice.setStatus("PENDING");
            invoice.setItems(items);

            // Save to database
            if (invoiceDao.createInvoice(invoice)) {
                return invoice;
            }
        } catch (Exception e) {
            System.err.println("Error creating invoice: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Item> getAllAvailableItems() {
        try {
            List<Item> items = itemDao.getAllItems();
            // Filter out items with zero stock if needed
            // items = items.stream().filter(item -> item.getStockQuantity() > 0).collect(Collectors.toList());
            return items != null ? items : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error getting all items: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private double calculateSubtotal(List<InvoiceItem> items) {
        if (items == null || items.isEmpty()) {
            return 0.0;
        }

        return items.stream()
                .mapToDouble(item -> item.getLineTotal() != 0 ? item.getLineTotal() : 0.0)
                .sum();
    }

    private double calculateDiscount(double subtotal, String customerType) {
        if (customerType == null) {
            return 0.0;
        }

        double discountPercentage = 0.0;

        switch (customerType.toLowerCase()) {
            case "premium":
                discountPercentage = 0.10; // 10%
                break;
            case "student":
                discountPercentage = 0.15; // 15%
                break;
            case "wholesale":
                discountPercentage = 0.20; // 20%
                break;
            // Regular customers get no discount
            default:
                discountPercentage = 0.0;
        }

        return subtotal * discountPercentage;
    }

    private double calculateTax(double amount) {
        // Assuming 8% tax rate
        return amount * 0.08;
    }

    private String generateInvoiceId() {
        // Simple invoice ID generation - in production use a more robust method
        return "INV-" + System.currentTimeMillis();
    }

    public Invoice getInvoiceById(String invoiceId) {
        if (invoiceId == null || invoiceId.trim().isEmpty()) {
            return null;
        }

        try {
            return invoiceDao.getInvoiceById(invoiceId);
        } catch (Exception e) {
            System.err.println("Error getting invoice by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Invoice> getInvoicesByCustomer(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            List<Invoice> invoices = invoiceDao.getInvoicesByCustomer(accountNumber);
            return invoices != null ? invoices : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error getting invoices by customer: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Item getItemById(String itemId) throws SQLException {
        if (itemId == null || itemId.trim().isEmpty()) {
            return null;
        }

        try {
            return itemDao.getItemById(itemId);
        } catch (SQLException e) {
            System.err.println("Error getting item by ID: " + e.getMessage());
            throw e;
        }
    }

    public Customer getCustomerByAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return null;
        }

        try {
            return customerDao.getCustomerByAccountNumber(accountNumber);
        } catch (Exception e) {
            System.err.println("Error getting customer by account number: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Item> searchItems(String query) {
        try {
            List<Item> allItems = itemDao.getAllItems();
            if (query == null || query.trim().isEmpty()) {
                return allItems != null ? allItems : new ArrayList<>();
            }

            // Basic in-memory search (replace with your logic)
            return allItems.stream()
                    .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error searching items: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Customer> getAllCustomers() {
        try {
            List<Customer> customers = customerDao.getAllCustomers();
            return customers != null ? customers : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error getting all customers: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}