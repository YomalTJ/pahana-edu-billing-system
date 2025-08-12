<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>
    <c:choose>
      <c:when test="${not empty customer}">Edit Customer</c:when>
      <c:otherwise>Add New Customer</c:otherwise>
    </c:choose>
  </title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
  <style>
    /* Main Content Styles */
    main {
      max-width: 800px;
      margin: 2rem auto;
      padding: 0 1rem;
    }

    h2 {
      color: #2c3e50;
      margin-bottom: 1.5rem;
      padding-bottom: 0.5rem;
      border-bottom: 1px solid #eee;
    }

    /* Form Styles */
    form {
      background-color: #fff;
      padding: 2rem;
      border-radius: 8px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }

    .form-group {
      margin-bottom: 1.5rem;
    }

    label {
      display: block;
      margin-bottom: 0.5rem;
      font-weight: 600;
      color: #34495e;
    }

    input[type="text"],
    input[type="email"],
    textarea,
    select {
      width: 100%;
      padding: 0.75rem;
      border: 1px solid #ddd;
      border-radius: 4px;
      font-size: 1rem;
      box-sizing: border-box;
      transition: border-color 0.3s;
    }

    input[type="text"]:focus,
    input[type="email"]:focus,
    textarea:focus,
    select:focus {
      outline: none;
      border-color: #3498db;
      box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
    }

    textarea {
      min-height: 100px;
      resize: vertical;
    }

    /* Radio Button Styles */
    .radio-group {
      display: flex;
      gap: 1rem;
      margin-top: 0.5rem;
    }

    .radio-group input[type="radio"] {
      width: auto;
      margin-right: 0.5rem;
    }

    .radio-group label {
      font-weight: normal;
      display: flex;
      align-items: center;
    }

    /* Button Styles */
    .form-actions {
      display: flex;
      gap: 1rem;
      margin-top: 2rem;
      justify-content: flex-end;
    }

    .button {
      padding: 0.75rem 1.5rem;
      border-radius: 4px;
      text-decoration: none;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.3s;
    }

    .button.primary {
      background-color: #3498db;
      color: white;
      border: none;
    }

    .button.primary:hover {
      background-color: #2980b9;
    }

    .button {
      background-color: #f5f5f5;
      color: #333;
      border: 1px solid #ddd;
    }

    .button:hover {
      background-color: #e0e0e0;
    }

    /* Responsive Adjustments */
    @media (max-width: 600px) {
      .form-actions {
        flex-direction: column;
      }

      .button {
        width: 100%;
        text-align: center;
      }
    }
  </style>
</head>
<body>
<jsp:include page="../header.jsp"/>

<main>
  <h2>
    <c:choose>
      <c:when test="${not empty customer}">Edit Customer</c:when>
      <c:otherwise>Add New Customer</c:otherwise>
    </c:choose>
  </h2>

  <form action="customers" method="post">
    <c:if test="${not empty customer}">
      <input type="hidden" name="action" value="update">
      <input type="hidden" name="accountNumber" value="${customer.accountNumber}">
    </c:if>
    <c:if test="${empty customer}">
      <input type="hidden" name="action" value="add">
    </c:if>

    <div class="form-group">
      <label for="accountNumber">Account Number:</label>
      <input type="text" id="accountNumber" name="accountNumber"
             value="${customer.accountNumber}"
             <c:if test="${not empty customer}">readonly</c:if> required>
    </div>

    <div class="form-group">
      <label for="name">Name:</label>
      <input type="text" id="name" name="name" value="${customer.name}" required>
    </div>

    <div class="form-group">
      <label for="address">Address:</label>
      <textarea id="address" name="address" required>${customer.address}</textarea>
    </div>

    <div class="form-group">
      <label for="telephone">Telephone:</label>
      <input type="text" id="telephone" name="telephone" value="${customer.telephone}" required>
    </div>

    <div class="form-group">
      <label for="email">Email:</label>
      <input type="email" id="email" name="email" value="${customer.email}">
    </div>

    <div class="form-group">
      <label for="customerType">Customer Type:</label>
      <select id="customerType" name="customerType" required>
        <option value="">Select Type</option>
        <c:forEach items="${customerTypes}" var="type">
          <option value="${type.typeName}"
                  <c:if test="${customer.customerType eq type.typeName}">selected</c:if>>
              ${type.typeName} (${type.discountPercentage}% discount)
          </option>
        </c:forEach>
      </select>
    </div>

    <c:if test="${not empty customer}">
      <div class="form-group">
        <label>Status:</label>
        <div class="radio-group">
          <input type="radio" id="active" name="status" value="ACTIVE"
                 <c:if test="${customer.status eq 'ACTIVE'}">checked</c:if>>
          <label for="active">Active</label>

          <input type="radio" id="inactive" name="status" value="INACTIVE"
                 <c:if test="${customer.status eq 'INACTIVE'}">checked</c:if>>
          <label for="inactive">Inactive</label>
        </div>
      </div>
    </c:if>

    <div class="form-actions">
      <button type="submit" class="button primary">
        <c:choose>
          <c:when test="${not empty customer}">Update</c:when>
          <c:otherwise>Add</c:otherwise>
        </c:choose>
      </button>
      <a href="${pageContext.request.contextPath}/customers" class="button">Cancel</a>
    </div>
  </form>
</main>
</body>
</html>