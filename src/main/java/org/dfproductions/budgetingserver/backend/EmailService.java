package org.dfproductions.budgetingserver.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRecoveryEmail(String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("budgeting.recovery@gmail.com");
        message.setTo(email);
        message.setSubject("Account Recovery");
        message.setText("You are receiving this email, because there was an attempt to recover your email. Your new password: \n" + password + "\nMake sure to change it AS SOON AS POSSIBLE!!!");

        mailSender.send(message);
    }

}
