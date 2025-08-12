<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
  /* Header Styles */
  header {
    background-color: #2c3e50;
    color: white;
    padding: 1rem 2rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  }

  header h1 {
    margin: 0;
    font-size: 1.5rem;
  }

  .button {
    background-color: #e74c3c;
    color: white;
    padding: 0.5rem 1rem;
    border-radius: 4px;
    text-decoration: none;
    font-weight: bold;
    transition: background-color 0.3s;
  }

  .button:hover {
    background-color: #c0392b;
  }

  /* Navigation Styles */
  nav {
    background-color: #34495e;
  }

  nav ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
    display: flex;
  }

  nav li {
    margin: 0;
  }

  nav a {
    color: white;
    text-decoration: none;
    padding: 1rem 1.5rem;
    display: block;
    transition: background-color 0.3s;
  }

  nav a:hover {
    background-color: #2c3e50;
  }

  /* Active link styling (optional) */
  nav a.active {
    background-color: #3498db;
    font-weight: bold;
  }
</style>

<header>
  <h1>Welcome, ${user.username}</h1>
  <a href="logout" class="button">Logout</a>
</header>
<nav>
  <ul>
    <li><a href="dashboard" ${param.active == 'dashboard' ? 'class="active"' : ''}>Dashboard</a></li>
    <li><a href="customers" ${param.active == 'customers' ? 'class="active"' : ''}>Customer Management</a></li>
    <li><a href="items" ${param.active == 'items' ? 'class="active"' : ''}>Item Management</a></li>
    <li><a href="billing" ${param.active == 'billing' ? 'class="active"' : ''}>Billing</a></li>
    <li><a href="help" ${param.active == 'help' ? 'class="active"' : ''}>Help</a></li>
  </ul>
</nav>