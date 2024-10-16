<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Submit Job Application</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      line-height: 1.6;
      color: #333;
      max-width: 600px;
      margin: 0 auto;
      padding: 20px;
      background-color: #f4f4f4;
    }
    h1 {
      color: #2c3e50;
      text-align: center;
      margin-bottom: 30px;
    }
    form {
      background-color: #ffffff;
      padding: 30px;
      border-radius: 8px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }
    label {
      display: block;
      margin-bottom: 5px;
      font-weight: bold;
    }
    input[type="text"],
    input[type="email"],
    input[type="file"] {
      width: 100%;
      padding: 10px;
      margin-bottom: 20px;
      border: 1px solid #ddd;
      border-radius: 4px;
      box-sizing: border-box;
    }
    input[type="file"] {
      padding: 5px;
    }
    button[type="submit"] {
      background-color: #3498db;
      color: #ffffff;
      padding: 12px 20px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 16px;
      width: 100%;
    }
    button[type="submit"]:hover {
      background-color: #2980b9;
    }
  </style>
</head>
<body>
<h1>Submit Job Application</h1>

<form action="application?action=addApplication" method="post" enctype="multipart/form-data">
  <label for="candidateName">Full Name:</label>
  <input name="candidateName" type="text" id="candidateName" required>

  <label for="candidateEmail">Email Address:</label>
  <input name="candidateEmail" type="email" id="candidateEmail" required>

  <label for="phoneNumber">Phone Number:</label>
  <input name="phoneNumber" type="text" id="phoneNumber" required>

  <label for="resume">Resume (PDF or Word document):</label>
  <input name="resume" type="file" id="resume" accept=".pdf,.doc,.docx" required>

  <input type="hidden" name="jobOfferId" value="${param.jobOfferId}">

  <button type="submit">Submit Application</button>
</form>
</body>
</html>