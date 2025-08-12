package com.pahanaedu.controller;

import com.pahanaedu.model.Customer;
import com.pahanaedu.model.CustomerType;
import com.pahanaedu.service.CustomerService;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "CustomerServlet", value = "/customers")
public class CustomerServlet extends HttpServlet {
    private CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "list":
            default:
                listCustomers(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addCustomer(request, response);
        } else if ("update".equals(action)) {
            updateCustomer(request, response);
        } else {
            listCustomers(request, response);
        }
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Customer> customers = customerService.getAllCustomers();
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/WEB-INF/views/customer/list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<CustomerType> customerTypes = customerService.getAllCustomerTypes();
        request.setAttribute("customerTypes", customerTypes);
        request.getRequestDispatcher("/WEB-INF/views/customer/form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNumber");
        Customer customer = customerService.getCustomerByAccountNumber(accountNumber);
        List<CustomerType> customerTypes = customerService.getAllCustomerTypes();

        request.setAttribute("customer", customer);
        request.setAttribute("customerTypes", customerTypes);
        request.getRequestDispatcher("/WEB-INF/views/customer/form.jsp").forward(request, response);
    }

    private void addCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Customer customer = new Customer();
        customer.setAccountNumber(request.getParameter("accountNumber"));
        customer.setName(request.getParameter("name"));
        customer.setAddress(request.getParameter("address"));
        customer.setTelephone(request.getParameter("telephone"));
        customer.setEmail(request.getParameter("email"));
        customer.setRegistrationDate(new Date());
        customer.setCustomerType(request.getParameter("customerType"));
        customer.setStatus("ACTIVE");

        if (customerService.addCustomer(customer)) {
            request.setAttribute("message", "Customer added successfully");
        } else {
            request.setAttribute("error", "Failed to add customer");
        }

        listCustomers(request, response);
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNumber");
        Customer customer = customerService.getCustomerByAccountNumber(accountNumber);

        if (customer != null) {
            customer.setName(request.getParameter("name"));
            customer.setAddress(request.getParameter("address"));
            customer.setTelephone(request.getParameter("telephone"));
            customer.setEmail(request.getParameter("email"));
            customer.setCustomerType(request.getParameter("customerType"));
            customer.setStatus(request.getParameter("status"));

            if (customerService.updateCustomer(customer)) {
                request.setAttribute("message", "Customer updated successfully");
            } else {
                request.setAttribute("error", "Failed to update customer");
            }
        } else {
            request.setAttribute("error", "Customer not found");
        }

        listCustomers(request, response);
    }
}