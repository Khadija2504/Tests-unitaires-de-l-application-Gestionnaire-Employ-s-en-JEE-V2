package com.hrmanagementsystem.dao.interfaces;

import com.hrmanagementsystem.entity.User;

public interface UserInterface {
    User findByEmail(String email);
}
