package com.hrmanagementsystem.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUtil {
    private final static EntityManagerFactory emf;
    private static EntityManager em;

    static{
        try {
            emf = Persistence.createEntityManagerFactory("hr_management_pu");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static EntityManagerFactory getEntityFactory(){
        return emf;
    }
}
