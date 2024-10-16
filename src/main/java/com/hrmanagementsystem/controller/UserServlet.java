package com.hrmanagementsystem.controller;

import com.hrmanagementsystem.dao.implementations.EmployeeDAO;
import com.hrmanagementsystem.dao.interfaces.EmployeeInterface;
import com.hrmanagementsystem.entity.User;
import com.hrmanagementsystem.enums.Role;
import com.hrmanagementsystem.service.EmployeeService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    protected EmployeeInterface employeeDAO = new EmployeeDAO();
    protected EmployeeService employeeService = new EmployeeService(employeeDAO);
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "addEmployee":
                addEmployee(request, response);
                break;
            case "updateEmployee":
                updateEmployee(request, response);
                break;
//            case "searchEmployee":
//                searchEmployee(request, response);
//                break;
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "addEmployeeForm":
                addEmployeeForm(request, response);
                break;
            case "editEmployee":
                editEmployee(request, response);
                break;
            case "deleteEmployee":
                deleteEmployee(request, response);
                break;
            case "employeeList":
                displayEmployeesList(request, response);
                break;
            case "employeeProfile":
                profile(request, response);
                break;
            case "generateFamilyAllowanceStats":
                generateFamilyAllowanceStats(request, response);
                break;
        }
    }

    private void addEmployeeForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/AddEmployee.jsp").forward(request, response);
    }

    private void editEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User employee = employeeService.getById(id);
        request.setAttribute("employee", employee);
        request.getRequestDispatcher("view/editEmployee.jsp").forward(request, response);
    }

    private void generateFamilyAllowanceStats(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> stats = employeeService.generateFamilyAllowanceStats();

        request.setAttribute("monthlyStats", stats.get("monthlyStats"));
        request.setAttribute("employeeStats", stats.get("employeeStats"));
        request.setAttribute("employees", stats.get("employees"));

        request.getRequestDispatcher("view/Statistics.jsp").forward(request, response);
    }

    private void addEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phoneNumber = request.getParameter("phoneNumber");
        int salary = Integer.parseInt(request.getParameter("salary"));
        String birthdayStr = request.getParameter("birthday");
        String hireDateStr = request.getParameter("hireDate");
        String position = request.getParameter("position");
        int kidsNum = Integer.parseInt(request.getParameter("kidsNum"));
        String situation = request.getParameter("situation");
        String department = request.getParameter("department");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String nssu = request.getParameter("nssu");

        try {
            boolean added = employeeService.save(firstName, lastName, phoneNumber, salary, birthdayStr, hireDateStr,
                    position, kidsNum, situation, department, email, password, nssu);

            if (added) {
                response.sendRedirect("employee?action=employeeList");
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("errorMessage", "Failed to add employee. Email or nsssu may already exist.");
                response.sendRedirect("employee?action=addEmployeeForm");
            }
        } catch (ParseException e) {
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Invalid date format.");
            response.sendRedirect("employee?action=addEmployeeForm");
        }
    }

    protected void deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        employeeService.delete(id);
        response.sendRedirect("employee?action=employeeList");
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phoneNumber = request.getParameter("phoneNumber");
        int salary = Integer.parseInt(request.getParameter("salary"));
        String birthdayStr = request.getParameter("birthday");
        String hireDateStr = request.getParameter("hireDate");
        String position = request.getParameter("position");
        int kidsNum = Integer.parseInt(request.getParameter("kidsNum"));
        String situation = request.getParameter("situation");
        String department = request.getParameter("department");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String nssu = request.getParameter("nssu");

        try {
            boolean updated = employeeService.update(id, firstName, lastName, phoneNumber, salary, birthdayStr, hireDateStr,
                    position, kidsNum, situation, department, email, password, nssu);

            if (updated) {
                response.sendRedirect("employee?action=employeeList");
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("errorMessage", "Failed to update employee. Email may already exist.");
                response.sendRedirect("employee?action=editEmployee&id=" + id);
            }
        } catch (ParseException e) {
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Invalid date format.");
            response.sendRedirect("employee?action=editEmployee&id=" + id);
        }
    }

    private void displayEmployeesList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> employeeList = employeeService.getAll();
        request.setAttribute("employees", employeeList);
        System.out.println(employeeList);
        request.getRequestDispatcher("view/DisplayAllEmployees.jsp").forward(request, response);
    }

    private void profile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer loggedInUserId = (Integer) request.getSession().getAttribute("loggedInUserId");
        User employee = employeeService.getById(loggedInUserId);

        if (employee == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found");
            return;
        }

        int familyAllowance = employeeService.calculateFamilyAllowance(employee.getSalary(), employee.getKidsNum());

        Map<String, Object> familyAllowanceDetails = new HashMap<>();
        familyAllowanceDetails.put("amount", familyAllowance);
        familyAllowanceDetails.put("num_children", employee.getKidsNum());
        familyAllowanceDetails.put("situation", employee.getSituation());

        request.setAttribute("employee", employee);
        request.setAttribute("familyAllowanceDetails", familyAllowanceDetails);
        request.getRequestDispatcher("view/profile.jsp").forward(request, response);
    }
}
