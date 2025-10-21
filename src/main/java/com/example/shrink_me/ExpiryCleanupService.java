package com.example.shrink_me;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExpiryCleanupService {
    private final UrlMappingRepository repository;

    public ExpiryCleanupService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.MINUTES) // low time for demo purposes
    public void cleanupExpired() {
        LocalDateTime now = LocalDateTime.now();
        repository.deleteExpiredUrls(now);
    }
}
