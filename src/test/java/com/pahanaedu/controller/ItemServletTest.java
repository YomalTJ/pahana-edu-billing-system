package com.pahanaedu.controller;

import com.pahanaedu.model.Item;
import com.pahanaedu.model.Category;
import com.pahanaedu.service.ItemService;
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

public class ItemServletTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private RequestDispatcher mockDispatcher;

    @Mock
    private ItemService mockItemService;

    private ItemServlet itemServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        itemServlet = new ItemServlet();

        try {
            java.lang.reflect.Field field = ItemServlet.class.getDeclaredField("itemService");
            field.setAccessible(true);
            field.set(itemServlet, mockItemService);
        } catch (Exception e) {
            fail("Failed to inject mock ItemService: " + e.getMessage());
        }
    }

    @Test
    public void testDoGetListItemsDefault() throws ServletException, IOException {
        List<Item> items = Arrays.asList(
                TestDataProvider.getValidItem(),
                TestDataProvider.getValidBookItem()
        );
        when(mockRequest.getParameter("action")).thenReturn(null);
        when(mockItemService.getAllItems()).thenReturn(items);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/item/list.jsp"))
                .thenReturn(mockDispatcher);

        itemServlet.doGet(mockRequest, mockResponse);

        verify(mockItemService, times(1)).getAllItems();
        verify(mockRequest, times(1)).setAttribute("items", items);
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoGetShowNewForm() throws ServletException, IOException {
        List<Category> categories = TestDataProvider.getAllCategories();
        when(mockRequest.getParameter("action")).thenReturn("new");
        when(mockItemService.getAllCategories()).thenReturn(categories);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/item/form.jsp"))
                .thenReturn(mockDispatcher);

        itemServlet.doGet(mockRequest, mockResponse);

        verify(mockItemService, times(1)).getAllCategories();
        verify(mockRequest, times(1)).setAttribute("categories", categories);
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoGetShowEditForm() throws ServletException, IOException {
        String itemId = "ITM00001";
        Item item = TestDataProvider.getValidItem();
        List<Category> categories = TestDataProvider.getAllCategories();

        when(mockRequest.getParameter("action")).thenReturn("edit");
        when(mockRequest.getParameter("itemId")).thenReturn(itemId);
        when(mockItemService.getItemById(itemId)).thenReturn(item);
        when(mockItemService.getAllCategories()).thenReturn(categories);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/item/form.jsp"))
                .thenReturn(mockDispatcher);

        itemServlet.doGet(mockRequest, mockResponse);

        verify(mockItemService, times(1)).getItemById(itemId);
        verify(mockItemService, times(1)).getAllCategories();
        verify(mockRequest, times(1)).setAttribute("item", item);
        verify(mockRequest, times(1)).setAttribute("categories", categories);
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostAddItemSuccess() throws ServletException, IOException {
        Item item = TestDataProvider.getValidItem();
        List<Item> items = Arrays.asList(item);

        when(mockRequest.getParameter("action")).thenReturn("add");
        when(mockRequest.getParameter("itemId")).thenReturn(item.getItemId());
        when(mockRequest.getParameter("name")).thenReturn(item.getName());
        when(mockRequest.getParameter("description")).thenReturn(item.getDescription());
        when(mockRequest.getParameter("categoryId")).thenReturn("1");
        when(mockRequest.getParameter("unitPrice")).thenReturn("29.99");
        when(mockRequest.getParameter("stockQuantity")).thenReturn("50");
        when(mockRequest.getParameter("reorderLevel")).thenReturn("10");

        when(mockItemService.addItem(any(Item.class))).thenReturn(true);
        when(mockItemService.getAllItems()).thenReturn(items);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/item/list.jsp"))
                .thenReturn(mockDispatcher);

        itemServlet.doPost(mockRequest, mockResponse);

        verify(mockItemService, times(1)).addItem(any(Item.class));
        verify(mockRequest, times(1)).setAttribute("message", "Item added successfully");
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostUpdateItemSuccess() throws ServletException, IOException {
        String itemId = "ITM00001";
        Item existingItem = TestDataProvider.getValidItem();
        Item updatedItem = TestDataProvider.getItemForUpdate();

        when(mockRequest.getParameter("action")).thenReturn("update");
        when(mockRequest.getParameter("itemId")).thenReturn(itemId);
        when(mockRequest.getParameter("name")).thenReturn(updatedItem.getName());
        when(mockRequest.getParameter("description")).thenReturn(updatedItem.getDescription());
        when(mockRequest.getParameter("categoryId")).thenReturn("1");
        when(mockRequest.getParameter("unitPrice")).thenReturn("39.99");
        when(mockRequest.getParameter("stockQuantity")).thenReturn("60");
        when(mockRequest.getParameter("reorderLevel")).thenReturn("15");

        when(mockItemService.getItemById(itemId)).thenReturn(existingItem);
        when(mockItemService.updateItem(any(Item.class))).thenReturn(true);
        when(mockItemService.getAllItems()).thenReturn(Arrays.asList(existingItem));
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/item/list.jsp"))
                .thenReturn(mockDispatcher);

        itemServlet.doPost(mockRequest, mockResponse);

        verify(mockItemService, times(1)).getItemById(itemId);
        verify(mockItemService, times(1)).updateItem(any(Item.class));
        verify(mockRequest, times(1)).setAttribute("message", "Item updated successfully");
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    // Additional tests for error cases, invalid inputs, etc.
}