package com.bajaj.webhookapp.service;

import com.bajaj.webhookapp.model.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebhookService {

    private final WebClient webClient;

    public WebhookService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void startProcess() {

        System.out.println("STEP 1 → Generating webhook");

        WebhookResponse response = generateWebhook();

        String webhookUrl = response.getWebhook();
        String token = response.getAccessToken();

        System.out.println("Webhook URL: " + webhookUrl);
        System.out.println("JWT Token: " + token);

        // ✅ FINAL SQL QUERY (Question 2 - Even RegNo)
        String finalSQLQuery =
                "SELECT e.employee_id, e.employee_name, d.department_name, " +
                "SUM(s.salary_amount) AS total_salary " +
                "FROM employees e " +
                "JOIN departments d ON e.department_id = d.department_id " +
                "JOIN salaries s ON e.employee_id = s.employee_id " +
                "GROUP BY e.employee_id, e.employee_name, d.department_name " +
                "ORDER BY total_salary DESC";

        System.out.println("STEP 2 → Sending final SQL query");

        sendFinalQuery(webhookUrl, token, finalSQLQuery);
    }

    private WebhookResponse generateWebhook() {

        WebhookRequest request = new WebhookRequest();
        request.setName("Tanishq Jarsodiwala");
        request.setRegNo("250850120178");
        request.setEmail("tj.twits@gmail.com"); 

        return webClient.post()
                .uri("https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(WebhookResponse.class)
                .block();
    }

    private void sendFinalQuery(String webhookUrl, String token, String query) {

        FinalQueryRequest body = new FinalQueryRequest(query);

        String response = webClient.post()
                .uri(webhookUrl)
                .header(HttpHeaders.AUTHORIZATION, token)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Submission Response: " + response);
    }
}
