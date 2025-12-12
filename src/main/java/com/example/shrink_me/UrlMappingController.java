package com.example.shrink_me;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class UrlMappingController {
    @Value("${websiteUrl}")
    private String websiteUrl;
    private final UrlMappingService service;

    public UrlMappingController(UrlMappingService service) {
        this.service = service;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<?> Shorten(@Valid @RequestBody RequestDto request) {
        UrlMapping mapping = service.shortenUrl(request.getUrl());
        var shortObj = Map.of(
                "Url", mapping.getLongUrl(),
                "shortUrl", websiteUrl + mapping.getShortKey(),
                "CreatedAt", mapping.getCreatedAt().toString());
        return ResponseEntity.ok(shortObj);
    }

    @GetMapping("/api/{shortKey:[a-zA-Z0-9]{7}}")
    public ResponseEntity<String> redirect(@PathVariable String shortKey) {
        String longUrl = service.resolve(shortKey);
        return ResponseEntity.ok(longUrl);
    }

    @GetMapping("/api/stats/{shortKey:[a-zA-Z0-9]{7}}")
    public ResponseEntity<?> stats(@PathVariable String shortKey) {
        UrlMapping mapping = service.getMapping(shortKey);
        var statsObj = Map.of(
                "Url", mapping.getLongUrl(),
                "ShortKey", websiteUrl + mapping.getShortKey(),
                "Clicks", mapping.getClicks());
        return ResponseEntity.ok(statsObj);
    }
}