<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <style>
        .dashboard-cards {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 20px;
            margin-bottom: 30px;
        }
        .card {
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            padding: 20px;
            text-align: center;
        }
        .card h3 {
            margin-top: 0;
            color: #555;
        }
        .card .value {
            font-size: 24px;
            font-weight: bold;
            margin: 10px 0;
        }
        .card.customers { border-top: 4px solid #2196F3; }
        .card.items { border-top: 4px solid #4CAF50; }
        .card.stock { border-top: 4px solid #FF9800; }
        .card.sales { border-top: 4px solid #9C27B0; }

        .recent-activity {
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            padding: 20px;
        }

        .error {
            color: red;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid red;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<!-- Remove or fix this include if not needed -->
<jsp:include page="./header.jsp"/>

<main>
    <h2>Dashboard</h2>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <div class="dashboard-cards">
        <div class="card customers">
            <h3>Customers</h3>
            <div class="value"><c:out value="${not empty customerCount ? customerCount : '0'}" /></div>
            <a href="${pageContext.request.contextPath}/customers">View All</a>
        </div>

        <div class="card items">
            <h3>Inventory Items</h3>
            <div class="value"><c:out value="${not empty itemCount ? itemCount : '0'}" /></div>
            <a href="${pageContext.request.contextPath}/items">View All</a>
        </div>

        <div class="card stock">
            <h3>Low Stock Items</h3>
            <div class="value"><c:out value="${not empty lowStockCount ? lowStockCount : '0'}" /></div>
            <a href="${pageContext.request.contextPath}/items?filter=low-stock">View All</a>
        </div>

        <div class="card sales">
            <h3>Today's Sales</h3>
            <div class="value">
                <c:choose>
                    <c:when test="${not empty todaySales}">
                        <fmt:formatNumber value="${todaySales}" type="currency"/>
                    </c:when>
                    <c:otherwise>$0.00</c:otherwise>
                </c:choose>
            </div>
            <a href="${pageContext.request.contextPath}/billing">New Bill</a>
        </div>
    </div>

    <div class="recent-activity">
        <h3>Recent Activity</h3>
        <p>Recent activity tracking will be implemented in a future version.</p>
    </div>
</main>
</body>
</html>