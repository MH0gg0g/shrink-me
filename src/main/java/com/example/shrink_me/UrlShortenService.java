package com.example.shrink_me;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UrlShortenService {
    private final UrlMappingRepository repository;
    private final int defaultTtlMinutes;

    public UrlShortenService(UrlMappingRepository repository, @Value("${default-ttl-minutes}") int defaultTtlMinutes) {
        this.repository = repository;
        this.defaultTtlMinutes = defaultTtlMinutes;
    }

    @Transactional
    public UrlMapping shortenUrl(String longUrl) {
        return shortenUrl(longUrl, null);
    }

    @Transactional
    public UrlMapping shortenUrl(String longUrl, Integer ttlMinutes) {
        var existingMapping = repository.findByLongUrl(longUrl);

        if (existingMapping.isPresent()) {
            return existingMapping.get();
        }

        String shortkey;
        do {
            shortkey = RandomKey.generate();
        } while (repository.findByShortKey(shortkey).isPresent());

        int ttlToUse = (ttlMinutes != null) ? ttlMinutes : defaultTtlMinutes;
        UrlMapping newMapping = new UrlMapping(longUrl, shortkey, ttlToUse);
        repository.save(newMapping);

        return newMapping;
    }

    @Transactional
    public String resolve(String shortKey) {
        var mapping = this.getMapping(shortKey);
        this.incrementClicks(mapping);
        return mapping.getLongUrl();
    }

    public void incrementClicks(UrlMapping mapping) {
        mapping.setClicks(mapping.getClicks() + 1);
    }

    public UrlMapping getMapping(String shortKey) {
        return repository.findByShortKey(shortKey)
                .orElseThrow(() -> new RuntimeException("URL mapping not found"));
    }
}