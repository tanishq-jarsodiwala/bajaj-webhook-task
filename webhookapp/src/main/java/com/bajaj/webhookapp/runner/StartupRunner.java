package com.bajaj.webhookapp.runner;

import com.bajaj.webhookapp.service.WebhookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private final WebhookService service;

    public StartupRunner(WebhookService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        service.startProcess();
    }
}
