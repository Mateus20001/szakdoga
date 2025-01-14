package com.szakdoga.backend.auth.services;

import com.szakdoga.backend.auth.controllers.UserController;
import com.szakdoga.backend.auth.model.EmailEntity;
import com.szakdoga.backend.auth.repositories.EmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class EmailService {
    private String apiKey = "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";

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String to, String userId, String password) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.set("Content-Type", "application/json");

        String emailBody = "<h1>Welcome to the University of Szeged!</h1>" +
                "<p>Welcome to the University of Szeged!</p>" +
                "<p>Your ID: " + userId + "</p>" +
                "<p>Your password: " + password + "</p>";

        // Prepare the payload with the dynamic values
        String payload = "{\n" +
                "  \"sender\": {\"email\": \"justforgenshin50@gmail.com\"},\n" +
                "  \"to\": [{\"email\": \"" + to + "\"}],\n" +
                "  \"subject\": \"Welcome to the University of Szeged!\",\n" +
                "  \"htmlContent\": \"" + emailBody + "\"\n" +
                "}";

        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            log.info("Email sent successfully: " + response.getBody());
        } catch (Exception e) {
            log.error("Error sending email: " + e.getMessage());
        }
    }
}