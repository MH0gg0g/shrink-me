package com.example.shrink_me;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UrlMappingService {
    private final UrlMappingRepository repository;

    public UrlMappingService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UrlMapping shortenUrl(String longUrl) {
        var existingMapping = repository.findByLongUrl(longUrl);

        if (existingMapping.isPresent()) {
            return existingMapping.get();
        }

        String shortkey;
        do {
            shortkey = RandomKeyGenerator.generate();
        } while (repository.findByShortKey(shortkey).isPresent());

        UrlMapping newMapping = new UrlMapping(longUrl, shortkey);
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