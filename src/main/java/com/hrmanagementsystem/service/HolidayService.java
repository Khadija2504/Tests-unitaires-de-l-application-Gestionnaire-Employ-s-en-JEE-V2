package com.hrmanagementsystem.service;

import com.hrmanagementsystem.dao.implementations.HolidayDAO;
import com.hrmanagementsystem.dao.interfaces.HolidayInterface;
import com.hrmanagementsystem.entity.Holiday;
import com.hrmanagementsystem.entity.User;
import com.hrmanagementsystem.enums.HolidayStatus;

import javax.servlet.http.Part;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HolidayService {
    public static final String UPLOAD_DIR = "uploads";
    HolidayInterface holidayInterface;
    public HolidayService (HolidayInterface holidayInterface) {
        this.holidayInterface = holidayInterface;
    }

    public void addHoliday(String startDateStr, String endDateStr, String reason, String uploadFilePath,
                           Part filePart, User employee) throws ParseException, IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(startDateStr);
        Date endDate = dateFormat.parse(endDateStr);

        String fileName = extractFileName(filePart);
        String filePath = uploadFilePath + File.separator + fileName;
        filePart.write(filePath);

        int requestedDays = calculateDaysBetween(startDate, endDate) + 1;
        List<Holiday> acceptedHolidays = getAcceptedHolidaysForEmployee(employee);
        int takenDays = calculateTotalDays(acceptedHolidays);

        if (takenDays + requestedDays > 30) {
            throw new IllegalArgumentException("Total holidays cannot exceed one month (30 days). " +
                    "Days already taken: " + takenDays + ", Requested: " + requestedDays +
                    ", Available: " + (30 - takenDays));
        }

        Holiday holiday = new Holiday(startDate, endDate, reason, filePath, employee);
        holidayInterface.save(holiday);
    }

    public List<Holiday> getAcceptedHolidaysForEmployee(User employee) {
        return holidayInterface.getAcceptedHolidaysForEmployee(employee);
    }

    public Map<User, List<Holiday>> generateAbsenceReport(List<User> employees) {
        Map<User, List<Holiday>> employeeHolidays = new HashMap<>();
        for (User employee : employees) {
            List<Holiday> acceptedHolidays = getAcceptedHolidaysForEmployee(employee);
            if (!acceptedHolidays.isEmpty()) {
                employeeHolidays.put(employee, acceptedHolidays);
            }
        }
        return employeeHolidays;
    }

    private int calculateDaysBetween(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    private int calculateTotalDays(List<Holiday> holidays) {
        return holidays.stream()
                .mapToInt(holiday -> calculateDaysBetween(holiday.getStartDate(), holiday.getEndDate()) + 1)
                .sum();
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }

    public List<Holiday> getAllHolidays() {
        return holidayInterface.getAllHolidays();
    }

    public Holiday getById(int id) {
        return holidayInterface.getById(id);
    }

    public void update(int holidayId, String newStatus) {
        Holiday holiday = holidayInterface.getById(holidayId);
        if (holiday != null) {
            holiday.setStatus(HolidayStatus.valueOf(newStatus));
            holidayInterface.update(holiday);
        }
    }

    public void downloadJustification(Holiday holiday, OutputStream outputStream) throws IOException {
        if (holiday == null || holiday.getJustification() == null) {
            throw new IllegalArgumentException("Holiday or justification not found");
        }

        File downloadFile = new File(holiday.getJustification());
        if (!downloadFile.exists()) {
            throw new FileNotFoundException("Justification file not found");
        }

        try (FileInputStream inStream = new FileInputStream(downloadFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    public String getJustificationFileName(Holiday holiday) {
        if (holiday == null || holiday.getJustification() == null) {
            return null;
        }
        return new File(holiday.getJustification()).getName();
    }
}
