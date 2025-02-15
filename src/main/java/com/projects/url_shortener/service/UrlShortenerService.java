package com.projects.url_shortener.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projects.url_shortener.entity.UrlMapping;
import com.projects.url_shortener.repo.UrlMappingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {
    @Autowired
    private UrlMappingRepository repository;

    @Autowired
    private CaffeineCacheService cacheService;

    public String generateShortUrl(String longUrl) {
        Optional<UrlMapping> existing = repository.findByLongUrl(longUrl);
        if (existing.isPresent()) {
            return existing.get().getShortUrl();
        }

        String shortUrl;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(longUrl.getBytes());

            // Encode to Base64 and take the first 6 characters
            shortUrl = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes).substring(0, 6);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }

        // String shortUrl = RandomStringUtils.randomAlphanumeric(6); // Generates a 6-character short URL
        UrlMapping urlMapping = new UrlMapping(null, shortUrl, longUrl, null);
        repository.save(urlMapping);

        // Save in Caffeine cache
        cacheService.saveToCache(shortUrl, longUrl);

        return shortUrl;
    }

    public String getLongUrl(String shortUrl) {

        // First check Caffeine Cache
        String cachedUrl = cacheService.getFromCache(shortUrl.substring(shortUrl.lastIndexOf("/") + 1));
        if (cachedUrl != null) {
            return cachedUrl;
        }
    
        return repository.findByShortUrl(shortUrl.substring(shortUrl.lastIndexOf("/") + 1))
                .map(UrlMapping::getLongUrl)
                .orElseThrow(() -> new RuntimeException("Short URL not found!"));
    }
    
}

