<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Page Not Found</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
<div class="error-container">
    <h1>404 - Page Not Found</h1>
    <p>The page you requested could not be found.</p>
    <a href="${pageContext.request.contextPath}/dashboard" class="button">Return to Dashboard</a>
</div>
</body>
</html>