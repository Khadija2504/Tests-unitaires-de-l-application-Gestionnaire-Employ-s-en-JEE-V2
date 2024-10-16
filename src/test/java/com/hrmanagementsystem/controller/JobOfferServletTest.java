//package com.hrmanagementsystem.controller;
//
//import com.hrmanagementsystem.dao.JobOfferDAO;
//import com.hrmanagementsystem.entity.JobOffer;
//import com.hrmanagementsystem.enums.JobOfferStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class JobOfferServletTest {
//
//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private HttpServletResponse response;
//
//    @Mock
//    private JobOfferDAO jobOfferDAO;
//
//    @Mock
//    private ScheduledExecutorService scheduler;
//
//    private JobOfferServlet servlet;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        MockitoAnnotations.openMocks(this);
//        servlet = new JobOfferServlet(jobOfferDAO);
//
//        // Inject the mocked scheduler
//        java.lang.reflect.Field schedulerField = JobOfferServlet.class.getDeclaredField("scheduler");
//        schedulerField.setAccessible(true);
//        schedulerField.set(servlet, scheduler);
//    }
//
//    @Test
//    void testAddJobOffer() throws ServletException, IOException {
//        // Arrange
//        String title = "Software Developer";
//        String description = "We are looking for a skilled software developer";
//        String expiredDate = LocalDate.now().plusDays(30).toString();
//
//        when(request.getParameter("title")).thenReturn(title);
//        when(request.getParameter("description")).thenReturn(description);
//        when(request.getParameter("expiredDate")).thenReturn(expiredDate);
//
//        // Act
//        servlet.addJobOffer(request, response);
//
//        // Assert
//        verify(response).sendRedirect("jobOffer?action=addJobOfferForm");
//
//        ArgumentCaptor<JobOffer> jobOfferCaptor = ArgumentCaptor.forClass(JobOffer.class);
//        JobOfferDAO.save(jobOfferCaptor.capture());
//
//        JobOffer capturedJobOffer = jobOfferCaptor.getValue();
//        assertEquals(title, capturedJobOffer.getTitle());
//        assertEquals(description, capturedJobOffer.getDescription());
//        assertEquals(JobOfferStatus.Open, capturedJobOffer.getStatus());
//        assertEquals(LocalDate.parse(expiredDate).atStartOfDay(), capturedJobOffer.getExpiredDate());
//
//        verify(scheduler).schedule(any(Runnable.class), anyLong(), eq(TimeUnit.SECONDS));
//    }
//
//    @Test
//    void testDeleteJobOffer() throws ServletException, IOException {
//        // Arrange
//        int jobOfferId = 1;
//        when(request.getParameter("id")).thenReturn(String.valueOf(jobOfferId));
//
//        // Act
//        servlet.deleteJobOffer(request, response);
//
//        // Assert
//        JobOfferDAO.delete(jobOfferId);
//        verify(response).sendRedirect("JobOfferList?action=jobOfferList");
//    }
//}