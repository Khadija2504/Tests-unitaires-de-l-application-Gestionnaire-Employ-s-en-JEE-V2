package com.hrmanagementsystem.controller;

import com.hrmanagementsystem.dao.implementations.ApplicationDAO;
import com.hrmanagementsystem.dao.implementations.JobOfferDAO;
import com.hrmanagementsystem.dao.implementations.NotificationDAO;
import com.hrmanagementsystem.dao.interfaces.ApplicationInterface;
import com.hrmanagementsystem.entity.Application;
import com.hrmanagementsystem.entity.JobOffer;
import com.hrmanagementsystem.entity.Notification;
import com.hrmanagementsystem.enums.ApplicationStatus;
import com.hrmanagementsystem.service.ApplicationService;
import com.hrmanagementsystem.util.EmailSender;

import javax.servlet.annotation.MultipartConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@MultipartConfig
public class ApplicationServlet extends HttpServlet {
    protected ApplicationInterface applicationDAO = new ApplicationDAO();
    protected ApplicationService applicationService = new ApplicationService(applicationDAO);

    private static final String UPLOAD_DIR = "uploads";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "addApplication":
                addApplication(req, resp);
                break;
            case "filterApplications":
                filterApplications(req, resp);
                break;
            case "updateApplicationStatus":
                updateApplicationStatus(req, resp);
                break;
        }
    }

    private void addApplication(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("candidateName");
        String description = req.getParameter("candidateEmail");
        String phoneNumber = req.getParameter("phoneNumber");
        LocalDateTime appliedDate = LocalDateTime.now();
        String applicationPath = req.getServletContext().getRealPath("");
        int jobOfferId = Integer.parseInt(req.getParameter("jobOfferId"));
        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
        Part filePart = req.getPart("resume");

        applicationService.save(title, description, phoneNumber, appliedDate, uploadFilePath, jobOfferId, filePart);
        resp.sendRedirect("jobOffer?action=JobOfferList");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "addApplicationForm":
                addApplicationForm(req, resp);
                break;
            case "getAllApplications":
                getAllApplications(req, resp);
                break;
            case "downloadResume":
                downloadResume(req, resp);
                break;
            case "filterApplications":
                filterApplications(req, resp);
                break;
        }
    }

    public void addApplicationForm (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("view/addApplication.jsp").forward(req, resp);
    }

    public void getAllApplications(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int jobOfferId = Integer.parseInt(req.getParameter("jobOfferId"));
        List<Application> applications = applicationService.getAllByJobOfferId(jobOfferId);

            for (Application app : applications) {
                File resumeFile = new File(app.getResume());
                if (resumeFile.exists()) {
                    app.setResume(resumeFile.getName());
                } else {
                    app.setResume("Resume not found");
                }
            }

            req.setAttribute("applications", applications);
            req.getRequestDispatcher("view/DisplayAllApplications.jsp").forward(req, resp);
    }

    public void filterApplications(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int jobOfferId = Integer.parseInt(req.getParameter("jobOfferId"));
        String statusParam = req.getParameter("status");
        ApplicationStatus status = ApplicationStatus.valueOf(statusParam);

        List<Application> filteredApplications = applicationService.getFilteredApplications(jobOfferId, status);

            for (Application app : filteredApplications) {
                File resumeFile = new File(app.getResume());
                if (resumeFile.exists()) {
                    app.setResume(resumeFile.getName());
                } else {
                    app.setResume("Resume not found");
                }
            }

            req.setAttribute("applications", filteredApplications);
            req.getRequestDispatcher("view/DisplayAllApplications.jsp").forward(req, resp);
    }

    public void downloadResume(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int applicationId = Integer.parseInt(req.getParameter("applicationId"));

        try {
            Application application = applicationService.getById(applicationId);
            if (application == null || application.getResume() == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Application or resume not found!");
                return;
            }

            File downloadFile = new File(application.getResume());
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment;filename=" + downloadFile.getName());

            try (OutputStream outStream = resp.getOutputStream()) {
                applicationService.downloadResume(applicationId, outStream);
            }
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error downloading resume: " + e.getMessage());
        }
    }
    private void updateApplicationStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int appId = Integer.parseInt(req.getParameter("applicationId"));
        String newStatus = req.getParameter("status");
        int jobOfferId = Integer.parseInt(req.getParameter("jobOfferId"));
System.out.println(jobOfferId);
        try {
            System.out.println(newStatus + "hello hello");
            applicationService.updateApplicationStatus(appId, newStatus);
            System.out.println(newStatus + "hello hello");
            resp.sendRedirect("application?action=getAllApplications&jobOfferId=" + jobOfferId);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Application not found")) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while updating the application status");
        }
    }
}
