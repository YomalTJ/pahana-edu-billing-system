package com.pahanaedu.controller;

import com.pahanaedu.model.*;
import com.pahanaedu.service.BillingService;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "BillingServlet", value = "/billing")
public class BillingServlet extends HttpServlet {
    private BillingService billingService;

    @Override
    public void init() throws ServletException {
        super.init();
        billingService = new BillingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "new";
        }

        try {
            switch (action) {
                case "view":
                    viewInvoice(request, response);
                    break;
                case "history":
                    viewCustomerHistory(request, response);
                    break;
                case "search":
                    searchItems(request, response);
                    break;
                case "new":
                default:
                    showNewInvoiceForm(request, response);
                    break;
            }
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            showNewInvoiceForm(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                createInvoice(request, response);
            } else if ("search".equals(action)) {
                searchItems(request, response);
            } else {
                showNewInvoiceForm(request, response);
            }
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            showNewInvoiceForm(request, response);
        }
    }

    private void showNewInvoiceForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Customer> customers = billingService.getAllCustomers();
            List<Item> allItems = billingService.getAllAvailableItems(); // Add this method to your service

            if (customers == null) {
                customers = new ArrayList<>();
            }
            if (allItems == null) {
                allItems = new ArrayList<>();
            }

            request.setAttribute("customers", customers);
            request.setAttribute("allItems", allItems); // Add this line
            request.getRequestDispatcher("/WEB-INF/views/billing/new.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load form data: " + e.getMessage());
            request.setAttribute("customers", new ArrayList<Customer>());
            request.setAttribute("allItems", new ArrayList<Item>());
            request.getRequestDispatcher("/WEB-INF/views/billing/new.jsp").forward(request, response);
        }
    }

    private void viewInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String invoiceId = request.getParameter("invoiceId");

        if (invoiceId == null || invoiceId.trim().isEmpty()) {
            request.setAttribute("error", "Invoice ID is required");
            showNewInvoiceForm(request, response);
            return;
        }

        try {
            Invoice invoice = billingService.getInvoiceById(invoiceId);

            if (invoice != null) {
                request.setAttribute("invoice", invoice);
                request.getRequestDispatcher("/WEB-INF/views/billing/view.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Invoice not found");
                showNewInvoiceForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error retrieving invoice: " + e.getMessage());
            showNewInvoiceForm(request, response);
        }
    }

    private void viewCustomerHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNumber");

        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            request.setAttribute("error", "Customer account number is required");
            showNewInvoiceForm(request, response);
            return;
        }

        try {
            List<Invoice> invoices = billingService.getInvoicesByCustomer(accountNumber);
            Customer customer = billingService.getCustomerByAccountNumber(accountNumber);

            if (customer == null) {
                request.setAttribute("error", "Customer not found");
                showNewInvoiceForm(request, response);
                return;
            }

            request.setAttribute("invoices", invoices != null ? invoices : new ArrayList<Invoice>());
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/WEB-INF/views/billing/history.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error retrieving customer history: " + e.getMessage());
            showNewInvoiceForm(request, response);
        }
    }

    private void createInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerAccount = request.getParameter("customerAccount");

        if (customerAccount == null || customerAccount.trim().isEmpty()) {
            request.setAttribute("error", "Customer account is required");
            showNewInvoiceForm(request, response);
            return;
        }

        try {
            Customer customer = billingService.getCustomerByAccountNumber(customerAccount);

            if (customer == null) {
                request.setAttribute("error", "Customer not found");
                showNewInvoiceForm(request, response);
                return;
            }

            // Get items from the request
            String[] itemIds = request.getParameterValues("itemId");
            String[] quantities = request.getParameterValues("quantity");

            if (itemIds == null || itemIds.length == 0) {
                request.setAttribute("error", "No items selected");
                showNewInvoiceForm(request, response);
                return;
            }

            List<InvoiceItem> items = new ArrayList<>();

            for (int i = 0; i < itemIds.length; i++) {
                String itemId = itemIds[i];
                if (itemId == null || itemId.trim().isEmpty()) {
                    continue;
                }

                try {
                    int quantity = Integer.parseInt(quantities[i]);

                    if (quantity > 0) {
                        Item item = billingService.getItemById(itemId);

                        if (item != null && item.getStockQuantity() >= quantity) {
                            InvoiceItem invoiceItem = new InvoiceItem();
                            invoiceItem.setItemId(itemId);
                            invoiceItem.setQuantity(quantity);
                            invoiceItem.setUnitPrice(item.getUnitPrice());
                            invoiceItem.setLineTotal(item.getUnitPrice() * quantity);
                            invoiceItem.setItem(item);

                            items.add(invoiceItem);
                        }
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid quantity
                    continue;
                }
            }

            if (items.isEmpty()) {
                request.setAttribute("error", "No valid items selected");
                showNewInvoiceForm(request, response);
                return;
            }

            Invoice invoice = billingService.createInvoice(customerAccount, items);

            if (invoice != null) {
                response.sendRedirect("billing?action=view&invoiceId=" + invoice.getInvoiceId());
            } else {
                request.setAttribute("error", "Failed to create invoice");
                showNewInvoiceForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error creating invoice: " + e.getMessage());
            showNewInvoiceForm(request, response);
        }
    }

    private void searchItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("query");
        String customerAccount = request.getParameter("customerAccount");

        if (customerAccount == null || customerAccount.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer account is required");
            return;
        }

        try {
            // Get customer to apply proper discount
            Customer customer = billingService.getCustomerByAccountNumber(customerAccount);
            if (customer == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid customer");
                return;
            }

            List<Item> items = billingService.searchItems(query);
            request.setAttribute("items", items != null ? items : new ArrayList<Item>());

            // Forward to a JSP that only contains the search results fragment
            request.getRequestDispatcher("/WEB-INF/views/billing/search_results.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("items", new ArrayList<Item>());
            request.getRequestDispatcher("/WEB-INF/views/billing/search_results.jsp").forward(request, response);
        }
    }
}