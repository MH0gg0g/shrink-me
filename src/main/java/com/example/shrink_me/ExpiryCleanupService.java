package com.example.shrink_me;

import java.time.ZoneId;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExpiryCleanupService {
    private static final long ONE_MONTH_MILLIS = 30L * 24 * 60 * 60 * 1000;
    private final UrlMappingRepository repository;

    public ExpiryCleanupService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanUpExpiredMappings() {
        System.out.println("------------------cleanUpExpiredMappings-----------------");
        var mappings = repository.findAll();
        long now = System.currentTimeMillis();
        for (var mapping : mappings) {
            long createdAt = mapping.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            if (now - createdAt > ONE_MONTH_MILLIS) {
                if (mapping.getClicks() > 0) {
                    mapping.setClicks(0);
                    repository.save(mapping);
                } else {
                    repository.delete(mapping);
                }
            }
        }
    }
}
