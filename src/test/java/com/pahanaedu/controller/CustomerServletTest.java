package com.pahanaedu.controller;

import com.pahanaedu.model.Customer;
import com.pahanaedu.model.CustomerType;
import com.pahanaedu.service.CustomerService;
import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CustomerServletTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private RequestDispatcher mockDispatcher;

    @Mock
    private CustomerService mockCustomerService;

    private CustomerServlet customerServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customerServlet = new CustomerServlet();

        // Inject mock CustomerService using reflection
        try {
            java.lang.reflect.Field field = CustomerServlet.class.getDeclaredField("customerService");
            field.setAccessible(true);
            field.set(customerServlet, mockCustomerService);
        } catch (Exception e) {
            fail("Failed to inject mock CustomerService: " + e.getMessage());
        }
    }

    @Test
    public void testDoGetListCustomersDefault() throws ServletException, IOException {
        // Arrange
        List<Customer> customers = Arrays.asList(
                TestDataProvider.getValidCustomer(),
                TestDataProvider.getValidPremiumCustomer()
        );
        when(mockRequest.getParameter("action")).thenReturn(null);
        when(mockCustomerService.getAllCustomers()).thenReturn(customers);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/customer/list.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        customerServlet.doGet(mockRequest, mockResponse);

        // Assert
        verify(mockCustomerService, times(1)).getAllCustomers();
        verify(mockRequest, times(1)).setAttribute("customers", customers);
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoGetListCustomersExplicit() throws ServletException, IOException {
        // Arrange
        List<Customer> customers = Arrays.asList(TestDataProvider.getValidCustomer());
        when(mockRequest.getParameter("action")).thenReturn("list");
        when(mockCustomerService.getAllCustomers()).thenReturn(customers);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/customer/list.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        customerServlet.doGet(mockRequest, mockResponse);

        // Assert
        verify(mockCustomerService, times(1)).getAllCustomers();
        verify(mockRequest, times(1)).setAttribute("customers", customers);
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoGetShowNewForm() throws ServletException, IOException {
        // Arrange
        List<CustomerType> customerTypes = TestDataProvider.getAllCustomerTypes();
        when(mockRequest.getParameter("action")).thenReturn("new");
        when(mockCustomerService.getAllCustomerTypes()).thenReturn(customerTypes);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/customer/form.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        customerServlet.doGet(mockRequest, mockResponse);

        // Assert
        verify(mockCustomerService, times(1)).getAllCustomerTypes();
        verify(mockRequest, times(1)).setAttribute("customerTypes", customerTypes);
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoGetShowEditForm() throws ServletException, IOException {
        // Arrange
        String accountNumber = "ACC00001";
        Customer customer = TestDataProvider.getValidCustomer();
        List<CustomerType> customerTypes = TestDataProvider.getAllCustomerTypes();

        when(mockRequest.getParameter("action")).thenReturn("edit");
        when(mockRequest.getParameter("accountNumber")).thenReturn(accountNumber);
        when(mockCustomerService.getCustomerByAccountNumber(accountNumber)).thenReturn(customer);
        when(mockCustomerService.getAllCustomerTypes()).thenReturn(customerTypes);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/customer/form.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        customerServlet.doGet(mockRequest, mockResponse);

        // Assert
        verify(mockCustomerService, times(1)).getCustomerByAccountNumber(accountNumber);
        verify(mockCustomerService, times(1)).getAllCustomerTypes();
        verify(mockRequest, times(1)).setAttribute("customer", customer);
        verify(mockRequest, times(1)).setAttribute("customerTypes", customerTypes);
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostAddCustomerSuccess() throws ServletException, IOException {
        // Arrange
        Customer customer = TestDataProvider.getValidCustomer();
        List<Customer> customers = Arrays.asList(customer);

        when(mockRequest.getParameter("action")).thenReturn("add");
        when(mockRequest.getParameter("accountNumber")).thenReturn(customer.getAccountNumber());
        when(mockRequest.getParameter("name")).thenReturn(customer.getName());
        when(mockRequest.getParameter("address")).thenReturn(customer.getAddress());
        when(mockRequest.getParameter("telephone")).thenReturn(customer.getTelephone());
        when(mockRequest.getParameter("email")).thenReturn(customer.getEmail());
        when(mockRequest.getParameter("customerType")).thenReturn(customer.getCustomerType());

        when(mockCustomerService.addCustomer(any(Customer.class))).thenReturn(true);
        when(mockCustomerService.getAllCustomers()).thenReturn(customers);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/customer/list.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        customerServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockCustomerService, times(1)).addCustomer(any(Customer.class));
        verify(mockRequest, times(1)).setAttribute("message", "Customer added successfully");
        verify(mockCustomerService, times(1)).getAllCustomers();
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostAddCustomerFailure() throws ServletException, IOException {
        // Arrange
        Customer customer = TestDataProvider.getValidCustomer();

        when(mockRequest.getParameter("action")).thenReturn("add");
        when(mockRequest.getParameter("accountNumber")).thenReturn(customer.getAccountNumber());
        when(mockRequest.getParameter("name")).thenReturn(customer.getName());
        when(mockRequest.getParameter("address")).thenReturn(customer.getAddress());
        when(mockRequest.getParameter("telephone")).thenReturn(customer.getTelephone());
        when(mockRequest.getParameter("email")).thenReturn(customer.getEmail());
        when(mockRequest.getParameter("customerType")).thenReturn(customer.getCustomerType());

        when(mockCustomerService.addCustomer(any(Customer.class))).thenReturn(false);
        when(mockCustomerService.getAllCustomers()).thenReturn(Arrays.asList());
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/customer/list.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        customerServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockCustomerService, times(1)).addCustomer(any(Customer.class));
        verify(mockRequest, times(1)).setAttribute("error", "Failed to add customer");
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostUpdateCustomerSuccess() throws ServletException, IOException {
        // Arrange
        String accountNumber = "ACC00001";
        Customer existingCustomer = TestDataProvider.getValidCustomer();
        Customer updatedCustomer = TestDataProvider.getCustomerForUpdate();

        when(mockRequest.getParameter("action")).thenReturn("update");
        when(mockRequest.getParameter("accountNumber")).thenReturn(accountNumber);
        when(mockRequest.getParameter("name")).thenReturn(updatedCustomer.getName());
        when(mockRequest.getParameter("address")).thenReturn(updatedCustomer.getAddress());
        when(mockRequest.getParameter("telephone")).thenReturn(updatedCustomer.getTelephone());
        when(mockRequest.getParameter("email")).thenReturn(updatedCustomer.getEmail());
        when(mockRequest.getParameter("customerType")).thenReturn(updatedCustomer.getCustomerType());
        when(mockRequest.getParameter("status")).thenReturn(updatedCustomer.getStatus());

        when(mockCustomerService.getCustomerByAccountNumber(accountNumber)).thenReturn(existingCustomer);
        when(mockCustomerService.updateCustomer(any(Customer.class))).thenReturn(true);
        when(mockCustomerService.getAllCustomers()).thenReturn(Arrays.asList(existingCustomer));
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/customer/list.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        customerServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockCustomerService, times(1)).getCustomerByAccountNumber(accountNumber);
        verify(mockCustomerService, times(1)).updateCustomer(any(Customer.class));
        verify(mockRequest, times(1)).setAttribute("message", "Customer updated successfully");
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostUpdateCustomerFailure() throws ServletException, IOException {
        // Arrange
        String accountNumber = "ACC00001";
        Customer existingCustomer = TestDataProvider.getValidCustomer();

        when(mockRequest.getParameter("action")).thenReturn("update");
        when(mockRequest.getParameter("accountNumber")).thenReturn(accountNumber);
        when(mockRequest.getParameter("name")).thenReturn("Updated Name");
        when(mockRequest.getParameter("address")).thenReturn("Updated Address");
        when(mockRequest.getParameter("telephone")).thenReturn("0777777777");
        when(mockRequest.getParameter("email")).thenReturn("updated@email.com");
        when(mockRequest.getParameter("customerType")).thenReturn("PREMIUM");
        when(mockRequest.getParameter("status")).thenReturn("ACTIVE");

        when(mockCustomerService.getCustomerByAccountNumber(accountNumber)).thenReturn(existingCustomer);
        when(mockCustomerService.updateCustomer(any(Customer.class))).thenReturn(false);
        when(mockCustomerService.getAllCustomers()).thenReturn(Arrays.asList(existingCustomer));
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/customer/list.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        customerServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockCustomerService, times(1)).getCustomerByAccountNumber(accountNumber);
        verify(mockCustomerService, times(1)).updateCustomer(any(Customer.class));
        verify(mockRequest, times(1)).setAttribute("error", "Failed to update customer");
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostUpdateCustomerNotFound() throws ServletException, IOException {
        // Arrange
        String accountNumber = "INVALID001";

        when(mockRequest.getParameter("action")).thenReturn("update");
        when(mockRequest.getParameter("accountNumber")).thenReturn(accountNumber);
        when(mockCustomerService.getCustomerByAccountNumber(accountNumber)).thenReturn(null);
        when(mockCustomerService.getAllCustomers()).thenReturn(Arrays.asList());
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/customer/list.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        customerServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockCustomerService, times(1)).getCustomerByAccountNumber(accountNumber);
        verify(mockCustomerService, never()).updateCustomer(any(Customer.class));
        verify(mockRequest, times(1)).setAttribute("error", "Customer not found");
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostInvalidAction() throws ServletException, IOException {
        // Arrange
        when(mockRequest.getParameter("action")).thenReturn("invalid");
        when(mockCustomerService.getAllCustomers()).thenReturn(Arrays.asList());
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/customer/list.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        customerServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockCustomerService, times(1)).getAllCustomers();
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostNullAction() throws ServletException, IOException {
        // Arrange
        when(mockRequest.getParameter("action")).thenReturn(null);
        when(mockCustomerService.getAllCustomers()).thenReturn(Arrays.asList());
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/customer/list.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        customerServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockCustomerService, times(1)).getAllCustomers();
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testAddCustomerWithNullParameters() throws ServletException, IOException {
        // Arrange
        when(mockRequest.getParameter("action")).thenReturn("add");
        when(mockRequest.getParameter("accountNumber")).thenReturn(null);
        when(mockRequest.getParameter("name")).thenReturn(null);
        when(mockRequest.getParameter("address")).thenReturn(null);
        when(mockRequest.getParameter("telephone")).thenReturn(null);
        when(mockRequest.getParameter("email")).thenReturn(null);
        when(mockRequest.getParameter("customerType")).thenReturn(null);

        when(mockCustomerService.addCustomer(any(Customer.class))).thenReturn(false);
        when(mockCustomerService.getAllCustomers()).thenReturn(Arrays.asList());
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/customer/list.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        customerServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockCustomerService, times(1)).addCustomer(any(Customer.class));
        verify(mockRequest, times(1)).setAttribute("error", "Failed to add customer");
    }

    @Test
    public void testAddCustomerWithEmptyParameters() throws ServletException, IOException {
        // Arrange
        when(mockRequest.getParameter("action")).thenReturn("add");
        when(mockRequest.getParameter("accountNumber")).thenReturn("");
        when(mockRequest.getParameter("name")).thenReturn("");
        when(mockRequest.getParameter("address")).thenReturn("");
        when(mockRequest.getParameter("telephone")).thenReturn("");
        when(mockRequest.getParameter("email")).thenReturn("");
        when(mockRequest.getParameter("customerType")).thenReturn("");

        when(mockCustomerService.addCustomer(any(Customer.class))).thenReturn(false);
        when(mockCustomerService.getAllCustomers()).thenReturn(Arrays.asList());
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/customer/list.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        customerServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockCustomerService, times(1)).addCustomer(any(Customer.class));
        verify(mockRequest, times(1)).setAttribute("error", "Failed to add customer");
    }
}