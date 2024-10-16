<%@ page import="com.hrmanagementsystem.enums.ApplicationStatus" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>All Applications</title>
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
    tr:nth-child(even) {
      background-color: #f9f9f9;
    }
    .download-link {
      color: blue;
      text-decoration: underline;
      cursor: pointer;
    }
  </style>
</head>
<body>
<%@ include file="navbar.jsp" %>
<h1>All Applications</h1>
<form action="${pageContext.request.contextPath}/application?action=filterApplications" method="post">
  <input type="hidden" name="jobOfferId" value="${param.jobOfferId}">

  <label for="status">Filter by status:</label>
  <select name="status" id="status">
    <option value="">All Statuses</option>
    <c:forEach var="status" items="<%= ApplicationStatus.values() %>">
      <option value="${status}">${status}</option>
    </c:forEach>
  </select>

  <input type="submit" value="Filter">
</form>
<c:choose>
  <c:when test="${not empty applications}">
    <table>
      <thead>
      <tr>
        <th>Candidate Name</th>
        <th>Email</th>
        <th>Phone Number</th>
        <th>Applied Date</th>
        <th>Job Title</th>
        <th>Status</th>
        <th>Resume</th>
        <c:if test="${user.role == 'Recruiter'}">
          <th>Update status</th>
        </c:if>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${applications}" var="app">
        <tr>
          <td>${app.candidateName}</td>
          <td>${app.candidateEmail}</td>
          <td>${app.candidatePhone}</td>
          <td>${app.appliedDate}</td>
          <td>${app.jobOffer.title}</td>
          <td>${app.status}</td>
          <td>
            <c:choose>
              <c:when test="${not empty app.resume}">
                <a href="application?action=downloadResume&applicationId=${app.id}" class="download-link">
                  Download Resume (${app.resume})
                </a>
              </c:when>
              <c:otherwise>
                Resume not available
              </c:otherwise>
            </c:choose>
          </td>
            <c:if test="${user.role == 'Recruiter'}">
          <td>
            <form action="application?action=updateApplicationStatus&applicationId=${app.id}&jobOfferId=${app.jobOffer.id}" method="post">
              <input type="hidden" name="status" value="<%= ApplicationStatus.Accepted%>" required>
              <button type="submit">Accept it</button>
            </form>
            <form action="application?action=updateApplicationStatus&applicationId=${app.id}&jobOfferId=${app.jobOffer.id}" method="post">
              <input type="hidden" name="status" value="<%= ApplicationStatus.Refused%>" required>
              <button type="submit">Refuse it</button>
            </form>
          </td>
          </c:if>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:when>
  <c:otherwise>
    <p>No applications found for this job offer.</p>
  </c:otherwise>
</c:choose>
</body>
</html>