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
    private final UrlShortenService service;

    public UrlMappingController(UrlShortenService service) {
        this.service = service;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<?> Shorten(@Valid @RequestBody RequestDto request) {
        UrlMapping mapping = service.shortenUrl(request.getUrl(), request.getTtlMinutes());
        var shortObj = Map.of(
                "Url", mapping.getLongUrl(),
                "shortUrl", websiteUrl + mapping.getShortKey(),
                "CreatedAt", mapping.getCreatedAt().toString(),
                "ExpiresAt", mapping.getExpiryDate().toString());
        return ResponseEntity.ok(shortObj);
    }

    @GetMapping("/{shortKey:[a-zA-Z0-9_-]{4,64}}")
    public ResponseEntity<String> redirect(@PathVariable String shortKey) {
        var longUrl = service.resolve(shortKey);
        return ResponseEntity.ok(longUrl);
    }

    @GetMapping("/api/stats/{shortKey}")
    public ResponseEntity<?> stats(@PathVariable String shortKey) {
        var mapping = service.getMapping(shortKey);
        var statsObj = Map.of(
                "Url", mapping.getLongUrl(),
                "shortUrl", websiteUrl + mapping.getShortKey(),
                "clicks", mapping.getClicks());
        return ResponseEntity.ok(statsObj);
    }
}