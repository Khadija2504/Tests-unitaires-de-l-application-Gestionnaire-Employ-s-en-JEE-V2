package com.hrmanagementsystem.dao.interfaces;

public interface EmailSenderInterface {
    void sendEmail(String to, String subject, String body);
}