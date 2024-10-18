package com.hrmanagementsystem.controller;

import com.hrmanagementsystem.dao.implementations.EmployeeDAO;
import com.hrmanagementsystem.dao.implementations.HolidayDAO;
import com.hrmanagementsystem.dao.interfaces.EmployeeInterface;
import com.hrmanagementsystem.dao.interfaces.HolidayInterface;
import com.hrmanagementsystem.entity.Holiday;
import com.hrmanagementsystem.entity.User;
import com.hrmanagementsystem.service.EmployeeService;
import com.hrmanagementsystem.service.HolidayService;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.*;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@MultipartConfig
public class HolidayServlet extends HttpServlet {
    protected HolidayInterface holidayDAO = new HolidayDAO();
    protected EmployeeInterface employeeDAO = new EmployeeDAO();
    protected HolidayService holidayService = new HolidayService(holidayDAO, employeeDAO);
    protected EmployeeService employeeService = new EmployeeService(employeeDAO);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "addHoliday":
                addHoliday(req, resp);
                break;
            case "updateHoliday":
                updateHoliday(req, resp);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "addHolidayForm":
                addHolidayForm(req, resp);
                break;
            case "getAllHolidays":
                getAllHolidays(req, resp);
                break;
            case "downloadJustification":
                downloadJustification(req, resp);
                break;
            case "generateMonthlyReport":
                generateAbsenceReport(req, resp);
                break;
        }
    }

    private void addHoliday(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");
        String reason = req.getParameter("reason");

        String holidayPath = req.getServletContext().getRealPath("");
        String uploadFilePath = holidayPath + File.separator + holidayService.UPLOAD_DIR;

        File uploadDir = new File(uploadFilePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        Part filePart = req.getPart("justification");

        Integer loggedInUserId = (Integer) req.getSession().getAttribute("loggedInUserId");
        User employee = employeeService.getById(loggedInUserId);

        if (employee == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid employee ID");
            return;
        }

        try {
            holidayService.addHoliday(startDateStr, endDateStr, reason, uploadFilePath, filePart, employee);
            resp.sendRedirect("holidays?action=getAllHolidays");
        } catch (ParseException e) {
            req.setAttribute("errorMessage", "Invalid date format");
            req.getRequestDispatcher("view/addHoliday.jsp").forward(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("view/addHoliday.jsp").forward(req, resp);
        }
    }

    private void generateAbsenceReport(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> employees = employeeService.getAll();
        Map<User, List<Holiday>> employeeHolidays = holidayService.generateAbsenceReport(employees);

        req.setAttribute("employeeHolidays", employeeHolidays);
        req.getRequestDispatcher("/view/report.jsp").forward(req, resp);
    }

    private void addHolidayForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("view/addHoliday.jsp").forward(req, resp);
    }

    private void getAllHolidays(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Holiday> holidays = holidayService.getAllHolidays();
        req.setAttribute("holidays", holidays);
        req.getRequestDispatcher("view/displayAllHolidays.jsp").forward(req, resp);
    }
    private void editHoliday(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Holiday holiday = holidayService.getById(id);
    }

    public void downloadJustification(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int holidayId = Integer.parseInt(req.getParameter("holidayId"));
            Holiday holiday = holidayService.getById(holidayId);

            if (holiday == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Holiday not found");
                return;
            }

            String fileName = holidayService.getJustificationFileName(holiday);
            if (fileName == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Justification not found");
                return;
            }

            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            try (OutputStream outStream = resp.getOutputStream()) {
                holidayService.downloadJustification(holiday, outStream);
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid holiday ID");
        } catch (IllegalArgumentException | FileNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error downloading file");
        }
    }

    private void updateHoliday(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int holidayId = Integer.parseInt(req.getParameter("holidayId"));
        String newStatus = req.getParameter("status");
        Integer loggedInUserId = (Integer) req.getSession().getAttribute("loggedInUserId");

        try {
            holidayService.update(holidayId, newStatus, loggedInUserId);
            resp.sendRedirect("holidays?action=getAllHolidays");
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid status");
        }
    }
}
