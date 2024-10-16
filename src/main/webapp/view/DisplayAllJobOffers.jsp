<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Job Offer List</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
<%@ include file="navbar.jsp" %>
<h1>Job Offer List</h1>

<table>
    <thead>
    <tr>
        <th>Title</th>
        <th>Description</th>
        <th>Published Date</th>
        <th>Status</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="jobOffer" items="${jobOffers}">
        <tr>
            <td>${jobOffer.title}</td>
            <td>${jobOffer.description}</td>
            <td>${jobOffer.publishedDate}</td>
            <td>${jobOffer.status}</td>
            <td>
                <c:if test="${user.role == 'Employee'}">
                    <a href="application?action=addApplicationForm&jobOfferId=${jobOffer.id}">Apply</a>
                </c:if>
                <c:if test="${user.role == 'Recruiter'}">
                    <a href="application?action=getAllApplications&jobOfferId=${jobOffer.id}">All applications</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<p><a href="jobOffer?action=addJobOfferForm">Add New Job Offer</a></p>
</body>
</html>