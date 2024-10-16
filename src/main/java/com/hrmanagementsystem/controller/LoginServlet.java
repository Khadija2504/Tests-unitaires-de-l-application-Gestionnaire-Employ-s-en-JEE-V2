package com.hrmanagementsystem.controller;

import com.hrmanagementsystem.dao.implementations.UserDAO;
import com.hrmanagementsystem.dao.interfaces.UserInterface;
import com.hrmanagementsystem.entity.User;
import com.hrmanagementsystem.service.AuthService;
import com.hrmanagementsystem.service.EmployeeService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private EntityManagerFactory emf;
    protected UserInterface userDAO = new UserDAO();
    protected AuthService authService = new AuthService(userDAO);

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("hr_management_pu");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = authService.authenticate(email, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("loggedInUserId", user.getId());
                session.setAttribute("user", user);

                String redirectUrl;
                switch (user.getRole()) {
                    case Admin:
                        redirectUrl = "/employee?action=employeeList";
                        break;
                    case RH:
                        redirectUrl = "/employee?action=generateFamilyAllowanceStats";
                        break;
                    case Recruiter:
                    case Employee:
                        redirectUrl = "/jobOffer?action=JobOfferList";
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid user role");
                }
                response.sendRedirect(request.getContextPath() + redirectUrl);
            } else {
                request.setAttribute("errorMessage", "Invalid email or password");
                request.getRequestDispatcher("view/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred during login");
            request.getRequestDispatcher("view/login.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        emf.close();
    }
}
