package com.assa.studentregistration.email;

import javax.mail.MessagingException;

public interface EmailSender {
    void send(String to, String message) throws MessagingException;
}
