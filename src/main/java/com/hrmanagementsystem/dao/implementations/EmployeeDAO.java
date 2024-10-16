package com.hrmanagementsystem.dao.implementations;

import com.hrmanagementsystem.dao.interfaces.EmployeeInterface;
import com.hrmanagementsystem.entity.User;
import com.hrmanagementsystem.enums.Role;

import javax.persistence.*;
import java.util.List;

public class EmployeeDAO implements EmployeeInterface {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("hr_management_pu");

    @Override
    public User getById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean getByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean save(User user) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
            query.setParameter("email", user.getEmail());
            boolean emailExists = query.getSingleResult() > 0;

            TypedQuery<Long> query1 = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.nssu = :nssu", Long.class);
            query1.setParameter("nssu", user.getNssu());
            boolean nssuExists = query1.getSingleResult() > 0;
            if (emailExists || nssuExists) {
                tx.rollback();
                return false;
            }

            em.persist(user);
            tx.commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean update(User user) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
            query.setParameter("email", user.getEmail());
            boolean emailExists = query.getSingleResult() > 0;

            if (emailExists) {
                tx.rollback();
                return false;
            }

            em.persist(user);
            tx.commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public User getByUsername(String username) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.firstName = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.role = :role", User.class);
            query.setParameter("role", Role.Employee);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public User findByNssu(String nssu) {
        EntityManager em = emf.createEntityManager();
        try{
            return em.createQuery("SELECT u FROM User u WHERE u.nssu = :nssu", User.class)
                    .setParameter("nssu", nssu).getSingleResult();
        }catch (NoResultException e) {
            return null;
        }
    }
}
