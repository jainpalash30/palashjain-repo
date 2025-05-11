package com.example.bfhl.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void executeWorkflow() {
        String name = "John Doe";
        String regNo = "REG12347";
        String email = "john@example.com";

        // 1. Generate webhook
        String genUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("regNo", regNo);
        requestBody.put("email", email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(genUrl, entity, Map.class);
        String webhookUrl = (String) response.getBody().get("webhook");
        String token = (String) response.getBody().get("accessToken");

        System.out.println("Webhook: " + webhookUrl);
        System.out.println("Token: " + token);

        // 2. Manually solve SQL from downloaded link
        String finalQuery = "SELECT name FROM users WHERE status = 'active';"; // Replace with real query

        // 3. Submit final query
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
        authHeaders.setBearerAuth(token);

        Map<String, String> submitBody = new HashMap<>();
        submitBody.put("finalQuery", finalQuery);

        HttpEntity<Map<String, String>> submitEntity = new HttpEntity<>(submitBody, authHeaders);

        ResponseEntity<String> submitResponse = restTemplate.postForEntity(webhookUrl, submitEntity, String.class);
        System.out.println("Submission response: " + submitResponse.getStatusCode() + " - " + submitResponse.getBody());
    }
}
