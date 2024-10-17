package com.hrmanagementsystem.service;

import com.hrmanagementsystem.dao.interfaces.ApplicationInterface;
import com.hrmanagementsystem.dao.interfaces.EmailSenderInterface;
import com.hrmanagementsystem.dao.interfaces.NotificationInterface;
import com.hrmanagementsystem.entity.Application;
import com.hrmanagementsystem.entity.Notification;
import com.hrmanagementsystem.enums.ApplicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationInterface applicationInterface;

    @Mock
    private NotificationInterface notificationInterface;

    @Mock
    private EmailSenderInterface emailSenderInterface;

    @Mock
    private Part filePart;

    @InjectMocks
    private ApplicationService applicationService;

    @BeforeEach
    void setUp() {
        applicationService = new ApplicationService(applicationInterface, notificationInterface, emailSenderInterface);
    }

    @Test
    void testGetById() {
        Application expectedApplication = new Application();
        when(applicationInterface.getById(1)).thenReturn(expectedApplication);

        Application result = applicationService.getById(1);

        assertEquals(expectedApplication, result);
        verify(applicationInterface).getById(1);
    }

    @Test
    void testSave() throws IOException {
        // Mocking necessary objects and methods
        String uploadFilePath = System.getProperty("java.io.tmpdir");
        when(filePart.getHeader("content-disposition")).thenReturn("filename=\"test.pdf\"");

        // Call the method
        applicationService.save("Test Title", "Test Description", "1234567890",
                LocalDateTime.now(), uploadFilePath, 1, filePart);

        // Verify that the save method was called on the applicationInterface
        verify(applicationInterface).save(any(Application.class));
    }

    @Test
    void testGetAllByJobOfferId() {
        List<Application> expectedApplications = Arrays.asList(new Application(), new Application());
        when(applicationInterface.getAllByJobOfferId(1)).thenReturn(expectedApplications);

        List<Application> result = applicationService.getAllByJobOfferId(1);

        assertEquals(expectedApplications, result);
        verify(applicationInterface).getAllByJobOfferId(1);
    }

    @Test
    void testGetFilteredApplications() {
        List<Application> expectedApplications = Arrays.asList(new Application(), new Application());
        when(applicationInterface.getFilteredApplications(1, ApplicationStatus.Pending)).thenReturn(expectedApplications);

        List<Application> result = applicationService.getFilteredApplications(1, ApplicationStatus.Pending);

        assertEquals(expectedApplications, result);
        verify(applicationInterface).getFilteredApplications(1, ApplicationStatus.Pending);
    }

    @Test
    void testUpdateApplicationStatus() {
        Application application = new Application();
        application.setCandidateEmail("test@example.com");
        when(applicationInterface.getById(1)).thenReturn(application);

        applicationService.updateApplicationStatus(1, "Accepted");

        verify(applicationInterface).updateStatus(any(Application.class));
        verify(notificationInterface).save(any(Notification.class));
        verify(emailSenderInterface).sendEmail(eq("test@example.com"), anyString(), anyString());
    }

    @Test
    void testUpdateApplicationStatus_InvalidStatus() {
        Application application = new Application();
        when(applicationInterface.getById(1)).thenReturn(application);

        assertThrows(IllegalArgumentException.class, () -> {
            applicationService.updateApplicationStatus(1, "InvalidStatus");
        });
    }

    @Test
    void testDownloadResume() throws IOException {
        File tempFile = File.createTempFile("test-resume", ".pdf");
        tempFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("This is a test resume content");
        }

        Application application = new Application();
        application.setResume(tempFile.getAbsolutePath());

        when(applicationInterface.getById(1)).thenReturn(application);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        applicationService.downloadResume(1, outputStream);

        assertTrue(outputStream.size() > 0, "Output stream should not be empty");

        String downloadedContent = outputStream.toString();
        String originalContent = new String(Files.readAllBytes(tempFile.toPath()));
        assertEquals(originalContent, downloadedContent, "Downloaded content should match the original file content");

        verify(applicationInterface).getById(1);
    }

    @Test
    void testDownloadResume_ApplicationNotFound() {
        when(applicationInterface.getById(1)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            applicationService.downloadResume(1, new ByteArrayOutputStream());
        });
    }

    @Test
    void testDownloadResume_ResumeNotFound() {
        Application application = new Application();
        application.setResume("/path/to/non-existent/file.pdf");

        when(applicationInterface.getById(1)).thenReturn(application);

        assertThrows(IOException.class, () -> {
            applicationService.downloadResume(1, new ByteArrayOutputStream());
        });
    }
}