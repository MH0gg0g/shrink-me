package com.example.shrink_me;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@AllArgsConstructor
public class UrlMappingController {
    private final UrlShortenService urlShortenService;

    @PostMapping("/api/shorten")
    public ResponseEntity<UrlMapping> Shortened(@Valid @RequestBody RequestDto request) {
        UrlMapping responseUrl = urlShortenService.shortenUrl(request.getUrl());
        return ResponseEntity.ok(responseUrl);
    }

    @GetMapping("/{shortKey}")
    public ResponseEntity<?> redirect(@PathVariable String shortKey) {
        var mapping = urlShortenService.resolve(shortKey);
        if (mapping.isEmpty()) {
            throw new RuntimeException("URL mapping not found");
        }
        urlShortenService.incrementClicks(mapping.get());
        return ResponseEntity.ok(mapping.get());
    }

    @GetMapping("/api/stats/{shortKey}")
    public ResponseEntity<?> stats(@PathVariable String shortKey) {
        var mapping = urlShortenService.resolve(shortKey);
        if (mapping.isEmpty()) {
            throw new RuntimeException("URL mapping not found");
        }
        return ResponseEntity.ok(mapping.get());
    }
}