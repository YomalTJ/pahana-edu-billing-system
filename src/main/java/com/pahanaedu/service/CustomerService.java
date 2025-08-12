package com.pahanaedu.service;

import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.model.Customer;
import com.pahanaedu.model.CustomerType;
import java.util.List;

public class CustomerService {
    private CustomerDAO customerDao = new CustomerDAO();

    public boolean addCustomer(Customer customer) {
        // You can add business logic/validation here
        return customerDao.addCustomer(customer);
    }

    public boolean updateCustomer(Customer customer) {
        // Business logic/validation
        return customerDao.updateCustomer(customer);
    }

    public Customer getCustomerByAccountNumber(String accountNumber) {
        return customerDao.getCustomerByAccountNumber(accountNumber);
    }

    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    public List<CustomerType> getAllCustomerTypes() {
        return customerDao.getAllCustomerTypes();
    }
}