package com.pahanaedu.service;

import com.pahanaedu.dao.ItemDAO;
import com.pahanaedu.model.Item;
import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ItemServiceTest {

    @Mock
    private ItemDAO mockItemDao;

    private ItemService itemService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        itemService = new ItemService();

        try {
            java.lang.reflect.Field field = ItemService.class.getDeclaredField("itemDao");
            field.setAccessible(true);
            field.set(itemService, mockItemDao);
        } catch (Exception e) {
            fail("Failed to inject mock DAO: " + e.getMessage());
        }
    }

    @Test
    public void testAddValidItem() {
        Item item = TestDataProvider.getValidItem();
        when(mockItemDao.addItem(item)).thenReturn(true);

        boolean result = itemService.addItem(item);

        assertTrue("Should successfully add valid item", result);
        verify(mockItemDao, times(1)).addItem(item);
    }

    @Test
    public void testAddItemFailure() {
        Item item = TestDataProvider.getValidItem();
        when(mockItemDao.addItem(item)).thenReturn(false);

        boolean result = itemService.addItem(item);

        assertFalse("Should return false when DAO fails", result);
        verify(mockItemDao, times(1)).addItem(item);
    }

    @Test
    public void testUpdateValidItem() {
        Item item = TestDataProvider.getItemForUpdate();
        when(mockItemDao.updateItem(item)).thenReturn(true);

        boolean result = itemService.updateItem(item);

        assertTrue("Should successfully update valid item", result);
        verify(mockItemDao, times(1)).updateItem(item);
    }

    @Test
    public void testUpdateItemFailure() {
        Item item = TestDataProvider.getItemForUpdate();
        when(mockItemDao.updateItem(item)).thenReturn(false);

        boolean result = itemService.updateItem(item);

        assertFalse("Should return false when update fails", result);
        verify(mockItemDao, times(1)).updateItem(item);
    }

    @Test
    public void testGetItemById() throws SQLException {
        String itemId = "ITM00001";
        Item expectedItem = TestDataProvider.getValidItem();
        when(mockItemDao.getItemById(itemId)).thenReturn(expectedItem);

        Item result = itemService.getItemById(itemId);

        assertNotNull("Item should not be null", result);
        assertEquals("Item IDs should match", itemId, result.getItemId());
        assertEquals("Names should match", expectedItem.getName(), result.getName());
        verify(mockItemDao, times(1)).getItemById(itemId);
    }

    @Test
    public void testGetItemByIdNotFound() throws SQLException {
        String itemId = "INVALID001";
        when(mockItemDao.getItemById(itemId)).thenReturn(null);

        Item result = itemService.getItemById(itemId);

        assertNull("Should return null for non-existent item", result);
        verify(mockItemDao, times(1)).getItemById(itemId);
    }

    @Test
    public void testGetAllItems() {
        List<Item> expectedItems = Arrays.asList(
                TestDataProvider.getValidItem(),
                TestDataProvider.getValidBookItem()
        );
        when(mockItemDao.getAllItems()).thenReturn(expectedItems);

        List<Item> result = itemService.getAllItems();

        assertNotNull("Item list should not be null", result);
        assertEquals("Should return correct number of items", 2, result.size());
        assertEquals("First item should match", "ITM00001", result.get(0).getItemId());
        assertEquals("Second item should match", "ITM00002", result.get(1).getItemId());
        verify(mockItemDao, times(1)).getAllItems();
    }

    @Test
    public void testGetAllItemsEmptyList() {
        when(mockItemDao.getAllItems()).thenReturn(Arrays.asList());

        List<Item> result = itemService.getAllItems();

        assertNotNull("Item list should not be null", result);
        assertEquals("Should return empty list", 0, result.size());
        verify(mockItemDao, times(1)).getAllItems();
    }

    @Test
    public void testGetAllCategories() {
        // This would test the delegation to CategoryDAO
        // Similar pattern as above
    }

    @Test
    public void testGetItemForBilling() throws SQLException {
        String itemId = "ITM00001";
        Item expectedItem = TestDataProvider.getValidItem();
        when(mockItemDao.getItemById(itemId)).thenReturn(expectedItem);

        Item result = itemService.getItemForBilling(itemId);

        assertNotNull("Item should not be null", result);
        assertEquals("Item IDs should match", itemId, result.getItemId());
        verify(mockItemDao, times(1)).getItemById(itemId);
    }

    @Test
    public void testSearchItems() {
        String query = "Java";
        List<Item> expectedItems = Arrays.asList(TestDataProvider.getValidItem());
        when(mockItemDao.getAllItems()).thenReturn(expectedItems);

        List<Item> result = itemService.searchItems(query);

        assertNotNull("Search results should not be null", result);
        assertTrue("Should return matching items", result.size() > 0);
    }

    @Test
    public void testSearchItemsEmptyQuery() {
        List<Item> expectedItems = Arrays.asList(
                TestDataProvider.getValidItem(),
                TestDataProvider.getValidBookItem()
        );
        when(mockItemDao.getAllItems()).thenReturn(expectedItems);

        List<Item> result = itemService.searchItems(null);

        assertNotNull("Search results should not be null", result);
        assertEquals("Should return all items for null query", 2, result.size());
    }
}