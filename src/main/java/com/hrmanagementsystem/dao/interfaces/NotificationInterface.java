package com.hrmanagementsystem.dao.interfaces;

import com.hrmanagementsystem.entity.Notification;

import javax.persistence.EntityManager;

public interface NotificationInterface {
    void save(Notification notification);
}
