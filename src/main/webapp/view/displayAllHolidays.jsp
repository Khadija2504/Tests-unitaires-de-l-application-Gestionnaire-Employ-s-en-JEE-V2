<%@ page import="com.hrmanagementsystem.enums.ApplicationStatus" %>
<%@ page import="com.hrmanagementsystem.enums.HolidayStatus" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Holidays</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            color: #333;
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f4f4f4;
        }
        h1 {
            color: #2c3e50;
            text-align: center;
            margin-bottom: 30px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #3498db;
            color: #fff;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .status-pending {
            color: #f39c12;
        }
        .status-approved {
            color: #27ae60;
        }
        .status-rejected {
            color: #e74c3c;
        }
        .add-holiday {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #3498db;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
        }
        .add-holiday:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
<%@ include file="navbar.jsp"%>
<h1>All Holidays</h1>

<table>
    <thead>
    <tr>
        <th>Employee</th>
        <th>Start Date</th>
        <th>End Date</th>
        <th>Reason</th>
        <th>Status</th>
        <th>Justification</th>
        <c:if test="${user.role == 'Admin'}">
            <th>Update status</th>
        </c:if>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="holiday" items="${holidays}">
        <tr>
            <td>${holiday.employee.firstName} ${holiday.employee.lastName}</td>
            <td><fmt:formatDate value="${holiday.startDate}" pattern="dd/MM/yyyy"/></td>
            <td><fmt:formatDate value="${holiday.endDate}" pattern="dd/MM/yyyy"/></td>
            <td>${holiday.reason}</td>
            <td class="status-${holiday.status.toString().toLowerCase()}">${holiday.status}</td>
            <td>
                <c:if test="${not empty holiday.justification}">
                    <a href="holidays?action=downloadJustification&holidayId=${holiday.id}" target="_blank">View Justification</a>
                </c:if>
            </td>
            <c:if test="${user.role == 'Admin'}">
            <td>
                <form action="holidays?action=updateHoliday&holidayId=${holiday.id}" method="post">
                    <input type="hidden" name="status" value="<%= HolidayStatus.Accepted%>" required>
                    <button type="submit">Accept it</button>
                </form>
                <form action="holidays?action=updateHoliday&holidayId=${holiday.id}" method="post">
                    <input type="hidden" name="status" value="<%= HolidayStatus.Rejected%>" required>
                    <button type="submit">Reject it</button>
                </form>
            </td>
            </c:if>
        </tr>
    </c:forEach>
    </tbody>
</table>

<a href="HolidayServlet?action=addHolidayForm" class="add-holiday">Request New Holiday</a>
</body>
</html>
