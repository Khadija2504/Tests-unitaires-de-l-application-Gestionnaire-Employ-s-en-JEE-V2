package com.hrmanagementsystem.controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.hrmanagementsystem.entity.User;
import com.hrmanagementsystem.enums.Role;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String requestURI = httpRequest.getRequestURI();
        System.out.println("Filtering request: " + requestURI);

        if (isPublicResource(requestURI)) {
            System.out.println("Public resource, allowing access");
            chain.doFilter(request, response);
            return;
        }

        if (session == null || session.getAttribute("user") == null) {
            System.out.println("No session or user, redirecting to login");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        System.out.println("User role: " + user.getRole());
        if (!hasAccess(user.getRole(), requestURI)) {
            System.out.println("Access denied for user role: " + user.getRole() + " to URI: " + requestURI);
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        System.out.println("Access granted, continuing filter chain");
        chain.doFilter(request, response);
    }

    private boolean isPublicResource(String requestURI) {
        return requestURI.endsWith("login") ||
                requestURI.endsWith("loginForm") ||
                requestURI.endsWith("logout") ||
                requestURI.endsWith("LoginServlet") ||
                requestURI.endsWith(".css") ||
                requestURI.endsWith(".js") ||
                requestURI.endsWith(".png") ||
                requestURI.endsWith(".jpg");
    }

    private boolean hasAccess(Role role, String requestURI) {
        switch (role) {
            case Admin:
                return requestURI.contains("employee") || requestURI.contains("holidays");
            case RH:
                return requestURI.contains("employee");
            case Employee:
                return requestURI.contains("application") || requestURI.contains("jobOffer") || requestURI.contains("holidays") || requestURI.contains("employee");
            case Recruiter:
                return requestURI.contains("jobOffer") || requestURI.contains("application") || requestURI.contains("addJobOfferForm");
            default:
                return false;
        }
    }
}
