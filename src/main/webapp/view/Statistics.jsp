<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Family Allowance Statistics</title>
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; margin: 0; padding: 20px; }
        h1, h2 { color: #333; }
        table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .stats-container { display: flex; flex-direction: column; }
        .stats-section { margin-bottom: 30px; }
    </style>
</head>
<body>
<%@ include file="navbar.jsp" %>
<h1>Family Allowance Statistics</h1>

<div class="stats-container">
    <div class="stats-section">
        <h2>Monthly Statistics</h2>
        <table>
            <thead>
            <tr>
                <th>Month</th>
                <th>Total Allowance</th>
                <th>Employee Count</th>
                <th>Average Allowance</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${monthlyStats}" var="entry">
                <tr>
                    <td>${entry.key}</td>
                    <td><fmt:formatNumber value="${entry.value.total}" type="currency"/></td>
                    <td>${entry.value.count}</td>
                    <td><fmt:formatNumber value="${entry.value.total / entry.value.count}" type="currency"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="stats-section">
        <h2>Employee Statistics</h2>
        <table>
            <thead>
            <tr>
                <th>Employee ID</th>
                <th>Name</th>
                <th>Salary</th>
                <th>Number of Children</th>
                <th>Marital Status</th>
                <th>Monthly Allowance</th>
                <th>Total Annual Allowance</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${employees}" var="employee">
                <tr>
                    <td>${employee.id}</td>
                    <td>${employee.firstName} ${employee.lastName}</td>
                    <td><fmt:formatNumber value="${employee.salary}" type="currency"/></td>
                    <td>${employee.kidsNum}</td>
                    <td>${employee.situation}</td>
                    <td><fmt:formatNumber value="${employeeStats[employee.id] / 12}" type="currency"/></td>
                    <td><fmt:formatNumber value="${employeeStats[employee.id]}" type="currency"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>