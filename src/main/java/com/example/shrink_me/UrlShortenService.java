package com.example.shrink_me;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UrlShortenService {
    private final UrlMappingRepository urlMappingRepository;

    @Transactional
    public UrlMapping shortenUrl(String longUrl) {
        Optional<UrlMapping> existingMapping = urlMappingRepository.findByLongUrl(longUrl);

        if (existingMapping.isPresent()) {
            return existingMapping.get();
        }

        String shortkey;
        do {
            shortkey = RandomKey.generate();
        } while (urlMappingRepository.findByShortKey(shortkey).isPresent());

        UrlMapping newMapping = new UrlMapping(longUrl, shortkey);
        urlMappingRepository.save(newMapping);

        return newMapping;
    }

    @Transactional(readOnly = true)
    public String getLongUrl(String shortKey) {
        Optional<UrlMapping> mapping = urlMappingRepository.findByShortKey(shortKey);

        return mapping.map(UrlMapping::getLongUrl).orElseThrow();
    }

    public Optional<UrlMapping> resolve(String key) {
        return urlMappingRepository.findByShortKey(key);
    }

    @Transactional
    public void incrementClicks(UrlMapping mapping) {
        mapping.setClicks(mapping.getClicks() + 1);
        urlMappingRepository.save(mapping);
    }
}
