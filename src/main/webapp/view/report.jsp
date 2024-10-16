<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Employee Absence Report</title>
  <style>
    body { font-family: Arial, sans-serif; line-height: 1.6; margin: 0; padding: 20px; }
    h1 { color: #333; }
    .employee { margin-bottom: 20px; border: 1px solid #ddd; padding: 10px; }
    table { border-collapse: collapse; width: 100%; }
    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
    th { background-color: #f2f2f2; }
  </style>
</head>
<body>
<%@ include file="navbar.jsp" %>
<h1>Employee Absence Report</h1>

<c:forEach items="${employeeHolidays}" var="entry">
  <div class="employee">
    <h2>Employee: ${entry.key.firstName} ${entry.key.lastName} (ID: ${entry.key.id})</h2>
    <table>
      <thead>
      <tr>
        <th>Start Date</th>
        <th>End Date</th>
        <th>Reason</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${entry.value}" var="holiday">
        <tr>
          <td><fmt:formatDate value="${holiday.startDate}" pattern="yyyy-MM-dd"/></td>
          <td><fmt:formatDate value="${holiday.endDate}" pattern="yyyy-MM-dd"/></td>
          <td>${holiday.reason}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</c:forEach>
</body>
</html>