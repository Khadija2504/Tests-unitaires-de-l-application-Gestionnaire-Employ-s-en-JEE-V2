package com.hrmanagementsystem.controller;

import com.hrmanagementsystem.dao.implementations.EmployeeDAO;
import com.hrmanagementsystem.dao.interfaces.EmployeeInterface;
import com.hrmanagementsystem.service.EmployeeService;
import com.hrmanagementsystem.util.AdminInitializer;
import com.hrmanagementsystem.util.PersistenceUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AdminInitializerListenerServlet implements ServletContextListener {
    protected EmployeeInterface EmployeeDAO = new EmployeeDAO();
    protected EmployeeService employeeService = new EmployeeService(EmployeeDAO);
    protected AdminInitializer adminInitializer = new AdminInitializer(employeeService);

    private EntityManagerFactory emf;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        emf = PersistenceUtil.getEntityFactory();
        EntityManager em = emf.createEntityManager();
        try {
            adminInitializer.initializeAdminUser();
        } finally {
            em.close();
        }
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}