package com.hrmanagementsystem.service;

import com.hrmanagementsystem.dao.interfaces.JobOfferInterface;
import com.hrmanagementsystem.entity.JobOffer;
import com.hrmanagementsystem.enums.JobOfferStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JobOfferService {
    private ScheduledExecutorService scheduler;
    JobOfferInterface jobOfferInterface;
    public JobOfferService(JobOfferInterface jobOfferInterface) {
        this.jobOfferInterface = jobOfferInterface;
    }

    public JobOffer addJobOffer(String title, String description, String expiredDateStr) {
        JobOffer jobOffer = new JobOffer(title, description, LocalDateTime.now(),
                LocalDate.parse(expiredDateStr).atStartOfDay(),
                JobOfferStatus.Open);

        jobOfferInterface.save(jobOffer);

        scheduleJobOfferExpiration(jobOffer);

        return jobOffer;
    }

    private void scheduleJobOfferExpiration(JobOffer jobOffer) {
        long delayInSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), jobOffer.getExpiredDate());
        scheduler.schedule(() -> {
            jobOffer.setStatus(JobOfferStatus.Expired);
            jobOfferInterface.update(jobOffer);
        }, delayInSeconds, TimeUnit.SECONDS);
    }

    public List<JobOffer> getAll() {
        return jobOfferInterface.getAll();
    }

    public void updateJobOfferStatus(int jobOfferId) {
        JobOffer jobOffer = jobOfferInterface.getById(jobOfferId);
        if (jobOffer != null && jobOffer.getStatus() == JobOfferStatus.Open) {
            jobOffer.setStatus(JobOfferStatus.Expired);
            jobOfferInterface.update(jobOffer);
        }
    }

    public void update(JobOffer jobOffer) {
        if (jobOffer != null && jobOffer.getStatus() == JobOfferStatus.Open) {
            jobOffer.setStatus(JobOfferStatus.Expired);
            jobOfferInterface.update(jobOffer);
        }
    }

    public List<JobOffer> getByStatus(JobOfferStatus status) {
        return jobOfferInterface.getByStatus(status);
    }

    public void delete(int id) {
        jobOfferInterface.delete(id);
    }

}
