<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Holiday Request</title>
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
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 20px;
        }
        form {
            display: flex;
            flex-direction: column;
        }
        label {
            margin-bottom: 5px;
            font-weight: bold;
        }
        input[type="date"],
        input[type="text"],
        input[type="file"] {
            width: 100%;
            padding: 8px;
            margin-bottom: 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
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
<%@ include file="navbar.jsp" %>
<div class="container">
    <h1>Add Holiday Request</h1>
    <% if(request.getAttribute("errorMessage") != null) { %>
    <div class="error-message">
        <%= request.getAttribute("errorMessage") %>
    </div>
    <% } %>
    <form action="holidays?action=addHoliday" method="post" enctype="multipart/form-data">
        <label for="startDate">Start Date:</label>
        <input name="startDate" type="date" id="startDate" required>

        <label for="endDate">End Date:</label>
        <input name="endDate" type="date" id="endDate" required>

        <label for="reason">Reason:</label>
        <input name="reason" type="text" id="reason" required>

        <label for="justification">Justification Document:</label>
        <input name="justification" type="file" id="justification" required>

        <input type="hidden" name="employeeId" value="${param.employeeId}">

        <button type="submit">Submit Holiday Request</button>
    </form>
</div>
</body>
</html>
