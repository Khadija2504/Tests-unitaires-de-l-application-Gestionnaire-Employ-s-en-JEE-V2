<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>Login - HR Management System</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f4f4;
      margin: 0;
      padding: 20px;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }
    .login-container {
      background-color: white;
      padding: 20px;
      border-radius: 5px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
      width: 300px;
    }
    h2 {
      text-align: center;
      color: #333;
    }
    form {
      display: flex;
      flex-direction: column;
    }
    label {
      margin-top: 10px;
      color: #666;
    }
    input[type="email"],
    input[type="password"] {
      padding: 8px;
      margin-top: 5px;
      border: 1px solid #ddd;
      border-radius: 4px;
    }
    input[type="submit"] {
      margin-top: 20px;
      padding: 10px;
      background-color: #4CAF50;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    input[type="submit"]:hover {
      background-color: #45a049;
    }
    .error-message {
      color: red;
      margin-top: 10px;
      text-align: center;
    }
  </style>
</head>
<body>
<div class="login-container">
  <h2>Login</h2>
  <form action="loginForm" method="post">
    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required>

    <input type="submit" value="Login">
  </form>

  <c:if test="${not empty error}">
    <p class="error-message">${error}</p>
  </c:if>
</div>
</body>
</html>