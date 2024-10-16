package com.hrmanagementsystem.entity;

import com.hrmanagementsystem.enums.ApplicationStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String candidateName;
    private String candidateEmail;
    private String resume;
    private LocalDateTime appliedDate;
    private String candidatePhone;

    @ManyToOne
    @JoinColumn(name = "job_offer_id", nullable = false)
    private JobOffer jobOffer;

    @Enumerated(EnumType.STRING)
    ApplicationStatus status;

    public Application() {}

    public Application(String candidateName, String candidateEmail, String resume, LocalDateTime appliedDate, String candidatePhone, JobOffer jobOffer, ApplicationStatus status) {
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.resume = resume;
        this.appliedDate = appliedDate;
        this.candidatePhone = candidatePhone;
        this.jobOffer = jobOffer;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getCandidatePhone() {
        return candidatePhone;
    }

    public void setCandidatePhone(String candidatePhone) {
        this.candidatePhone = candidatePhone;
    }

    public JobOffer getJobOffer() {
        return jobOffer;
    }

    public void setJobOffer(JobOffer jobOffer) {
        this.jobOffer = jobOffer;
    }

    public ApplicationStatus getStatus() {
        return status;
    }
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}

