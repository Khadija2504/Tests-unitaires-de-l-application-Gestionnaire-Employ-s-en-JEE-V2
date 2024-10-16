<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add New Job Offer</title>
    <style>
        form {
            max-width: 500px;
            margin: 0 auto;
        }
        label {
            display: block;
            margin-top: 10px;
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 5px;
            margin-top: 5px;
        }
        input[type="submit"] {
            margin-top: 10px;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<%@ include file="navbar.jsp"%>
<h1>Add New Job Offer</h1>

<form action="jobOffer?action=addJobOffer" method="post">
    <input type="hidden" name="action" value="addJobOffer">

    <label for="title">Job Title:</label>
    <input type="text" id="title" name="title" required>

    <label for="expiredDate">Expired date:</label>
    <input type="date" id="expiredDate" name="expiredDate" required>

    <label for="description">Job Description:</label>
    <textarea id="description" name="description" rows="5" required></textarea>

    <input type="submit" value="Add Job Offer">
</form>

<p><a href="jobOffer?action=JobOfferList">Back to Job Offer List</a></p>
</body>
</html>