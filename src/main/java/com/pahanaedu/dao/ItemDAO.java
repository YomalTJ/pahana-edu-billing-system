package com.pahanaedu.dao;

import com.pahanaedu.model.Category;
import com.pahanaedu.model.Item;
import com.pahanaedu.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    private CategoryDAO categoryDao = new CategoryDAO();

    public boolean addItem(Item item) {
        // First check for null input
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        // Validate required fields
        if (item.getItemId() == null || item.getName() == null || item.getCategory() == null) {
            throw new IllegalArgumentException("Item ID, name, and category are required");
        }

        String sql = "INSERT INTO items (item_id, name, description, category_id, unit_price, stock_quantity, reorder_level) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getItemId());
            stmt.setString(2, item.getName());
            stmt.setString(3, item.getDescription());
            stmt.setInt(4, item.getCategory().getCategoryId());
            stmt.setDouble(5, item.getUnitPrice());
            stmt.setInt(6, item.getStockQuantity());
            stmt.setInt(7, item.getReorderLevel());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (item.getItemId() == null || item.getName() == null || item.getCategory() == null) {
            throw new IllegalArgumentException("Item ID, name, and category are required");
        }

        String sql = "UPDATE items SET name=?, description=?, category_id=?, unit_price=?, " +
                "stock_quantity=?, reorder_level=? WHERE item_id=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setInt(3, item.getCategory().getCategoryId());
            stmt.setDouble(4, item.getUnitPrice());
            stmt.setInt(5, item.getStockQuantity());
            stmt.setInt(6, item.getReorderLevel());
            stmt.setString(7, item.getItemId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteItem(String itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }

        String sql = "DELETE FROM items WHERE item_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Item getItemById(String itemId) throws SQLException {
        if (itemId == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }

        String sql = "SELECT * FROM items WHERE item_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, itemId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Item item = new Item();
                    item.setItemId(rs.getString("item_id"));
                    item.setName(rs.getString("name"));
                    item.setDescription(rs.getString("description"));

                    // Verify category exists
                    int categoryId = rs.getInt("category_id");
                    Category category = categoryDao.getCategoryById(categoryId);
                    if (category == null) {
                        System.err.println("Category not found for ID: " + categoryId);
                    }
                    item.setCategory(category);

                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setStockQuantity(rs.getInt("stock_quantity"));
                    item.setReorderLevel(rs.getInt("reorder_level"));

                    return item;
                }
            }
        }
        return null;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items ORDER BY name";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public int getItemCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM items";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt("count") : 0;
        }
    }

    public int getLowStockItemCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM items WHERE stock_quantity <= reorder_level";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt("count") : 0;
        }
    }
}