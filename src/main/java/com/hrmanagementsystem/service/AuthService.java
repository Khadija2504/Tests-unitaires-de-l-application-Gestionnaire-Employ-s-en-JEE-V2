package com.hrmanagementsystem.service;

import com.hrmanagementsystem.dao.interfaces.UserInterface;
import com.hrmanagementsystem.entity.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    protected UserInterface userInterface;
    public AuthService(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public User authenticate(String email, String password) {
        User user = userInterface.findByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

}
