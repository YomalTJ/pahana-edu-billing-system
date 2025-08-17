package com.pahanaedu.controller;

import com.pahanaedu.model.Item;
import com.pahanaedu.service.ItemService;
import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BillItemServletTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private ItemService mockItemService;

    private BillItemServlet billItemServlet;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        billItemServlet = new BillItemServlet();

        // Inject mock ItemService using reflection
        java.lang.reflect.Field field = BillItemServlet.class.getDeclaredField("itemService");
        field.setAccessible(true);
        field.set(billItemServlet, mockItemService);

        // Setup response writer
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);
    }

    @Test
    public void testDoGetWithValidItem() throws Exception {
        // Arrange
        String itemId = "ITM00001";
        Item item = TestDataProvider.getValidItem();

        when(mockRequest.getParameter("itemId")).thenReturn(itemId);
        when(mockItemService.getItemForBilling(itemId)).thenReturn(item);

        // Act
        billItemServlet.doGet(mockRequest, mockResponse);
        writer.flush();

        // Assert
        String json = stringWriter.toString();
        assertTrue("JSON should contain item ID", json.contains(itemId));
        assertTrue("JSON should contain item name", json.contains(item.getName()));
        assertTrue("JSON should contain unit price", json.contains(String.valueOf(item.getUnitPrice())));
        assertTrue("JSON should contain stock quantity", json.contains(String.valueOf(item.getStockQuantity())));
        verify(mockItemService, times(1)).getItemForBilling(itemId);
    }

    @Test
    public void testDoGetWithInvalidItem() throws Exception {
        // Arrange
        String itemId = "INVALID001";

        when(mockRequest.getParameter("itemId")).thenReturn(itemId);
        when(mockItemService.getItemForBilling(itemId)).thenReturn(null);

        // Act
        billItemServlet.doGet(mockRequest, mockResponse);
        writer.flush();

        // Assert
        String json = stringWriter.toString();
        assertTrue("JSON should contain error message", json.contains("error"));
        assertTrue("JSON should indicate item not available", json.contains("not available"));
        verify(mockItemService, times(1)).getItemForBilling(itemId);
    }

    @Test
    public void testDoGetWithMissingItemId() throws Exception {
        // Arrange
        when(mockRequest.getParameter("itemId")).thenReturn(null);

        // Act
        billItemServlet.doGet(mockRequest, mockResponse);
        writer.flush();

        // Assert
        String json = stringWriter.toString();
        assertTrue("JSON should contain error message", json.contains("error"));
        assertTrue("JSON should indicate missing parameter", json.contains("missing"));
        verify(mockItemService, never()).getItemForBilling(anyString());
    }
}