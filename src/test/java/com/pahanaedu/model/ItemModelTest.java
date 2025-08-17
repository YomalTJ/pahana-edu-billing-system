package com.pahanaedu.model;

import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import static org.junit.Assert.*;

public class ItemModelTest {

    private Item item;

    @Before
    public void setUp() {
        item = new Item();
    }

    @Test
    public void testItemConstructor() {
        assertNotNull("Item object should be created", new Item());
    }

    @Test
    public void testSetAndGetItemId() {
        String itemId = "ITM00001";
        item.setItemId(itemId);
        assertEquals("Item ID should match", itemId, item.getItemId());
    }

    @Test
    public void testSetAndGetName() {
        String name = "Advanced Java Programming";
        item.setName(name);
        assertEquals("Name should match", name, item.getName());
    }

    @Test
    public void testSetAndGetDescription() {
        String description = "Comprehensive guide to Java programming";
        item.setDescription(description);
        assertEquals("Description should match", description, item.getDescription());
    }

    @Test
    public void testSetAndGetCategory() {
        Category category = new Category();
        category.setCategoryId(1);
        category.setCategoryName("Books");
        item.setCategory(category);
        assertEquals("Category should match", category, item.getCategory());
    }

    @Test
    public void testSetAndGetUnitPrice() {
        double unitPrice = 29.99;
        item.setUnitPrice(unitPrice);
        assertEquals("Unit price should match", unitPrice, item.getUnitPrice(), 0.001);
    }

    @Test
    public void testSetAndGetStockQuantity() {
        int stockQuantity = 50;
        item.setStockQuantity(stockQuantity);
        assertEquals("Stock quantity should match", stockQuantity, item.getStockQuantity());
    }

    @Test
    public void testSetAndGetReorderLevel() {
        int reorderLevel = 10;
        item.setReorderLevel(reorderLevel);
        assertEquals("Reorder level should match", reorderLevel, item.getReorderLevel());
    }

    @Test
    public void testItemWithNullValues() {
        item.setItemId(null);
        item.setName(null);
        item.setDescription(null);
        item.setCategory(null);

        assertNull("Item ID should be null", item.getItemId());
        assertNull("Name should be null", item.getName());
        assertNull("Description should be null", item.getDescription());
        assertNull("Category should be null", item.getCategory());
    }

    @Test
    public void testItemWithEmptyStrings() {
        item.setItemId("");
        item.setName("");
        item.setDescription("");

        assertEquals("Item ID should be empty", "", item.getItemId());
        assertEquals("Name should be empty", "", item.getName());
        assertEquals("Description should be empty", "", item.getDescription());
    }

    @Test
    public void testItemWithValidData() {
        Item testItem = TestDataProvider.getValidItem();

        item.setItemId(testItem.getItemId());
        item.setName(testItem.getName());
        item.setDescription(testItem.getDescription());
        item.setCategory(testItem.getCategory());
        item.setUnitPrice(testItem.getUnitPrice());
        item.setStockQuantity(testItem.getStockQuantity());
        item.setReorderLevel(testItem.getReorderLevel());

        assertEquals("Item ID should match", testItem.getItemId(), item.getItemId());
        assertEquals("Name should match", testItem.getName(), item.getName());
        assertEquals("Description should match", testItem.getDescription(), item.getDescription());
        assertEquals("Category should match", testItem.getCategory(), item.getCategory());
        assertEquals("Unit price should match", testItem.getUnitPrice(), item.getUnitPrice(), 0.001);
        assertEquals("Stock quantity should match", testItem.getStockQuantity(), item.getStockQuantity());
        assertEquals("Reorder level should match", testItem.getReorderLevel(), item.getReorderLevel());
    }
}