package com.hrmanagementsystem.controller;

import com.hrmanagementsystem.dao.implementations.JobOfferDAO;
import com.hrmanagementsystem.dao.interfaces.JobOfferInterface;
import com.hrmanagementsystem.entity.JobOffer;
import com.hrmanagementsystem.service.JobOfferService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class JobOfferServlet extends HttpServlet {
    private ScheduledExecutorService scheduler;
    protected JobOfferInterface jobOfferDAO = new JobOfferDAO();
    protected JobOfferService jobOfferService = new JobOfferService(jobOfferDAO);

    @Override
    public void init() throws ServletException {
        super.init();
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void destroy() {
        super.destroy();
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "addJobOffer":
                addJobOffer(req,resp);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "addJobOfferForm":
                addJobOfferForm(req, resp);
                break;
            case "deleteJobOffer":
                deleteJobOffer(req, resp);
                break;
            case "JobOfferList":
                JobOfferList(req, resp);
                break;
        }
    }

    private void addJobOfferForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("view/addJobOffer.jsp").forward(req, resp);
    }

    protected void addJobOffer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String expiredDateStr = request.getParameter("expiredDate");

        try {
            JobOffer jobOffer = jobOfferService.addJobOffer(title, description, expiredDateStr);
            System.out.println("Job offer added: " + jobOffer);
            response.sendRedirect("jobOffer?action=addJobOfferForm");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error adding job offer: " + e.getMessage());
            request.getRequestDispatcher("view/addJobOfferForm.jsp").forward(request, response);
        }
    }

    public void deleteJobOffer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        jobOfferService.delete(id);
        resp.sendRedirect("JobOfferList?action=jobOfferList");
    }

    private void JobOfferList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<JobOffer> jobOffers = jobOfferService.getAll();
        req.setAttribute("jobOffers", jobOffers);
        req.getRequestDispatcher("view/DisplayAllJobOffers.jsp").forward(req, resp);
    }

    private void handleUpdateJobOfferStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jobOfferIdStr = request.getParameter("jobOfferId");
        if (jobOfferIdStr != null && !jobOfferIdStr.isEmpty()) {
            try {
                int jobOfferId = Integer.parseInt(jobOfferIdStr);
                jobOfferService.updateJobOfferStatus(jobOfferId);
                response.sendRedirect("jobOffer?action=listJobOffers");
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid job offer ID");
                request.getRequestDispatcher("view/error.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Job offer ID is required");
            request.getRequestDispatcher("view/error.jsp").forward(request, response);
        }
    }
}
