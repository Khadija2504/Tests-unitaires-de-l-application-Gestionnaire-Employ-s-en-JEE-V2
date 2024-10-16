package com.hrmanagementsystem.service;

import com.hrmanagementsystem.dao.implementations.ApplicationDAO;
import com.hrmanagementsystem.dao.implementations.JobOfferDAO;
import com.hrmanagementsystem.dao.implementations.NotificationDAO;
import com.hrmanagementsystem.dao.interfaces.ApplicationInterface;
import com.hrmanagementsystem.dao.interfaces.NotificationInterface;
import com.hrmanagementsystem.entity.Application;
import com.hrmanagementsystem.entity.JobOffer;
import com.hrmanagementsystem.entity.Notification;
import com.hrmanagementsystem.enums.ApplicationStatus;
import com.hrmanagementsystem.util.EmailSender;

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class ApplicationService {
   protected ApplicationInterface applicationInterface ;
   protected NotificationInterface notificationInterface = new NotificationDAO();
    public ApplicationService (ApplicationInterface applicationInterface) {
        this.applicationInterface = applicationInterface;
    }

    public Application getById(int id) {
      return  applicationInterface.getById(id);
    }

    public void save(String title, String description, String phoneNumber, LocalDateTime appliedDate, String uploadFilePath, int jobOfferId, Part filePart) throws IOException {
        File uploadDir = new File(uploadFilePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileName = extractFileName(filePart);
        String filePath = uploadFilePath + File.separator + fileName;
        filePart.write(filePath);

        JobOfferDAO jobOfferDAO = new JobOfferDAO();
        JobOffer jobOffer = jobOfferDAO.getById(jobOfferId);

        if (jobOffer == null) {
            throw new IllegalArgumentException("Invalid Job Offer ID");
        }

        Application application = new Application(title, description, filePath, appliedDate, phoneNumber, jobOffer, ApplicationStatus.Pending);
        applicationInterface.save(application);
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

    public List<Application> getAllByJobOfferId(int jobOfferId) {
        List<Application> applications = applicationInterface.getAllByJobOfferId(jobOfferId);
        return applications;
    }

    public List<Application> getFilteredApplications(int jobOfferId, ApplicationStatus status) {
        List<Application> applications = applicationInterface.getFilteredApplications(jobOfferId, status);
        return applications;
    }

    public void updateApplicationStatus(int appId, String newStatus) throws IllegalArgumentException {
        Application application = getById(appId);
        if (application == null) {
            throw new IllegalArgumentException("Application not found");
        }

        try {
            ApplicationStatus status = ApplicationStatus.valueOf(newStatus);
            application.setStatus(status);
            applicationInterface.updateStatus(application);

            String candidateEmail = application.getCandidateEmail();
            String message;
            String subject;

            if (status == ApplicationStatus.Accepted) {
                message = "Your application has been accepted for the job offer that you applied!";
                subject = "Application accepted";
            } else {
                message = "Your application has been refused for the job offer that you applied. Good luck in the next job offer!";
                subject = "Application refused";
            }

            EmailSender.sendEmail(candidateEmail, subject, message);

            Notification notification = new Notification(message, new Date());
            notificationInterface.save(notification);

            System.out.println("Notification saved" + notification);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status");
        }
    }

    public void downloadResume(int applicationId, OutputStream outputStream) throws IOException {
        Application application = applicationInterface.getById(applicationId);
        if (application == null || application.getResume() == null) {
            throw new IllegalArgumentException("Application or resume not found!");
        }

        File downloadFile = new File(application.getResume());
        if (!downloadFile.exists()) {
            throw new IOException("Resume file not found!");
        }

        try (FileInputStream inStream = new FileInputStream(downloadFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

}
