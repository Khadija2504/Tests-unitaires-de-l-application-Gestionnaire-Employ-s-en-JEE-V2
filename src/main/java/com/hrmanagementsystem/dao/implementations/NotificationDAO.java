package com.hrmanagementsystem.dao.implementations;

import com.hrmanagementsystem.dao.interfaces.NotificationInterface;
import com.hrmanagementsystem.entity.Notification;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class NotificationDAO implements NotificationInterface {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("hr_management_pu");

    @Override
    public void save(Notification notification) {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(notification);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
