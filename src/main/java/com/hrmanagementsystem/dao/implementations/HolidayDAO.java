package com.hrmanagementsystem.dao.implementations;

import com.hrmanagementsystem.dao.interfaces.HolidayInterface;
import com.hrmanagementsystem.entity.Holiday;
import com.hrmanagementsystem.entity.User;
import com.hrmanagementsystem.enums.HolidayStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HolidayDAO implements HolidayInterface {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("hr_management_pu");

    @Override
    public Holiday getById(int id) {
        EntityManager em = emf.createEntityManager();
        try{
            return em.find(Holiday.class, id);
        }finally {
            em.close();
        }
    }

    @Override
    public List<Holiday> getAcceptedHolidaysForEmployee(User employee) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Holiday> query = em.createQuery(
                    "SELECT h FROM Holiday h WHERE h.employee = :employee AND h.status = :status",
                    Holiday.class);
            query.setParameter("employee", employee);
            query.setParameter("status", HolidayStatus.Accepted);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Map<String, Map<String, Object>> getMonthlyAbsenceReport(int year, int month) {
        EntityManager em = emf.createEntityManager();
        try {
            LocalDate startOfMonth = LocalDate.of(year, month, 1);
            LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

            TypedQuery<Holiday> query = em.createQuery(
                    "SELECT h FROM Holiday h WHERE h.startDate >= :startDate AND h.endDate <= :endDate",
                    Holiday.class
            );
            query.setParameter("startDate", Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            query.setParameter("endDate", Date.from(endOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()));

            List<Holiday> holidays = query.getResultList();

            Map<String, Map<String, Object>> report = new HashMap<>();

            for (Holiday holiday : holidays) {
                String reason = holiday.getReason();
                long days = (holiday.getEndDate().getTime() - holiday.getStartDate().getTime()) / (1000 * 60 * 60 * 24) + 1;

                report.putIfAbsent(reason, new HashMap<>());
                Map<String, Object> reasonStats = report.get(reason);
                reasonStats.put("count", (int) reasonStats.getOrDefault("count", 0) + 1);
                reasonStats.put("totalDays", (long) reasonStats.getOrDefault("totalDays", 0L) + days);
            }

            return report;
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Holiday holiday) {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(holiday);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
    }

    @Override
    public List<Holiday> getAllHolidays() {
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<Holiday> query = em.createQuery("FROM Holiday", Holiday.class);
            return query.getResultList();
        }finally {
            em.close();
        }
    }

    @Override
    public void update(Holiday holiday) {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.merge(holiday);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
