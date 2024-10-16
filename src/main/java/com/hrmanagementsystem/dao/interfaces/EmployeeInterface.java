package com.hrmanagementsystem.dao.interfaces;

import com.hrmanagementsystem.entity.User;
import com.hrmanagementsystem.enums.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public interface EmployeeInterface {
    User getById(int id);

    boolean getByEmail(String email);
    boolean save(User user);

    void delete(int id);

    boolean update(User user);

    User getByUsername(String username);

    List<User> getAll();
    User findByNssu(String nssu);
}
