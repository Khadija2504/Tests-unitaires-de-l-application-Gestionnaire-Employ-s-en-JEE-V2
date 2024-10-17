package com.hrmanagementsystem.service;

import com.hrmanagementsystem.dao.interfaces.JobOfferInterface;
import com.hrmanagementsystem.entity.JobOffer;
import com.hrmanagementsystem.enums.JobOfferStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobOfferServiceTest {

    @Mock
    private JobOfferInterface jobOfferInterface;

    @Mock
    private ScheduledExecutorService scheduler;

    private JobOfferService jobOfferService;

    @BeforeEach
    void setUp() {
        jobOfferService = new JobOfferService(jobOfferInterface, scheduler);
    }

    @AfterEach
    void tearDown() {
        jobOfferService.shutdown();
    }

    @Test
    void testAddJobOffer() {
        String title = "Software Engineer";
        String description = "Java developer needed";
        String expiredDateStr = LocalDate.now().plusDays(30).toString();

        when(scheduler.schedule(any(Runnable.class), anyLong(), any(TimeUnit.class)))
                .thenReturn(mock(ScheduledFuture.class));

        JobOffer jobOffer = jobOfferService.addJobOffer(title, description, expiredDateStr);

        assertNotNull(jobOffer);
        assertEquals(title, jobOffer.getTitle());
        assertEquals(description, jobOffer.getDescription());
        assertEquals(JobOfferStatus.Open, jobOffer.getStatus());
        assertEquals(LocalDate.parse(expiredDateStr).atStartOfDay(), jobOffer.getExpiredDate());
        verify(jobOfferInterface).save(any(JobOffer.class));
        verify(scheduler).schedule(any(Runnable.class), anyLong(), eq(TimeUnit.SECONDS));
    }

    @Test
    void testGetAll() {
        List<JobOffer> expectedOffers = Arrays.asList(
                new JobOffer("Title1", "Desc1", LocalDateTime.now(), LocalDateTime.now().plusDays(30), JobOfferStatus.Open),
                new JobOffer("Title2", "Desc2", LocalDateTime.now(), LocalDateTime.now().plusDays(60), JobOfferStatus.Open)
        );
        when(jobOfferInterface.getAll()).thenReturn(expectedOffers);

        List<JobOffer> actualOffers = jobOfferService.getAll();

        assertEquals(expectedOffers, actualOffers);
        verify(jobOfferInterface).getAll();
    }

    @Test
    void testUpdateJobOfferStatus() {
        int jobOfferId = 1;
        JobOffer jobOffer = new JobOffer("Title", "Desc", LocalDateTime.now(), LocalDateTime.now().plusDays(30), JobOfferStatus.Open);
        when(jobOfferInterface.getById(jobOfferId)).thenReturn(jobOffer);

        jobOfferService.updateJobOfferStatus(jobOfferId);

        assertEquals(JobOfferStatus.Expired, jobOffer.getStatus());
        verify(jobOfferInterface).update(jobOffer);
    }

    @Test
    void testUpdate() {
        JobOffer jobOffer = new JobOffer("Title", "Desc", LocalDateTime.now(), LocalDateTime.now().plusDays(30), JobOfferStatus.Open);

        jobOfferService.update(jobOffer);

        assertEquals(JobOfferStatus.Expired, jobOffer.getStatus());
        verify(jobOfferInterface).update(jobOffer);
    }

    @Test
    void testGetByStatus() {
        JobOfferStatus status = JobOfferStatus.Open;
        List<JobOffer> expectedOffers = Arrays.asList(
                new JobOffer("Title1", "Desc1", LocalDateTime.now(), LocalDateTime.now().plusDays(30), JobOfferStatus.Open),
                new JobOffer("Title2", "Desc2", LocalDateTime.now(), LocalDateTime.now().plusDays(60), JobOfferStatus.Open)
        );
        when(jobOfferInterface.getByStatus(status)).thenReturn(expectedOffers);

        List<JobOffer> actualOffers = jobOfferService.getByStatus(status);

        assertEquals(expectedOffers, actualOffers);
        verify(jobOfferInterface).getByStatus(status);
    }

    @Test
    void testDelete() {
        int id = 1;

        jobOfferService.delete(id);

        verify(jobOfferInterface).delete(id);
    }
}