package com.pahanaedu.service;

import com.pahanaedu.dao.CategoryDAO;
import com.pahanaedu.dao.ItemDAO;
import com.pahanaedu.model.Category;
import com.pahanaedu.model.Item;
import com.pahanaedu.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemService {
    private ItemDAO itemDao = new ItemDAO();
    private CategoryDAO categoryDao = new CategoryDAO();

    public boolean addItem(Item item) {
        // Business validation can be added here
        return itemDao.addItem(item);
    }

    public boolean updateItem(Item item) {
        return itemDao.updateItem(item);
    }

    public boolean deleteItem(String itemId) {
        return itemDao.deleteItem(itemId);
    }

    public Item getItemById(String itemId) {
        try {
            return itemDao.getItemById(itemId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Item> getAllItems() {
        return itemDao.getAllItems();
    }

    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    public Item getItemForBilling(String itemId) {
        System.out.println("Searching for item: " + itemId);
        Item item = null;
        try {
            item = itemDao.getItemById(itemId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (item != null) {
            System.out.println("Found item: " + item.getItemId() +
                    ", Stock: " + item.getStockQuantity());
            return item;
        }
        System.out.println("Item not found or out of stock");
        return null;
    }

    public List<Item> searchItems(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllItems();
        }

        String sql = "SELECT * FROM items WHERE name LIKE ? OR item_id LIKE ? ORDER BY name";
        List<Item> items = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + query + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setItemId(rs.getString("item_id"));
                    item.setName(rs.getString("name"));
                    item.setDescription(rs.getString("description"));

                    Category category = categoryDao.getCategoryById(rs.getInt("category_id"));
                    item.setCategory(category);

                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setStockQuantity(rs.getInt("stock_quantity"));
                    item.setReorderLevel(rs.getInt("reorder_level"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}