<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Customer Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <style>
        /* Main Content Styles */
        main {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
        }

        h2 {
            color: #2c3e50;
            margin-bottom: 1.5rem;
            padding-bottom: 0.5rem;
            border-bottom: 1px solid #eee;
        }

        /* Message Styles */
        .success {
            background-color: #d4edda;
            color: #155724;
            padding: 1rem;
            margin-bottom: 1.5rem;
            border-radius: 4px;
            border: 1px solid #c3e6cb;
        }

        .error {
            background-color: #f8d7da;
            color: #721c24;
            padding: 1rem;
            margin-bottom: 1.5rem;
            border-radius: 4px;
            border: 1px solid #f5c6cb;
        }

        /* Action Button */
        .actions {
            margin-bottom: 1.5rem;
            text-align: right;
        }

        .button {
            display: inline-block;
            padding: 0.5rem 1rem;
            background-color: #3498db;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-weight: 600;
            transition: background-color 0.3s;
        }

        .button:hover {
            background-color: #2980b9;
        }

        /* Table Styles */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 1rem;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }

        th, td {
            padding: 1rem;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #34495e;
            color: white;
            font-weight: 600;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        /* Action Buttons in Table */
        td .button {
            padding: 0.3rem 0.6rem;
            font-size: 0.9rem;
        }

        /* Status Indicators */
        td:contains('ACTIVE') {
            color: #28a745;
            font-weight: 600;
        }

        td:contains('INACTIVE') {
            color: #dc3545;
            font-weight: 600;
        }

        /* Responsive Table */
        @media (max-width: 768px) {
            table {
                display: block;
                overflow-x: auto;
            }
        }
    </style>
</head>
<body>
<jsp:include page="../header.jsp"/>

<main>
    <h2>Customer Management</h2>

    <c:if test="${not empty message}">
        <div class="success">${message}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <div class="actions">
        <a href="${pageContext.request.contextPath}/customers?action=new" class="button">Add New Customer</a>
    </div>

    <table>
        <thead>
        <tr>
            <th>Account #</th>
            <th>Name</th>
            <th>Telephone</th>
            <th>Email</th>
            <th>Type</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${customers}" var="customer">
            <tr>
                <td>${customer.accountNumber}</td>
                <td>${customer.name}</td>
                <td>${customer.telephone}</td>
                <td>${customer.email}</td>
                <td>${customer.customerType}</td>
                <td class="status">${customer.status}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/customers?action=edit&accountNumber=${customer.accountNumber}" class="button">Edit</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>

<script>
    // Add status color coding
    document.addEventListener('DOMContentLoaded', function() {
        const statusCells = document.querySelectorAll('td.status');
        statusCells.forEach(cell => {
            if (cell.textContent.trim() === 'ACTIVE') {
                cell.style.color = '#28a745';
                cell.style.fontWeight = '600';
            } else if (cell.textContent.trim() === 'INACTIVE') {
                cell.style.color = '#dc3545';
                cell.style.fontWeight = '600';
            }
        });
    });
</script>
</body>
</html>