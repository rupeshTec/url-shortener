package com.projects.url_shortener.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaffeineCacheService {

    @Autowired
    private Cache<String, String> cache;

    public void saveToCache(String shortUrl, String longUrl) {
        cache.put(shortUrl, longUrl);
        System.out.println(cache.asMap());
    }

    public String getFromCache(String shortUrl) {
        return cache.getIfPresent(shortUrl);
    }
}

