package com.pahanaedu.dao;

import com.pahanaedu.model.*;
import com.pahanaedu.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    private CustomerDAO customerDao = new CustomerDAO();
    private ItemDAO itemDao = new ItemDAO();

    public boolean createBill(Bill bill) {
        String billSql = "INSERT INTO bills (bill_id, customer_account, bill_date, subtotal, " +
                "discount, tax, total, payment_method, payment_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String itemSql = "INSERT INTO bill_items (bill_id, item_id, quantity, unit_price, line_total) " +
                "VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // Insert bill
            try (PreparedStatement billStmt = conn.prepareStatement(billSql)) {
                billStmt.setString(1, bill.getBillId());
                billStmt.setString(2, bill.getCustomer().getAccountNumber());
                billStmt.setTimestamp(3, new Timestamp(bill.getBillDate().getTime()));
                billStmt.setDouble(4, bill.getSubtotal());
                billStmt.setDouble(5, bill.getDiscount());
                billStmt.setDouble(6, bill.getTax());
                billStmt.setDouble(7, bill.getTotal());
                billStmt.setString(8, bill.getPaymentMethod());
                billStmt.setString(9, bill.getPaymentStatus());

                billStmt.executeUpdate();
            }

            // Insert bill items
            try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                for (BillItem item : bill.getItems()) {
                    itemStmt.setString(1, bill.getBillId());
                    itemStmt.setString(2, item.getItem().getItemId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getUnitPrice());
                    itemStmt.setDouble(5, item.getLineTotal());
                    itemStmt.addBatch();

                    // Update item stock
                    updateItemStock(conn, item.getItem().getItemId(), item.getQuantity());
                }
                itemStmt.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateItemStock(Connection conn, String itemId, int quantity) throws SQLException {
        String sql = "UPDATE items SET stock_quantity = stock_quantity - ? WHERE item_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setString(2, itemId);
            stmt.executeUpdate();
        }
    }

    public Bill getBillById(String billId) {
        String billSql = "SELECT * FROM bills WHERE bill_id = ?";
        String itemsSql = "SELECT * FROM bill_items WHERE bill_id = ?";

        Bill bill = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement billStmt = conn.prepareStatement(billSql)) {

            billStmt.setString(1, billId);
            try (ResultSet rs = billStmt.executeQuery()) {
                if (rs.next()) {
                    bill = new Bill();
                    bill.setBillId(rs.getString("bill_id"));

                    Customer customer = customerDao.getCustomerByAccountNumber(rs.getString("customer_account"));
                    bill.setCustomer(customer);

                    bill.setBillDate(rs.getTimestamp("bill_date"));
                    bill.setSubtotal(rs.getDouble("subtotal"));
                    bill.setDiscount(rs.getDouble("discount"));
                    bill.setTax(rs.getDouble("tax"));
                    bill.setTotal(rs.getDouble("total"));
                    bill.setPaymentMethod(rs.getString("payment_method"));
                    bill.setPaymentStatus(rs.getString("payment_status"));

                    // Get bill items
                    try (PreparedStatement itemsStmt = conn.prepareStatement(itemsSql)) {
                        itemsStmt.setString(1, billId);
                        try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                            List<BillItem> items = new ArrayList<>();
                            while (itemsRs.next()) {
                                BillItem item = new BillItem();
                                item.setBillItemId(itemsRs.getInt("bill_item_id"));

                                Item product = itemDao.getItemById(itemsRs.getString("item_id"));
                                item.setItem(product);

                                item.setQuantity(itemsRs.getInt("quantity"));
                                item.setUnitPrice(itemsRs.getDouble("unit_price"));
                                item.setLineTotal(itemsRs.getDouble("line_total"));
                                items.add(item);
                            }
                            bill.setItems(items);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bill;
    }

    public List<Bill> getBillsByCustomer(String accountNumber) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT bill_id FROM bills WHERE customer_account = ? ORDER BY bill_date DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Bill bill = getBillById(rs.getString("bill_id"));
                    if (bill != null) {
                        bills.add(bill);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    public String generateNewBillId() {
        String sql = "SELECT MAX(bill_id) AS last_id FROM bills";
        String prefix = "BIL";
        int nextNum = 1;

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("last_id");
                if (lastId != null && lastId.startsWith(prefix)) {
                    nextNum = Integer.parseInt(lastId.substring(prefix.length())) + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prefix + String.format("%05d", nextNum);
    }

    public double getTodaySalesTotal() throws SQLException {
        String sql = "SELECT SUM(total) AS total FROM bills WHERE DATE(bill_date) = CURDATE()";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getDouble("total") : 0.0;
        }
    }

}