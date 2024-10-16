package com.hrmanagementsystem.dao.interfaces;

import com.hrmanagementsystem.entity.Holiday;
import com.hrmanagementsystem.entity.User;
import com.hrmanagementsystem.enums.HolidayStatus;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface HolidayInterface {
    Holiday getById(int id);

    List<Holiday> getAcceptedHolidaysForEmployee(User employee);

    Map<String, Map<String, Object>> getMonthlyAbsenceReport(int year, int month);

    void save(Holiday holiday);

    List<Holiday> getAllHolidays();

    void update(Holiday holiday);
}
