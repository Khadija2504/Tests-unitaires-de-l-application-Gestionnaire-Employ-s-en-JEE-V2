package com.hrmanagementsystem.dao.interfaces;

import com.hrmanagementsystem.entity.Application;
import com.hrmanagementsystem.enums.ApplicationStatus;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public interface ApplicationInterface {
    Application getById(int id);

    void save(Application application);

    List<Application> getAllByJobOfferId(int jobOfferId);

    List<Application> getFilteredApplications(int jobOfferId, ApplicationStatus status);

    void updateStatus(Application application);
}
