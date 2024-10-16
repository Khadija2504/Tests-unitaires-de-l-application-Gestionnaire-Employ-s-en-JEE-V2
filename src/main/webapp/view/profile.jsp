<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Profile</title>
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; margin: 0; padding: 20px; }
        h1, h2 { color: #333; }
        table { border-collapse: collapse; width: 100%; max-width: 600px; margin-bottom: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
<%@ include file="navbar.jsp" %>
<h1>Employee Profile</h1>

<h2>Personal Information</h2>
<table>
    <tr>
        <th>Employee ID</th>
        <td>${employee.id}</td>
    </tr>
    <tr>
        <th>First Name</th>
        <td>${employee.firstName}</td>
    </tr>
    <tr>
        <th>Last Name</th>
        <td>${employee.lastName}</td>
    </tr>
    <tr>
        <th>Email</th>
        <td>${employee.email}</td>
    </tr>
    <tr>
        <th>Phone Number</th>
        <td>${employee.phoneNumber}</td>
    </tr>
    <tr>
        <th>Birthday</th>
        <td><fmt:formatDate value="${employee.birthday}" pattern="yyyy-MM-dd"/></td>
    </tr>
    <tr>
        <th>NSSU</th>
        <td>${employee.nssu}</td>
    </tr>
</table>

<h2>Employment Information</h2>
<table>
    <tr>
        <th>Department</th>
        <td>${employee.department}</td>
    </tr>
    <tr>
        <th>Position</th>
        <td>${employee.position}</td>
    </tr>
    <tr>
        <th>Hire Date</th>
        <td><fmt:formatDate value="${employee.hireDate}" pattern="yyyy-MM-dd"/></td>
    </tr>
    <tr>
        <th>Role</th>
        <td>${employee.role}</td>
    </tr>
</table>

<h2>Salary Information</h2>
<table>
    <tr>
        <th>Base Salary</th>
        <td>${employee.salary}</td>
    </tr>
    <tr>
        <th>Total Salary</th>
        <td>${employee.totalSalary}</td>
    </tr>
</table>

<h2>Family Information</h2>
<table>
    <tr>
        <th>Marital Status</th>
        <td>${employee.situation}</td>
    </tr>
    <tr>
        <th>Number of Children</th>
        <td>${employee.kidsNum}</td>
    </tr>
</table>

<h2>Family Allowance Details</h2>
<table>
    <tr>
        <th>Amount</th>
        <td>${familyAllowanceDetails.amount}</td>
    </tr>
    <tr>
        <th>Number of Children</th>
        <td>${familyAllowanceDetails.num_children}</td>
    </tr>
    <tr>
        <th>Marital Status</th>
        <td>${familyAllowanceDetails.marital_status}</td>
    </tr>
</table>

<p><a href="dashboard">Back to Dashboard</a></p>
</body>
</html>