package com.hrmanagementsystem.dao.implementations;

import com.hrmanagementsystem.dao.interfaces.ApplicationInterface;
import com.hrmanagementsystem.entity.Application;
import com.hrmanagementsystem.entity.User;
import com.hrmanagementsystem.enums.ApplicationStatus;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class ApplicationDAO implements ApplicationInterface {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("hr_management_pu");

    @Override
    public Application getById(int id) {
        EntityManager em = emf.createEntityManager();
        try{
            return em.find(Application.class,id);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Application application) {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(application);
            em.getTransaction().commit();
        }catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Application> getAllByJobOfferId(int jobOfferId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Application> query = em.createQuery(
                    "SELECT a FROM Application a WHERE a.jobOffer.id = :jobOfferId",
                    Application.class
            );
            query.setParameter("jobOfferId", jobOfferId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Application> getFilteredApplications(int jobOfferId, ApplicationStatus status) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Application> query = em.createQuery(
                    "SELECT a FROM Application a WHERE a.jobOffer.id = :jobOfferId AND a.status = :status",
                    Application.class
            );
            query.setParameter("jobOfferId", jobOfferId);
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    @Override
    public void updateStatus(Application application) {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.merge(application);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

}
