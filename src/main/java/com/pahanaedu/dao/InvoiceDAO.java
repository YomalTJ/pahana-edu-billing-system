package com.pahanaedu.dao;

import com.pahanaedu.model.Customer;
import com.pahanaedu.model.Invoice;
import com.pahanaedu.model.InvoiceItem;
import com.pahanaedu.model.Item;
import com.pahanaedu.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    private ItemDAO itemDao = new ItemDAO();
    private CustomerDAO customerDao = new CustomerDAO();

    public boolean createInvoice(Invoice invoice) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // Insert invoice
            String invoiceSql = "INSERT INTO invoices (invoice_id, customer_account, invoice_date, " +
                    "subtotal, discount, tax, total, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement invoiceStmt = conn.prepareStatement(invoiceSql)) {
                invoiceStmt.setString(1, invoice.getInvoiceId());
                invoiceStmt.setString(2, invoice.getCustomerAccount());
                invoiceStmt.setTimestamp(3, new Timestamp(invoice.getInvoiceDate().getTime()));
                invoiceStmt.setDouble(4, invoice.getSubtotal());
                invoiceStmt.setDouble(5, invoice.getDiscount());
                invoiceStmt.setDouble(6, invoice.getTax());
                invoiceStmt.setDouble(7, invoice.getTotal());
                invoiceStmt.setString(8, invoice.getStatus());

                invoiceStmt.executeUpdate();
            }

            // Insert invoice items
            String itemSql = "INSERT INTO invoice_items (invoice_id, item_id, quantity, " +
                    "unit_price, line_total) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                for (InvoiceItem item : invoice.getItems()) {
                    itemStmt.setString(1, invoice.getInvoiceId());
                    itemStmt.setString(2, item.getItemId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getUnitPrice());
                    itemStmt.setDouble(5, item.getLineTotal());
                    itemStmt.addBatch();

                    // Update stock quantity
                    updateItemStock(conn, item.getItemId(), -item.getQuantity());
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

    private void updateItemStock(Connection conn, String itemId, int quantityChange) throws SQLException {
        String sql = "UPDATE items SET stock_quantity = stock_quantity + ? WHERE item_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantityChange);
            stmt.setString(2, itemId);
            stmt.executeUpdate();
        }
    }

    public Invoice getInvoiceById(String invoiceId) {
        String invoiceSql = "SELECT * FROM invoices WHERE invoice_id = ?";
        Invoice invoice = null;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(invoiceSql)) {

            stmt.setString(1, invoiceId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    invoice = new Invoice();
                    invoice.setInvoiceId(rs.getString("invoice_id"));
                    invoice.setCustomerAccount(rs.getString("customer_account"));
                    invoice.setInvoiceDate(rs.getTimestamp("invoice_date"));
                    invoice.setSubtotal(rs.getDouble("subtotal"));
                    invoice.setDiscount(rs.getDouble("discount"));
                    invoice.setTax(rs.getDouble("tax"));
                    invoice.setTotal(rs.getDouble("total"));
                    invoice.setStatus(rs.getString("status"));

                    // Get customer details
                    Customer customer = customerDao.getCustomerByAccountNumber(invoice.getCustomerAccount());
                    invoice.setCustomer(customer);

                    // Get invoice items
                    List<InvoiceItem> items = getInvoiceItems(conn, invoiceId);
                    invoice.setItems(items);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoice;
    }

    private List<InvoiceItem> getInvoiceItems(Connection conn, String invoiceId) throws SQLException {
        List<InvoiceItem> items = new ArrayList<>();
        String sql = "SELECT * FROM invoice_items WHERE invoice_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, invoiceId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    InvoiceItem item = new InvoiceItem();
                    item.setInvoiceItemId(rs.getInt("invoice_item_id"));
                    item.setInvoiceId(rs.getString("invoice_id"));
                    item.setItemId(rs.getString("item_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setLineTotal(rs.getDouble("line_total"));

                    // Get item details
                    Item itemDetails = itemDao.getItemById(item.getItemId());
                    item.setItem(itemDetails);

                    items.add(item);
                }
            }
        }
        return items;
    }

    public List<Invoice> getInvoicesByCustomer(String accountNumber) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE customer_account = ? ORDER BY invoice_date DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getString("invoice_id"));
                    invoice.setCustomerAccount(rs.getString("customer_account"));
                    invoice.setInvoiceDate(rs.getTimestamp("invoice_date"));
                    invoice.setSubtotal(rs.getDouble("subtotal"));
                    invoice.setDiscount(rs.getDouble("discount"));
                    invoice.setTax(rs.getDouble("tax"));
                    invoice.setTotal(rs.getDouble("total"));
                    invoice.setStatus(rs.getString("status"));

                    invoices.add(invoice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }
}