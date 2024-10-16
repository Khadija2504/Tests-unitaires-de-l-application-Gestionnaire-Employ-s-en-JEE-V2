package com.hrmanagementsystem.dao.interfaces;

import com.hrmanagementsystem.entity.JobOffer;
import com.hrmanagementsystem.enums.JobOfferStatus;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public interface JobOfferInterface {
    JobOffer getById(int id);

    void save(JobOffer jobOffer);

    void update(JobOffer jobOffer);

    void delete(int id);

    List<JobOffer> getAll();
    List<JobOffer> getByStatus(JobOfferStatus status);
}
