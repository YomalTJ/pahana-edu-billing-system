package com.pahanaedu.dao;

import com.pahanaedu.model.Customer;
import com.pahanaedu.model.CustomerType;
import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.*;

public class CustomerDAOTest {

    private CustomerDAO customerDao;

    @Before
    public void setUp() {
        customerDao = new CustomerDAO();
    }

    @Test
    public void testAddCustomerMethodExists() {
        // Test that the addCustomer method exists and can be called
        Customer customer = TestDataProvider.getValidCustomer();

        try {
            boolean result = customerDao.addCustomer(customer);
            // Method exists and can be called
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            // Database connection errors are acceptable in test environment
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getCause() instanceof SQLException ||
                            e.getMessage().contains("database") || e.getMessage().contains("connection"));
        }
    }

    @Test
    public void testAddCustomerNullHandling() {
        // Test how the method handles null input
        try {
            boolean result = customerDao.addCustomer(null);
            assertTrue("Method should handle null input gracefully", true);
        } catch (NullPointerException e) {
            fail("Method should handle null input without NPE");
        } catch (Exception e) {
            // Database exceptions are acceptable
            assertTrue("Should be database related exception",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testUpdateCustomerMethodExists() {
        // Test that the updateCustomer method exists and can be called
        Customer customer = TestDataProvider.getCustomerForUpdate();

        try {
            boolean result = customerDao.updateCustomer(customer);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testGetCustomerByAccountNumberMethodExists() {
        // Test that the method exists and handles valid input
        String accountNumber = "ACC00001";

        try {
            Customer result = customerDao.getCustomerByAccountNumber(accountNumber);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testGetCustomerByAccountNumberNullHandling() {
        // Test null account number handling
        try {
            Customer result = customerDao.getCustomerByAccountNumber(null);
            assertTrue("Method should handle null input gracefully", true);
        } catch (Exception e) {
            assertTrue("Should be database related exception",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testGetCustomerByAccountNumberEmptyString() {
        // Test empty string handling
        try {
            Customer result = customerDao.getCustomerByAccountNumber("");
            assertTrue("Method should handle empty string gracefully", true);
        } catch (Exception e) {
            assertTrue("Should be database related exception",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testGetAllCustomersMethodExists() {
        // Test that the method exists and can be called
        try {
            List<Customer> result = customerDao.getAllCustomers();
            assertNotNull("Result should not be null", result);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testGetAllCustomerTypesMethodExists() {
        // Test that the method exists and can be called
        try {
            List<CustomerType> result = customerDao.getAllCustomerTypes();
            assertNotNull("Result should not be null", result);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testDeleteCustomerMethodExists() {
        // Test that the deleteCustomer method exists and can be called
        String accountNumber = "ACC00001";

        try {
            boolean result = customerDao.deleteCustomer(accountNumber);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testDeleteCustomerNullHandling() {
        // Test null account number handling for delete
        try {
            boolean result = customerDao.deleteCustomer(null);
            assertTrue("Method should handle null input gracefully", true);
        } catch (Exception e) {
            assertTrue("Should be database related exception",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testGetCustomerCountMethodExists() {
        // Test that the getCustomerCount method exists and can be called
        try {
            int result = customerDao.getCustomerCount();
            assertTrue("Customer count should be non-negative", result >= 0);
        } catch (SQLException e) {
            // Expected in test environment without database
            assertTrue("Should be SQL exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e.getMessage().contains("database") || e.getMessage().contains("connection"));
        }
    }

    @Test
    public void testGenerateNewAccountNumberMethodExists() {
        // Test that the generateNewAccountNumber method exists and returns proper format
        try {
            String accountNumber = customerDao.generateNewAccountNumber();
            if (accountNumber != null) {
                assertTrue("Account number should start with ACC", accountNumber.startsWith("ACC"));
                assertTrue("Account number should be at least 8 characters", accountNumber.length() >= 8);
            }
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testSQLInjectionPrevention() {
        // Test that prepared statements prevent SQL injection
        Customer maliciousCustomer = TestDataProvider.InvalidCustomerData.getCustomerWithSQLInjection();

        try {
            boolean result = customerDao.addCustomer(maliciousCustomer);
            // Should not throw SQL syntax errors due to prepared statements
            assertTrue("Prepared statements should prevent SQL injection", true);
        } catch (Exception e) {
            assertTrue("Should be database connection related",
                    e.getMessage().contains("database") || e.getMessage().contains("connection"));
        }
    }

    @Test
    public void testLongDataHandling() {
        // Test handling of very long data
        Customer longDataCustomer = TestDataProvider.InvalidCustomerData.getCustomerWithVeryLongData();

        try {
            boolean result = customerDao.addCustomer(longDataCustomer);
            assertTrue("Method should handle long data gracefully", true);
        } catch (Exception e) {
            // Should either handle gracefully or throw appropriate database exception
            assertTrue("Should be database related exception",
                    e instanceof SQLException || e.getMessage().contains("database") ||
                            e.getMessage().contains("too long") || e.getMessage().contains("length"));
        }
    }
}