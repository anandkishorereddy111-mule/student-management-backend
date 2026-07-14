package com.students.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import com.students.dto.Course;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Async
    public void sendRegistrationConfirmation(String toEmail, String studentName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Registration Received - Student Management System");
        message.setText("Hi " + studentName + ",\n\nYour registration has been received successfully. "
                + "Your status is currently PENDING until payment is completed.\n\nThanks!");
        mailSender.send(message);
    }
    @Async
public void sendPaymentConfirmation(String toEmail, String studentName) {
    try {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Payment Received - Student Management System");
        message.setText("Hi " + studentName + ",\n\n" +
                "We've received your payment successfully. Your registration status has been updated to Complete.\n\n" +
                "Thank you!");
        mailSender.send(message);
    } catch (Exception e) {
        System.out.println("Failed to send payment confirmation email: " + e.getMessage());
    }
}
}