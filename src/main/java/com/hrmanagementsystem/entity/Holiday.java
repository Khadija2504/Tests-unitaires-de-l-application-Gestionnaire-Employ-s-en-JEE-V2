package com.hrmanagementsystem.entity;

import com.hrmanagementsystem.enums.HolidayStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "holidays")
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date startDate;
    private Date endDate;
    private String reason;
    private String Justification;

    @Enumerated(EnumType.STRING)
    private HolidayStatus status;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    public Holiday() {}

    public Holiday(Date startDate, Date endDate, String reason, String justification, HolidayStatus status, User employee) {

        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        Justification = justification;
        this.status = status;
        this.employee = employee;
    }

    public Holiday(Date startDate, Date endDate, String reason, String filePath, User employee) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        Justification = filePath;
        this.employee = employee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getJustification() {
        return Justification;
    }

    public void setJustification(String justification) {
        Justification = justification;
    }

    public HolidayStatus getStatus() {
        return status;
    }

    public void setStatus(HolidayStatus status) {
        this.status = status;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }
}

