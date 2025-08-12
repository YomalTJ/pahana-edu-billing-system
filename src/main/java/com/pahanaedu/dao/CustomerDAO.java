package com.pahanaedu.dao;

import com.pahanaedu.model.Customer;
import com.pahanaedu.model.CustomerType;
import com.pahanaedu.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public boolean addCustomer(Customer customer) {
        if (customer == null) {
            System.err.println("Attempted to add null customer");
            return false;
        }

        String sql = "INSERT INTO customers (account_number, name, address, telephone, email, " +
                "registration_date, customer_type, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getAccountNumber());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getTelephone());
            stmt.setString(5, customer.getEmail());
            stmt.setDate(6, new java.sql.Date(customer.getRegistrationDate().getTime()));
            stmt.setString(7, customer.getCustomerType());
            stmt.setString(8, customer.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCustomer(Customer customer) {
        if (customer == null || customer.getAccountNumber() == null) {
            return false;
        }

        String sql = "UPDATE customers SET name=?, address=?, telephone=?, email=?, " +
                "customer_type=?, status=? WHERE account_number=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getTelephone());
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getCustomerType());
            stmt.setString(6, customer.getStatus());
            stmt.setString(7, customer.getAccountNumber());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomer(String accountNumber) {
        String sql = "DELETE FROM customers WHERE account_number = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Customer getCustomerByAccountNumber(String accountNumber) {
        if (accountNumber == null) {
            return null;
        }

        String sql = "SELECT * FROM customers WHERE account_number = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer();
                    customer.setAccountNumber(rs.getString("account_number"));
                    customer.setName(rs.getString("name"));
                    customer.setAddress(rs.getString("address"));
                    customer.setTelephone(rs.getString("telephone"));
                    customer.setEmail(rs.getString("email"));
                    customer.setRegistrationDate(rs.getDate("registration_date"));
                    customer.setCustomerType(rs.getString("customer_type"));
                    customer.setStatus(rs.getString("status"));
                    customer.setLastPurchaseDate(rs.getDate("last_purchase_date"));
                    return customer;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY name";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setAccountNumber(rs.getString("account_number"));
                customer.setName(rs.getString("name"));
                customer.setAddress(rs.getString("address"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
                customer.setRegistrationDate(rs.getDate("registration_date"));
                customer.setCustomerType(rs.getString("customer_type"));
                customer.setStatus(rs.getString("status"));
                customer.setLastPurchaseDate(rs.getDate("last_purchase_date"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public List<CustomerType> getAllCustomerTypes() {
        List<CustomerType> types = new ArrayList<>();
        String sql = "SELECT * FROM customer_types";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CustomerType type = new CustomerType();
                type.setTypeId(rs.getInt("type_id"));
                type.setTypeName(rs.getString("type_name"));
                type.setDiscountPercentage(rs.getDouble("discount_percentage"));
                types.add(type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

    public int getCustomerCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM customers";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
            throw new SQLException("No results returned for customer count");
        }
    }

    public String generateNewAccountNumber() {
        String sql = "SELECT MAX(CAST(SUBSTRING(account_number, 4) AS UNSIGNED)) AS last_num FROM customers WHERE account_number REGEXP '^ACC[0-9]+$'";
        String prefix = "ACC";
        int nextNum = 1;

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int lastNum = rs.getInt("last_num");
                if (lastNum > 0) {
                    nextNum = lastNum + 1;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error generating account number: " + e.getMessage());
            e.printStackTrace();
        }
        return prefix + String.format("%05d", nextNum);
    }
}