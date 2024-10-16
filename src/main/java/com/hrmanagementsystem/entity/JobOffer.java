package com.hrmanagementsystem.entity;

import com.hrmanagementsystem.enums.JobOfferStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_offers")
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String description;

    private LocalDateTime publishedDate;
    private LocalDateTime expiredDate;
    @Enumerated(EnumType.STRING)
    private JobOfferStatus status;
    public JobOffer() {

    }

    public JobOffer(String title, String description, LocalDateTime publishedDate, LocalDateTime expiredDate, JobOfferStatus status) {
        this.title = title;
        this.description = description;
        this.publishedDate = publishedDate;
        this.expiredDate = expiredDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    public JobOfferStatus getStatus() {
        return status;
    }

    public void setStatus(JobOfferStatus status) {
        this.status = status;
    }
}

