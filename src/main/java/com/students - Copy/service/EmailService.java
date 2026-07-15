package com.students.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${mail.from}")
    private String fromEmail;

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Async
    public void sendRegistrationConfirmation(String toEmail, String studentName) {
        String subject = "Registration Received - Student Management System";
        String body = "Hi " + studentName + ",\n\nYour registration has been received successfully. "
                + "Your status is currently PENDING until payment is completed.\n\nThanks!";
        sendEmail(toEmail, studentName, subject, body);
    }

    @Async
    public void sendPaymentConfirmation(String toEmail, String studentName) {
        String subject = "Payment Received - Student Management System";
        String body = "Hi " + studentName + ",\n\n"
                + "We've received your payment successfully. Your registration status has been updated to Complete.\n\n"
                + "Thank you!";
        sendEmail(toEmail, studentName, subject, body);
    }

    private void sendEmail(String toEmail, String toName, String subject, String textBody) {
        try {
            JSONObject sender = new JSONObject();
            sender.put("email", fromEmail);
            sender.put("name", "Student Management System");

            JSONObject recipient = new JSONObject();
            recipient.put("email", toEmail);
            recipient.put("name", toName);

            JSONObject payload = new JSONObject();
            payload.put("sender", sender);
            payload.put("to", new JSONObject[]{recipient});
            payload.put("subject", subject);
            payload.put("textContent", textBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BREVO_URL))
                    .header("accept", "application/json")
                    .header("api-key", brevoApiKey)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("Email sent successfully to " + toEmail + " (" + subject + ")");
            } else {
                System.out.println("Brevo email failed [" + response.statusCode() + "]: " + response.body());
            }
        } catch (Exception e) {
            System.out.println("Failed to send email to " + toEmail + ": " + e.getMessage());
        }
    }
}