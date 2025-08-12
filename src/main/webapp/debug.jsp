<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.pahanaedu.util.DBUtil" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="com.pahanaedu.dao.CustomerDAO" %>
<%@ page import="com.pahanaedu.model.Customer" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>Database Debug Page</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .success { color: green; background: #e8f5e8; padding: 10px; border: 1px solid green; }
        .error { color: red; background: #ffe8e8; padding: 10px; border: 1px solid red; }
        .info { color: blue; background: #e8f0ff; padding: 10px; border: 1px solid blue; }
        table { border-collapse: collapse; width: 100%; margin-top: 10px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
<h1>Database Connection Debug</h1>

<h2>1. Database Connection Test</h2>
<%
    try {
        Connection conn = DBUtil.getConnection();
        if (conn != null && !conn.isClosed()) {
            out.println("<div class='success'>✅ Database connection successful!</div>");
            conn.close();
        } else {
            out.println("<div class='error'>❌ Database connection failed - connection is null or closed</div>");
        }
    } catch (Exception e) {
        out.println("<div class='error'>❌ Database connection error: " + e.getMessage() + "</div>");
        e.printStackTrace();
    }
%>

<h2>2. Customer DAO Test</h2>
<%
    try {
        CustomerDAO customerDao = new CustomerDAO();
        out.println("<div class='success'>✅ CustomerDAO initialized successfully!</div>");

        List<Customer> customers = customerDao.getAllCustomers();
        out.println("<div class='info'>📊 Found " + customers.size() + " customers in database</div>");

        if (!customers.isEmpty()) {
            out.println("<table>");
            out.println("<tr><th>Account Number</th><th>Name</th><th>Type</th><th>Phone</th></tr>");
            for (Customer customer : customers) {
                out.println("<tr>");
                out.println("<td>" + (customer.getAccountNumber() != null ? customer.getAccountNumber() : "NULL") + "</td>");
                out.println("<td>" + (customer.getName() != null ? customer.getName() : "NULL") + "</td>");
                out.println("<td>" + (customer.getCustomerType() != null ? customer.getCustomerType() : "NULL") + "</td>");
                out.println("<td>" + (customer.getTelephone() != null ? customer.getTelephone() : "NULL") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }
    } catch (Exception e) {
        out.println("<div class='error'>❌ CustomerDAO error: " + e.getMessage() + "</div>");
        e.printStackTrace();
    }
%>

<h2>3. Sample Customer Data</h2>
<div class='info'>
    If you don't have customers in your database, you can add some sample data using this SQL:
    <pre>
INSERT INTO customers (account_number, name, address, telephone, customer_type) VALUES
('ACC00001', 'John Doe', '123 Main St, Colombo', '0771234567', 'Regular'),
('ACC00002', 'Jane Smith', '456 Lake Rd, Kandy', '0759876543', 'Premium'),
('ACC00003', 'Bob Wilson', '789 Hill St, Galle', '0712345678', 'Student');
        </pre>
</div>

<hr>
<p><a href="/billing">Go to Billing Page</a> | <a href="/dashboard">Go to Dashboard</a></p>
</body>
</html>