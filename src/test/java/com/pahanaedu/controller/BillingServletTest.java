package com.pahanaedu.controller;

import com.pahanaedu.model.*;
import com.pahanaedu.service.BillingService;
import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BillingServletTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private RequestDispatcher mockDispatcher;

    @Mock
    private BillingService mockBillingService;

    private BillingServlet billingServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        billingServlet = new BillingServlet();

        try {
            java.lang.reflect.Field field = BillingServlet.class.getDeclaredField("billingService");
            field.setAccessible(true);
            field.set(billingServlet, mockBillingService);
        } catch (Exception e) {
            fail("Failed to inject mock BillingService: " + e.getMessage());
        }
    }

    @Test
    public void testDoGetShowNewInvoiceForm() throws ServletException, IOException {
        // Arrange
        List<Customer> customers = Arrays.asList(TestDataProvider.getValidCustomer());
        when(mockRequest.getParameter("action")).thenReturn("new");
        when(mockBillingService.getAllCustomers()).thenReturn(customers);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/billing/new.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        billingServlet.doGet(mockRequest, mockResponse);

        // Assert
        verify(mockBillingService, times(1)).getAllCustomers();
        verify(mockRequest, times(1)).setAttribute("customers", customers);
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoGetViewInvoice() throws ServletException, IOException {
        // Arrange
        String invoiceId = "INV00001";
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);

        when(mockRequest.getParameter("action")).thenReturn("view");
        when(mockRequest.getParameter("invoiceId")).thenReturn(invoiceId);
        when(mockBillingService.getInvoiceById(invoiceId)).thenReturn(invoice);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/billing/view.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        billingServlet.doGet(mockRequest, mockResponse);

        // Assert
        verify(mockBillingService, times(1)).getInvoiceById(invoiceId);
        verify(mockRequest, times(1)).setAttribute("invoice", invoice);
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoGetViewCustomerHistory() throws ServletException, IOException {
        // Arrange
        String accountNumber = "ACC00001";
        Customer customer = TestDataProvider.getValidCustomer();
        List<Invoice> invoices = Arrays.asList(new Invoice());

        when(mockRequest.getParameter("action")).thenReturn("history");
        when(mockRequest.getParameter("accountNumber")).thenReturn(accountNumber);
        when(mockBillingService.getCustomerByAccountNumber(accountNumber)).thenReturn(customer);
        when(mockBillingService.getInvoicesByCustomer(accountNumber)).thenReturn(invoices);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/billing/history.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        billingServlet.doGet(mockRequest, mockResponse);

        // Assert
        verify(mockBillingService, times(1)).getCustomerByAccountNumber(accountNumber);
        verify(mockBillingService, times(1)).getInvoicesByCustomer(accountNumber);
        verify(mockRequest, times(1)).setAttribute("customer", customer);
        verify(mockRequest, times(1)).setAttribute("invoices", invoices);
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostCreateInvoiceSuccess() throws ServletException, IOException, SQLException {
        // Arrange
        String customerAccount = "ACC00001";
        String[] itemIds = {"ITM00001"};
        String[] quantities = {"2"};

        Customer customer = TestDataProvider.getValidCustomer();
        Item item = TestDataProvider.getValidItem();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId("INV00001");

        when(mockRequest.getParameter("action")).thenReturn("create");
        when(mockRequest.getParameter("customerAccount")).thenReturn(customerAccount);
        when(mockRequest.getParameterValues("itemId")).thenReturn(itemIds);
        when(mockRequest.getParameterValues("quantity")).thenReturn(quantities);
        when(mockBillingService.getCustomerByAccountNumber(customerAccount)).thenReturn(customer);
        when(mockBillingService.getItemById(itemIds[0])).thenReturn(item);
        when(mockBillingService.createInvoice(eq(customerAccount), anyList())).thenReturn(invoice);

        // Act
        billingServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockBillingService, times(1)).getCustomerByAccountNumber(customerAccount);
        verify(mockBillingService, times(1)).getItemById(itemIds[0]);
        verify(mockBillingService, times(1)).createInvoice(eq(customerAccount), anyList());
        verify(mockResponse, times(1)).sendRedirect("billing?action=view&invoiceId=INV00001");
    }

    @Test
    public void testDoPostCreateInvoiceWithInvalidCustomer() throws ServletException, IOException {
        // Arrange
        String customerAccount = "INVALID001";

        when(mockRequest.getParameter("action")).thenReturn("create");
        when(mockRequest.getParameter("customerAccount")).thenReturn(customerAccount);
        when(mockBillingService.getCustomerByAccountNumber(customerAccount)).thenReturn(null);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/billing/new.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        billingServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockBillingService, times(1)).getCustomerByAccountNumber(customerAccount);
        verify(mockRequest, times(1)).setAttribute("error", "Customer not found");
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }
}