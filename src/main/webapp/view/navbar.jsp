<%--
  Created by IntelliJ IDEA.
  User: safiy
  Date: 10/10/2024
  Time: 9:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>

    <style>
        nav {
            background-color: #333;
            padding: 10px 0;
        }

        nav ul {
            list-style-type: none;
            padding: 0;
            margin: 0;
            display: flex;
            justify-content: center;
        }

        nav ul li {
            margin: 0 10px;
        }

        nav ul li a {
            color: white;
            text-decoration: none;
            padding: 5px 10px;
            border-radius: 3px;
            transition: background-color 0.3s;
        }

        nav ul li a:hover {
            background-color: #555;
        }
    </style>
</head>
<body>
<nav>
    <ul>
        <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
        <c:if test="${user.role == 'Admin'}">
            <li><a href="${pageContext.request.contextPath}/employee?action=addEmployeeForm">Add employee</a></li>
            <li><a href="${pageContext.request.contextPath}/employee?action=employeeList">Employees list</a></li>
            <li><a href="${pageContext.request.contextPath}/holidays?action=getAllHolidays">Holidays list</a></li>
            <li><a href="${pageContext.request.contextPath}/holidays?action=generateMonthlyReport">Absence report</a></li>
        </c:if>
        <c:if test="${user.role == 'Employee'}">
            <li><a href="${pageContext.request.contextPath}/employee?action=employeeProfile">View profile</a></li>
            <li><a href="${pageContext.request.contextPath}/holidays?action=addHolidayForm">Add a holiday request</a></li>
            <li><a href="${pageContext.request.contextPath}/holidays?action=getAllHolidays">Holidays list</a></li>
            <li><a href="${pageContext.request.contextPath}/jobOffer?action=JobOfferList">Job offers list</a></li>
        </c:if>
        <c:if test="${user.role == 'Recruiter'}">
            <li><a href="${pageContext.request.contextPath}/jobOffer?action=addJobOfferForm">Add job offer</a></li>
            <li><a href="${pageContext.request.contextPath}/jobOffer?action=JobOfferList">Job offers list</a></li>
        </c:if><c:if test="${user.role == 'RH'}">
        <li><a href="${pageContext.request.contextPath}/employee?action=generateFamilyAllowanceStats">Statistics</a></li>
    </c:if>
    </ul>
</nav>
</body>
</html>
