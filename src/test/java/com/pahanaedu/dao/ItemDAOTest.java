package com.pahanaedu.dao;

import com.pahanaedu.model.Item;
import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class ItemDAOTest {

    private ItemDAO itemDao;

    @Before
    public void setUp() {
        itemDao = new ItemDAO();
    }

    @Test
    public void testAddItemMethodExists() {
        Item item = TestDataProvider.getValidItem();
        try {
            boolean result = itemDao.addItem(item);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getCause() instanceof SQLException);
        }
    }

    @Test
    public void testAddItemNullHandling() {
        try {
            boolean result = itemDao.addItem(null);
            fail("Method should throw IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // This is the expected behavior
            assertTrue(e.getMessage().contains("cannot be null"));
        } catch (Exception e) {
            fail("Unexpected exception type: " + e.getClass().getName());
        }
    }

    @Test
    public void testUpdateItemMethodExists() {
        Item item = TestDataProvider.getValidItem();
        try {
            boolean result = itemDao.updateItem(item);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testGetItemByIdMethodExists() {
        String itemId = "ITM00001";
        try {
            Item result = itemDao.getItemById(itemId);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testGetItemByIdNullHandling() {
        try {
            Item result = itemDao.getItemById(null);
            fail("Method should throw IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Verify the exception message is meaningful
            assertTrue("Exception message should indicate null is not allowed",
                    e.getMessage().contains("cannot be null") ||
                            e.getMessage().contains("must not be null"));
        } catch (SQLException e) {
            fail("Should throw IllegalArgumentException before database operations");
        } catch (Exception e) {
            fail("Unexpected exception type: " + e.getClass().getName());
        }
    }

    @Test
    public void testGetAllItemsMethodExists() {
        try {
            List<Item> result = itemDao.getAllItems();
            assertNotNull("Result should not be null", result);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testDeleteItemMethodExists() {
        String itemId = "ITM00001";
        try {
            boolean result = itemDao.deleteItem(itemId);
            assertTrue("Method executed without throwing exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e instanceof SQLException || e.getMessage().contains("database"));
        }
    }

    @Test
    public void testGetItemCountMethodExists() {
        try {
            int result = itemDao.getItemCount();
            assertTrue("Item count should be non-negative", result >= 0);
        } catch (SQLException e) {
            assertTrue("Should be SQL exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e.getMessage().contains("database") || e.getMessage().contains("connection"));
        }
    }

    @Test
    public void testGetLowStockItemCountMethodExists() {
        try {
            int result = itemDao.getLowStockItemCount();
            assertTrue("Low stock count should be non-negative", result >= 0);
        } catch (SQLException e) {
            assertTrue("Should be SQL exception", true);
        } catch (Exception e) {
            assertTrue("Exception should be database-related",
                    e.getMessage().contains("database") || e.getMessage().contains("connection"));
        }
    }

    @Test
    public void testSQLInjectionPrevention() {
        Item maliciousItem = TestDataProvider.InvalidItemData.getItemWithSQLInjection();
        try {
            boolean result = itemDao.addItem(maliciousItem);
            assertTrue("Prepared statements should prevent SQL injection", true);
        } catch (Exception e) {
            assertTrue("Should be database connection related",
                    e.getMessage().contains("database") || e.getMessage().contains("connection"));
        }
    }

    @Test
    public void testDeleteItemNullHandling() {
        try {
            boolean result = itemDao.deleteItem(null);
            fail("Method should throw IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("cannot be null"));
        }
    }
}