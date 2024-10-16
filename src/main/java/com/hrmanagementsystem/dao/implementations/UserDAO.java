package com.hrmanagementsystem.dao.implementations;

import com.hrmanagementsystem.dao.interfaces.UserInterface;
import com.hrmanagementsystem.entity.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

public class UserDAO implements UserInterface {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("hr_management_pu");

    @Override
    public User findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}