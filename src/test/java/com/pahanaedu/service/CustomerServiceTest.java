package com.pahanaedu.service;

import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.model.Customer;
import com.pahanaedu.model.CustomerType;
import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Arrays;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerDAO mockCustomerDao;

    private CustomerService customerService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerService();

        // Inject mock DAO using reflection
        try {
            java.lang.reflect.Field field = CustomerService.class.getDeclaredField("customerDao");
            field.setAccessible(true);
            field.set(customerService, mockCustomerDao);
        } catch (Exception e) {
            fail("Failed to inject mock DAO: " + e.getMessage());
        }
    }

    @Test
    public void testAddValidCustomer() {
        // Arrange
        Customer customer = TestDataProvider.getValidCustomer();
        when(mockCustomerDao.addCustomer(customer)).thenReturn(true);

        // Act
        boolean result = customerService.addCustomer(customer);

        // Assert
        assertTrue("Should successfully add valid customer", result);
        verify(mockCustomerDao, times(1)).addCustomer(customer);
    }

    @Test
    public void testAddCustomerFailure() {
        // Arrange
        Customer customer = TestDataProvider.getValidCustomer();
        when(mockCustomerDao.addCustomer(customer)).thenReturn(false);

        // Act
        boolean result = customerService.addCustomer(customer);

        // Assert
        assertFalse("Should return false when DAO fails", result);
        verify(mockCustomerDao, times(1)).addCustomer(customer);
    }

    @Test
    public void testUpdateValidCustomer() {
        // Arrange
        Customer customer = TestDataProvider.getCustomerForUpdate();
        when(mockCustomerDao.updateCustomer(customer)).thenReturn(true);

        // Act
        boolean result = customerService.updateCustomer(customer);

        // Assert
        assertTrue("Should successfully update valid customer", result);
        verify(mockCustomerDao, times(1)).updateCustomer(customer);
    }

    @Test
    public void testUpdateCustomerFailure() {
        // Arrange
        Customer customer = TestDataProvider.getCustomerForUpdate();
        when(mockCustomerDao.updateCustomer(customer)).thenReturn(false);

        // Act
        boolean result = customerService.updateCustomer(customer);

        // Assert
        assertFalse("Should return false when update fails", result);
        verify(mockCustomerDao, times(1)).updateCustomer(customer);
    }

    @Test
    public void testGetCustomerByAccountNumber() {
        // Arrange
        String accountNumber = "ACC00001";
        Customer expectedCustomer = TestDataProvider.getValidCustomer();
        when(mockCustomerDao.getCustomerByAccountNumber(accountNumber)).thenReturn(expectedCustomer);

        // Act
        Customer result = customerService.getCustomerByAccountNumber(accountNumber);

        // Assert
        assertNotNull("Customer should not be null", result);
        assertEquals("Account numbers should match", accountNumber, result.getAccountNumber());
        assertEquals("Names should match", expectedCustomer.getName(), result.getName());
        verify(mockCustomerDao, times(1)).getCustomerByAccountNumber(accountNumber);
    }

    @Test
    public void testGetCustomerByAccountNumberNotFound() {
        // Arrange
        String accountNumber = "INVALID001";
        when(mockCustomerDao.getCustomerByAccountNumber(accountNumber)).thenReturn(null);

        // Act
        Customer result = customerService.getCustomerByAccountNumber(accountNumber);

        // Assert
        assertNull("Should return null for non-existent customer", result);
        verify(mockCustomerDao, times(1)).getCustomerByAccountNumber(accountNumber);
    }

    @Test
    public void testGetAllCustomers() {
        // Arrange
        List<Customer> expectedCustomers = Arrays.asList(
                TestDataProvider.getValidCustomer(),
                TestDataProvider.getValidPremiumCustomer()
        );
        when(mockCustomerDao.getAllCustomers()).thenReturn(expectedCustomers);

        // Act
        List<Customer> result = customerService.getAllCustomers();

        // Assert
        assertNotNull("Customer list should not be null", result);
        assertEquals("Should return correct number of customers", 2, result.size());
        assertEquals("First customer should match", "ACC00001", result.get(0).getAccountNumber());
        assertEquals("Second customer should match", "ACC00002", result.get(1).getAccountNumber());
        verify(mockCustomerDao, times(1)).getAllCustomers();
    }

    @Test
    public void testGetAllCustomersEmptyList() {
        // Arrange
        when(mockCustomerDao.getAllCustomers()).thenReturn(Arrays.asList());

        // Act
        List<Customer> result = customerService.getAllCustomers();

        // Assert
        assertNotNull("Customer list should not be null", result);
        assertEquals("Should return empty list", 0, result.size());
        verify(mockCustomerDao, times(1)).getAllCustomers();
    }

    @Test
    public void testGetAllCustomerTypes() {
        // Arrange
        List<CustomerType> expectedTypes = TestDataProvider.getAllCustomerTypes();
        when(mockCustomerDao.getAllCustomerTypes()).thenReturn(expectedTypes);

        // Act
        List<CustomerType> result = customerService.getAllCustomerTypes();

        // Assert
        assertNotNull("Customer types list should not be null", result);
        assertEquals("Should return correct number of types", 3, result.size());
        assertEquals("First type should be REGULAR", "REGULAR", result.get(0).getTypeName());
        assertEquals("Second type should be PREMIUM", "PREMIUM", result.get(1).getTypeName());
        assertEquals("Third type should be VIP", "VIP", result.get(2).getTypeName());
        verify(mockCustomerDao, times(1)).getAllCustomerTypes();
    }

    @Test
    public void testAddNullCustomer() {
        // Arrange
        when(mockCustomerDao.addCustomer(null)).thenReturn(false);

        // Act
        boolean result = customerService.addCustomer(null);

        // Assert
        assertFalse("Should handle null customer gracefully", result);
        verify(mockCustomerDao, times(1)).addCustomer(null);
    }

    @Test
    public void testUpdateNullCustomer() {
        // Arrange
        when(mockCustomerDao.updateCustomer(null)).thenReturn(false);

        // Act
        boolean result = customerService.updateCustomer(null);

        // Assert
        assertFalse("Should handle null customer gracefully", result);
        verify(mockCustomerDao, times(1)).updateCustomer(null);
    }
}