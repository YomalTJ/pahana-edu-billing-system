<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Server Error</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
<div class="error-container">
  <h1>500 - Server Error</h1>
  <p>An unexpected error occurred. Our team has been notified.</p>
  <a href="${pageContext.request.contextPath}/dashboard" class="button">Return to Dashboard</a>
</div>
</body>
</html>