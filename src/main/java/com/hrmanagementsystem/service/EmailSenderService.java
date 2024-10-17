package com.hrmanagementsystem.service;

import com.hrmanagementsystem.dao.interfaces.EmailSenderInterface;
import com.hrmanagementsystem.util.EmailSender;

public class EmailSenderService implements EmailSenderInterface {
    @Override
    public void sendEmail(String to, String subject, String body) {
        EmailSender.sendEmail(to, subject, body);
    }
}
