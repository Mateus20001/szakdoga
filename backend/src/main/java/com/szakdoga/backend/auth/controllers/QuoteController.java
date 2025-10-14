package com.szakdoga.backend.auth.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/quote")
public class QuoteController {
    @GetMapping("/daily-quote")
    public Object getDailyQuote() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://zenquotes.io/api/today";
        return restTemplate.getForObject(url, Object.class);
    }
}
