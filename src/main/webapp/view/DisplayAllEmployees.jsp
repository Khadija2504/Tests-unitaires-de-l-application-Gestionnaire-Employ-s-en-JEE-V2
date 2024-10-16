<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 1400px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            color: #333;
        }
        .btn {
            display: inline-block;
            padding: 10px 15px;
            background-color: #007bff;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            font-size: 0.9em;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .btn-edit, .btn-delete {
            padding: 5px 10px;
            color: #fff;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            text-decoration: none;
            font-size: 0.9em;
        }
        .btn-edit {
            background-color: #ffc107;
        }
        .btn-delete {
            background-color: #dc3545;
        }
        .btn-edit:hover {
            background-color: #e0a800;
        }
        .btn-delete:hover {
            background-color: #c82333;
        }
    </style>
</head>
<body>
<%@ include file="navbar.jsp" %>
<div class="container">
    <h2>Employee List</h2>
    <a href="employee?action=addEmployeeForm" class="btn">Add New Employee</a>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Phone Number</th>
            <th>Email</th>
            <th>Salary</th>
            <th>Birthday</th>
            <th>Hire Date</th>
            <th>Position</th>
            <th>Number of Kids</th>
            <th>Total Salary</th>
            <th>Situation</th>
            <th>Department</th>
            <c:if test="${user.role == 'Admin'}">
                <th>Actions</th>
            </c:if>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="employee" items="${employees}">
            <tr>
                <td>${employee.id}</td>
                <td>${employee.firstName}</td>
                <td>${employee.lastName}</td>
                <td>${employee.phoneNumber}</td>
                <td>${employee.email}</td>
                <td>${employee.salary}</td>
                <td><fmt:formatDate value="${employee.birthday}" pattern="yyyy-MM-dd"/></td>
                <td><fmt:formatDate value="${employee.hireDate}" pattern="yyyy-MM-dd"/></td>
                <td>${employee.position}</td>
                <td>${employee.kidsNum}</td>
                <td>${employee.totalSalary}</td>
                <td>${employee.situation}</td>
                <td>${employee.department}</td>
                <c:if test="${user.role == 'Admin'}">
                <td>
                    <a href="employee?action=editEmployee&id=${employee.id}" class="btn-edit">Edit</a>
                    <a href="employee?action=deleteEmployee&id=${employee.id}" class="btn-delete" onclick="return confirm('Are you sure you want to delete this employee?')">Delete</a>
                </td>
                </c:if>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>