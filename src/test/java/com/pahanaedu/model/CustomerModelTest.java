package com.pahanaedu.model;

import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import java.util.Date;
import static org.junit.Assert.*;

public class CustomerModelTest {

    private Customer customer;

    @Before
    public void setUp() {
        customer = new Customer();
    }

    @Test
    public void testCustomerConstructor() {
        // Test that a new Customer can be created
        Customer newCustomer = new Customer();
        assertNotNull("Customer object should be created", newCustomer);
    }

    @Test
    public void testSetAndGetAccountNumber() {
        // Arrange
        String accountNumber = "ACC00001";

        // Act
        customer.setAccountNumber(accountNumber);

        // Assert
        assertEquals("Account number should match", accountNumber, customer.getAccountNumber());
    }

    @Test
    public void testSetAndGetName() {
        // Arrange
        String name = "John Doe";

        // Act
        customer.setName(name);

        // Assert
        assertEquals("Name should match", name, customer.getName());
    }

    @Test
    public void testSetAndGetAddress() {
        // Arrange
        String address = "123 Main Street, Colombo 03";

        // Act
        customer.setAddress(address);

        // Assert
        assertEquals("Address should match", address, customer.getAddress());
    }

    @Test
    public void testSetAndGetTelephone() {
        // Arrange
        String telephone = "0771234567";

        // Act
        customer.setTelephone(telephone);

        // Assert
        assertEquals("Telephone should match", telephone, customer.getTelephone());
    }

    @Test
    public void testSetAndGetEmail() {
        // Arrange
        String email = "john.doe@email.com";

        // Act
        customer.setEmail(email);

        // Assert
        assertEquals("Email should match", email, customer.getEmail());
    }

    @Test
    public void testSetAndGetRegistrationDate() {
        // Arrange
        Date registrationDate = new Date();

        // Act
        customer.setRegistrationDate(registrationDate);

        // Assert
        assertEquals("Registration date should match", registrationDate, customer.getRegistrationDate());
    }

    @Test
    public void testSetAndGetCustomerType() {
        // Arrange
        String customerType = "PREMIUM";

        // Act
        customer.setCustomerType(customerType);

        // Assert
        assertEquals("Customer type should match", customerType, customer.getCustomerType());
    }

    @Test
    public void testSetAndGetStatus() {
        // Arrange
        String status = "ACTIVE";

        // Act
        customer.setStatus(status);

        // Assert
        assertEquals("Status should match", status, customer.getStatus());
    }

    @Test
    public void testSetAndGetLastPurchaseDate() {
        // Arrange
        Date lastPurchaseDate = new Date();

        // Act
        customer.setLastPurchaseDate(lastPurchaseDate);

        // Assert
        assertEquals("Last purchase date should match", lastPurchaseDate, customer.getLastPurchaseDate());
    }

    @Test
    public void testCustomerWithNullValues() {
        // Test that Customer can handle null values
        customer.setAccountNumber(null);
        customer.setName(null);
        customer.setAddress(null);
        customer.setTelephone(null);
        customer.setEmail(null);
        customer.setRegistrationDate(null);
        customer.setCustomerType(null);
        customer.setStatus(null);
        customer.setLastPurchaseDate(null);

        // Assert all getters return null
        assertNull("Account number should be null", customer.getAccountNumber());
        assertNull("Name should be null", customer.getName());
        assertNull("Address should be null", customer.getAddress());
        assertNull("Telephone should be null", customer.getTelephone());
        assertNull("Email should be null", customer.getEmail());
        assertNull("Registration date should be null", customer.getRegistrationDate());
        assertNull("Customer type should be null", customer.getCustomerType());
        assertNull("Status should be null", customer.getStatus());
        assertNull("Last purchase date should be null", customer.getLastPurchaseDate());
    }

    @Test
    public void testCustomerWithEmptyStrings() {
        // Test that Customer can handle empty strings
        customer.setAccountNumber("");
        customer.setName("");
        customer.setAddress("");
        customer.setTelephone("");
        customer.setEmail("");
        customer.setCustomerType("");
        customer.setStatus("");

        // Assert all getters return empty strings
        assertEquals("Account number should be empty", "", customer.getAccountNumber());
        assertEquals("Name should be empty", "", customer.getName());
        assertEquals("Address should be empty", "", customer.getAddress());
        assertEquals("Telephone should be empty", "", customer.getTelephone());
        assertEquals("Email should be empty", "", customer.getEmail());
        assertEquals("Customer type should be empty", "", customer.getCustomerType());
        assertEquals("Status should be empty", "", customer.getStatus());
    }

    @Test
    public void testCustomerWithValidData() {
        // Test setting all valid data
        Customer testCustomer = TestDataProvider.getValidCustomer();

        customer.setAccountNumber(testCustomer.getAccountNumber());
        customer.setName(testCustomer.getName());
        customer.setAddress(testCustomer.getAddress());
        customer.setTelephone(testCustomer.getTelephone());
        customer.setEmail(testCustomer.getEmail());
        customer.setRegistrationDate(testCustomer.getRegistrationDate());
        customer.setCustomerType(testCustomer.getCustomerType());
        customer.setStatus(testCustomer.getStatus());

        // Assert all values match
        assertEquals("Account number should match", testCustomer.getAccountNumber(), customer.getAccountNumber());
        assertEquals("Name should match", testCustomer.getName(), customer.getName());
        assertEquals("Address should match", testCustomer.getAddress(), customer.getAddress());
        assertEquals("Telephone should match", testCustomer.getTelephone(), customer.getTelephone());
        assertEquals("Email should match", testCustomer.getEmail(), customer.getEmail());
        assertEquals("Registration date should match", testCustomer.getRegistrationDate(), customer.getRegistrationDate());
        assertEquals("Customer type should match", testCustomer.getCustomerType(), customer.getCustomerType());
        assertEquals("Status should match", testCustomer.getStatus(), customer.getStatus());
    }

    @Test
    public void testCustomerTypeModel() {
        // Test CustomerType model
        CustomerType customerType = new CustomerType();

        // Test setters and getters
        customerType.setTypeId(1);
        customerType.setTypeName("PREMIUM");
        customerType.setDiscountPercentage(10.0);

        assertEquals("Type ID should match", 1, customerType.getTypeId());
        assertEquals("Type name should match", "PREMIUM", customerType.getTypeName());
        assertEquals("Discount percentage should match", 10.0, customerType.getDiscountPercentage(), 0.01);
    }

    @Test
    public void testCustomerTypeWithNullValues() {
        // Test CustomerType with null values
        CustomerType customerType = new CustomerType();

        customerType.setTypeName(null);

        assertNull("Type name should be null", customerType.getTypeName());
        assertEquals("Type ID should default to 0", 0, customerType.getTypeId());
        assertEquals("Discount percentage should default to 0.0", 0.0, customerType.getDiscountPercentage(), 0.01);
    }

    @Test
    public void testCustomerTypeWithValidData() {
        // Test CustomerType with valid data
        CustomerType testType = TestDataProvider.getPremiumCustomerType();
        CustomerType customerType = new CustomerType();

        customerType.setTypeId(testType.getTypeId());
        customerType.setTypeName(testType.getTypeName());
        customerType.setDiscountPercentage(testType.getDiscountPercentage());

        assertEquals("Type ID should match", testType.getTypeId(), customerType.getTypeId());
        assertEquals("Type name should match", testType.getTypeName(), customerType.getTypeName());
        assertEquals("Discount percentage should match", testType.getDiscountPercentage(),
                customerType.getDiscountPercentage(), 0.01);
    }
}