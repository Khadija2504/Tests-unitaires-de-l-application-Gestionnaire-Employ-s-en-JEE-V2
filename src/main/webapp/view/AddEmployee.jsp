<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Employee</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }

        h2 {
            color: #333;
            text-align: center;
        }

        form {
            display: grid;
            gap: 10px;
        }

        label {
            font-weight: bold;
        }

        input[type="text"],
        input[type="email"],
        input[type="number"],
        input[type="date"],
        input[type="password"],
        select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }

        input[type="submit"] {
            background-color: #007bff;
            color: #fff;
            border: none;
            padding: 10px 15px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        input[type="submit"]:hover {
            background-color: #0056b3;
        }

        .error-message {
            color: red;
            margin-top: 10px;
            text-align: center;
        }
    </style>
</head>
<body>
<%@ include file="navbar.jsp" %>
<div class="container">
    <h2>Add New Employee</h2>
    <c:if test="${not empty errorMessage}">
        <div class="error-message">${errorMessage}</div>
    </c:if>
    <form action="employee?action=addEmployee" method="post">
        <%--@declare id="nssu"--%><input type="hidden" name="action" value="addEmployee">

        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" required>

        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" required>

        <label for="phoneNumber">Phone Number:</label>
        <input type="text" id="phoneNumber" name="phoneNumber" required>

        <label for="nssu">Number of single social security</label>
        <input name="nssu" id="nssu" type="text" required>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>

        <label for="salary">Salary:</label>
        <input type="number" id="salary" name="salary" required>

        <label for="birthday">Birthday:</label>
        <input type="date" id="birthday" name="birthday" required>

        <label for="hireDate">Hire Date:</label>
        <input type="date" id="hireDate" name="hireDate" required>

        <label for="position">Position:</label>
        <input type="text" id="position" name="position" required>

        <label for="kidsNum">Number of Kids:</label>
        <input type="number" id="kidsNum" name="kidsNum" required>

        <label for="situation">Situation:</label>
        <input type="text" id="situation" name="situation" required>

        <label for="department">Department:</label>
        <input type="text" id="department" name="department" required>

        <input type="submit" value="Add Employee">
    </form>
</div>
</body>
</html>