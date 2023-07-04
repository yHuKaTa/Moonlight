package com.aacdemy.moonlight.service.impl;

import com.aacdemy.moonlight.dto.user.UserRegistrationRequestDto;
import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String fromEmail;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendRegistrationEmail(UserRegistrationRequestDto user) {

        String recipientEmail = user.getEmail();
        String subject = "Registration to Moonlight Hotel & Spa successful";
        String message = "Dear " + user.getFirstName() + ",\n\n" +
                "Your registration was successful. Your login details are:\n" +
                "Email: " + user.getEmail() + "\n" +
                "Password: " + user.getPassword() + "\n\n" +
                "Thank you for registering.";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setFrom(fromEmail);
        email.setText(message);

        try {
            javaMailSender.send(email);
        } catch (Exception ex) {
            System.err.println("Error sending registration email: " + ex.getMessage());
        }

    }

    @Override
    public void sendPasswordResetEmail(User user, String newPassword) {
        String recipientEmail = user.getEmail();
        String subject = "Moonlight Hotel & Spa password reset";
        String message = "Hello " + user.getFirstName() + ",\n\n" +
                " This is your new password for logging into the Moonlight Hotel & Spa system:" + "\n" +
                newPassword +
                " Once you have logged in, please change your password in the profile settings section." + "\n" + "\n" +
                "Best regards," + "\n" +
                "Moonlight Hotel & Spa";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("bootcamp@nasbg.com");
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message);

        try {
            javaMailSender.send(email);
        } catch (Exception ex) {
            System.err.println("Error sending registration email: " + ex.getMessage());
        }

    }


}
