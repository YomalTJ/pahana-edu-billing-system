package com.pahanaedu.test.integration;

import com.pahanaedu.model.Item;
import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

public class ItemValidationTest {

    private static final Pattern ITEM_ID_PATTERN = Pattern.compile("^ITM\\d{5}$");
    private static final double MIN_PRICE = 0.01;
    private static final double MAX_PRICE = 9999.99;
    private static final int MIN_QUANTITY = 0;
    private static final int MAX_QUANTITY = 9999;

    @Test
    public void testValidItemValidation() {
        Item item = TestDataProvider.getValidItem();
        assertTrue("Valid item should pass validation", validateItem(item));
        assertTrue("Item ID should be valid format",
                ITEM_ID_PATTERN.matcher(item.getItemId()).matches());
        assertTrue("Unit price should be within valid range",
                item.getUnitPrice() >= MIN_PRICE && item.getUnitPrice() <= MAX_PRICE);
        assertTrue("Stock quantity should be within valid range",
                item.getStockQuantity() >= MIN_QUANTITY && item.getStockQuantity() <= MAX_QUANTITY);
    }

    @Test
    public void testItemWithInvalidId() {
        Item item = TestDataProvider.InvalidItemData.getItemWithInvalidId();
        assertFalse("Item with invalid ID should fail validation",
                ITEM_ID_PATTERN.matcher(item.getItemId()).matches());
    }

    @Test
    public void testItemWithNegativePrice() {
        Item item = TestDataProvider.InvalidItemData.getItemWithNegativePrice();
        assertFalse("Item with negative price should fail validation",
                item.getUnitPrice() >= MIN_PRICE);
    }

    @Test
    public void testItemWithNegativeQuantity() {
        Item item = TestDataProvider.InvalidItemData.getItemWithNegativeQuantity();
        assertFalse("Item with negative quantity should fail validation",
                item.getStockQuantity() >= MIN_QUANTITY);
    }

    @Test
    public void testItemWithNullName() {
        Item item = TestDataProvider.InvalidItemData.getItemWithNullName();
        assertFalse("Item with null name should fail validation", validateItem(item));
    }

    @Test
    public void testItemWithEmptyDescription() {
        Item item = TestDataProvider.InvalidItemData.getItemWithEmptyDescription();
        assertTrue("Empty description should be allowed", validateItem(item));
    }

    @Test
    public void testItemIdGeneration() {
        String[] validItemIds = {"ITM00001", "ITM12345", "ITM99999"};
        String[] invalidItemIds = {"ITM123", "ITM123456", "PRD00001", "ITM0000A"};

        for (String valid : validItemIds) {
            assertTrue("Item ID " + valid + " should be valid",
                    ITEM_ID_PATTERN.matcher(valid).matches());
        }

        for (String invalid : invalidItemIds) {
            assertFalse("Item ID " + invalid + " should be invalid",
                    ITEM_ID_PATTERN.matcher(invalid).matches());
        }
    }

    @Test
    public void testPriceValidation() {
        double[] validPrices = {0.01, 10.50, 9999.99};
        double[] invalidPrices = {0.00, -1.00, 10000.00};

        for (double valid : validPrices) {
            assertTrue("Price " + valid + " should be valid",
                    valid >= MIN_PRICE && valid <= MAX_PRICE);
        }

        for (double invalid : invalidPrices) {
            assertFalse("Price " + invalid + " should be invalid",
                    invalid >= MIN_PRICE && invalid <= MAX_PRICE);
        }
    }

    @Test
    public void testQuantityValidation() {
        int[] validQuantities = {0, 10, 9999};
        int[] invalidQuantities = {-1, 10000};

        for (int valid : validQuantities) {
            assertTrue("Quantity " + valid + " should be valid",
                    valid >= MIN_QUANTITY && valid <= MAX_QUANTITY);
        }

        for (int invalid : invalidQuantities) {
            assertFalse("Quantity " + invalid + " should be invalid",
                    invalid >= MIN_QUANTITY && invalid <= MAX_QUANTITY);
        }
    }

    @Test
    public void testItemWithNullDescription() {
        Item item = TestDataProvider.getValidItem();
        item.setDescription(null);
        assertFalse("Null description should not be allowed", validateItem(item));
    }

    private boolean validateItem(Item item) {
        if (item == null) return false;
        if (item.getItemId() == null || !ITEM_ID_PATTERN.matcher(item.getItemId()).matches()) return false;
        if (item.getName() == null || item.getName().trim().isEmpty()) return false;
        if (item.getDescription() == null) return false;
        if (item.getUnitPrice() < MIN_PRICE || item.getUnitPrice() > MAX_PRICE) return false;
        if (item.getStockQuantity() < MIN_QUANTITY || item.getStockQuantity() > MAX_QUANTITY) return false;
        if (item.getReorderLevel() < 0) return false;
        return true;
    }
}